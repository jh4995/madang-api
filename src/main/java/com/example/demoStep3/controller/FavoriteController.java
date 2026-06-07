package com.example.demoStep3.controller;

import com.example.demoStep3.dto.FavoriteDto;
import com.example.demoStep3.dto.FavoriteWithCongestionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private DataSource dataSource;

    // ───────────────────────────────────────
    // (d) 즐겨찾기 등록
    // POST /api/favorites  { "userId": "user1", "id": "K323" }
    // ───────────────────────────────────────
    @PostMapping
    public String addFavorite(@RequestBody FavoriteDto dto) {
        String sql = "INSERT INTO favorite (user_id, id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dto.getUserId());
            stmt.setString(2, dto.getId());
            stmt.executeUpdate();
            return "즐겨찾기 등록 완료";
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                return "이미 즐겨찾기에 등록된 코스입니다.";
            }
            e.printStackTrace();
            return "등록 실패: " + e.getMessage();
        }
    }

    // ───────────────────────────────────────
    // 즐겨찾기 삭제
    // DELETE /api/favorites?userId=user1&id=K323
    // ───────────────────────────────────────
    @DeleteMapping
    public String removeFavorite(@RequestParam String userId, @RequestParam String id) {
        String sql = "DELETE FROM favorite WHERE user_id = ? AND id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0 ? "즐겨찾기 삭제 완료" : "해당 즐겨찾기를 찾을 수 없습니다.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "삭제 실패: " + e.getMessage();
        }
    }

    // ───────────────────────────────────────
    // (d) 즐겨찾기 목록 + 혼잡도
    // GET /api/favorites/user1
    // ───────────────────────────────────────
    @GetMapping("/{userId}")
    public List<FavoriteWithCongestionDto> getFavorites(@PathVariable String userId) {
        List<FavoriteWithCongestionDto> result = new ArrayList<>();
        String sql = "SELECT f.id, w1.trail_name, w1.course_name, w1.district_name, " +
                "w2.difficulty, w2.course_km, " +
                "IFNULL(v.now_walking, 0) AS now_walking, " +
                "f.created_at " +
                "FROM favorite f " +
                "JOIN walk1 w1 ON f.id = w1.id " +
                "JOIN walk2 w2 ON f.id = w2.id " +
                "LEFT JOIN (SELECT id, COUNT(*) AS now_walking FROM visit_log " +
                "  WHERE exit_time IS NULL GROUP BY id) v ON f.id = v.id " +
                "WHERE f.user_id = ? " +
                "ORDER BY f.created_at DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int count = rs.getInt("now_walking");
                String grade;
                if (count <= 5) grade = "여유";
                else if (count <= 15) grade = "보통";
                else grade = "혼잡";

                result.add(new FavoriteWithCongestionDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("district_name"),
                        rs.getString("difficulty"), rs.getDouble("course_km"),
                        count, grade, rs.getString("created_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }
}