package com.example.demoStep3.controller;

import com.example.demoStep3.dto.WalkViewDto;
import com.example.demoStep3.dto.WalkDetailDto;
import com.example.demoStep3.dto.WalkCongestionDto;
import com.example.demoStep3.dto.WalkNearbyDto;
import com.example.demoStep3.dto.HourlyStatsDto;
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
@RequestMapping("/api/walks")
public class WalkController {

    @Autowired
    private DataSource dataSource;

    // ───────────────────────────────────────
    // 기본 CRUD
    // ───────────────────────────────────────

    // GET /api/walks — 전체 산책로 목록
    @GetMapping
    public List<WalkViewDto> getAllWalks() {
        List<WalkViewDto> result = new ArrayList<>();
        String sql = "SELECT w1.id, w1.trail_name, w1.course_name, w1.district_name, " +
                "w2.difficulty, w2.course_km, w2.minutes " +
                "FROM walk1 w1 JOIN walk2 w2 ON w1.id = w2.id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(new WalkViewDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("district_name"),
                        rs.getString("difficulty"), rs.getDouble("course_km"),
                        rs.getInt("minutes")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // GET /api/walks/{id} — 특정 산책로 상세
    @GetMapping("/{id}")
    public WalkDetailDto getWalkById(@PathVariable String id) {
        String sql = "SELECT w1.id, w1.trail_name, w1.course_name, w1.course_desc, w1.district_name, " +
                "w2.difficulty, w2.course_km, w2.hour, w2.minute, w2.minutes, w2.water, w2.toilet, " +
                "w3.address, w3.lat, w3.lng " +
                "FROM walk1 w1 JOIN walk2 w2 ON w1.id = w2.id JOIN walk3 w3 ON w1.id = w3.id " +
                "WHERE w1.id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new WalkDetailDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("course_desc"),
                        rs.getString("district_name"), rs.getString("difficulty"),
                        rs.getDouble("course_km"), rs.getInt("hour"), rs.getInt("minute"),
                        rs.getInt("minutes"), rs.getString("water"), rs.getString("toilet"),
                        rs.getString("address"), rs.getDouble("lat"), rs.getDouble("lng")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // GET /api/walks/search?district=대전 — 지역별 검색
    @GetMapping("/search")
    public List<WalkViewDto> searchByDistrict(@RequestParam String district) {
        List<WalkViewDto> result = new ArrayList<>();
        String sql = "SELECT w1.id, w1.trail_name, w1.course_name, w1.district_name, " +
                "w2.difficulty, w2.course_km, w2.minutes " +
                "FROM walk1 w1 JOIN walk2 w2 ON w1.id = w2.id " +
                "WHERE w1.district_name LIKE ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + district + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new WalkViewDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("district_name"),
                        rs.getString("difficulty"), rs.getDouble("course_km"),
                        rs.getInt("minutes")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // ───────────────────────────────────────
    // (a) 내 주변 + 조건 검색 (거리순) — 기능2
    // GET /api/walks/nearby?lat=36.37&lng=127.38&km=10
    // ───────────────────────────────────────
    @GetMapping("/nearby")
    public List<WalkNearbyDto> getNearbyWalks(
            @RequestParam double lat, @RequestParam double lng,
            @RequestParam(defaultValue = "10") double km,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String water,
            @RequestParam(required = false) String toilet) {

        List<WalkNearbyDto> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT w1.id, w1.trail_name, w1.course_name, w1.district_name, " +
                        "w2.difficulty, w2.course_km, w2.minutes, w2.water, w2.toilet, " +
                        "w3.lat, w3.lng, " +
                        "fn_distance(?, ?, w3.lat, w3.lng) AS distance_m " +
                        "FROM walk1 w1 JOIN walk2 w2 ON w1.id = w2.id JOIN walk3 w3 ON w1.id = w3.id " +
                        "WHERE fn_distance(?, ?, w3.lat, w3.lng) <= ? "
        );

        if (difficulty != null) sql.append("AND w2.difficulty = ? ");
        if (water != null) sql.append("AND w2.water = ? ");
        if (toilet != null) sql.append("AND w2.toilet = ? ");
        sql.append("ORDER BY distance_m ASC");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;
            stmt.setDouble(idx++, lat);
            stmt.setDouble(idx++, lng);
            stmt.setDouble(idx++, lat);
            stmt.setDouble(idx++, lng);
            stmt.setDouble(idx++, km * 1000);
            if (difficulty != null) stmt.setString(idx++, difficulty);
            if (water != null) stmt.setString(idx++, water);
            if (toilet != null) stmt.setString(idx++, toilet);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new WalkNearbyDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("district_name"),
                        rs.getString("difficulty"), rs.getDouble("course_km"),
                        rs.getInt("minutes"), rs.getString("water"), rs.getString("toilet"),
                        rs.getDouble("lat"), rs.getDouble("lng"),
                        Math.round(rs.getDouble("distance_m"))
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // ───────────────────────────────────────
    // (b) 코스 현재 혼잡도 — 기능1
    // GET /api/walks/congestion
    // ───────────────────────────────────────
    @GetMapping("/congestion")
    public List<WalkCongestionDto> getCongestion() {
        List<WalkCongestionDto> result = new ArrayList<>();
        String sql = "SELECT w1.id, w1.trail_name, w1.district_name, " +
                "IFNULL(v.now_walking, 0) AS now_walking " +
                "FROM walk1 w1 LEFT JOIN (" +
                "  SELECT id, COUNT(*) AS now_walking FROM visit_log " +
                "  WHERE exit_time IS NULL GROUP BY id" +
                ") v ON w1.id = v.id " +
                "WHERE IFNULL(v.now_walking, 0) > 0 " +
                "ORDER BY now_walking DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int count = rs.getInt("now_walking");
                String grade;
                if (count <= 5) grade = "여유";
                else if (count <= 15) grade = "보통";
                else grade = "혼잡";

                result.add(new WalkCongestionDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("district_name"), count, grade
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // ───────────────────────────────────────
    // (c) 한산한 코스 추천 — 기능1
    // GET /api/walks/quiet?lat=36.37&lng=127.38&km=15
    // ───────────────────────────────────────
    @GetMapping("/quiet")
    public List<WalkNearbyDto> getQuietWalks(
            @RequestParam double lat, @RequestParam double lng,
            @RequestParam(defaultValue = "15") double km) {

        List<WalkNearbyDto> result = new ArrayList<>();
        String sql = "SELECT w1.id, w1.trail_name, w1.course_name, w1.district_name, " +
                "w2.difficulty, w2.course_km, w2.minutes, w2.water, w2.toilet, " +
                "w3.lat, w3.lng, " +
                "fn_distance(?, ?, w3.lat, w3.lng) AS distance_m, " +
                "IFNULL(v.now_walking, 0) AS now_walking " +
                "FROM walk1 w1 JOIN walk2 w2 ON w1.id = w2.id " +
                "JOIN walk3 w3 ON w1.id = w3.id " +
                "LEFT JOIN (SELECT id, COUNT(*) AS now_walking FROM visit_log " +
                "  WHERE exit_time IS NULL GROUP BY id) v ON w1.id = v.id " +
                "WHERE fn_distance(?, ?, w3.lat, w3.lng) <= ? " +
                "ORDER BY now_walking ASC, distance_m ASC LIMIT 10";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lng);
            stmt.setDouble(3, lat);
            stmt.setDouble(4, lng);
            stmt.setDouble(5, km * 1000);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new WalkNearbyDto(
                        rs.getString("id"), rs.getString("trail_name"),
                        rs.getString("course_name"), rs.getString("district_name"),
                        rs.getString("difficulty"), rs.getDouble("course_km"),
                        rs.getInt("minutes"), rs.getString("water"), rs.getString("toilet"),
                        rs.getDouble("lat"), rs.getDouble("lng"),
                        Math.round(rs.getDouble("distance_m"))
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }

    // ───────────────────────────────────────
    // (e) 시간대별 평소 혼잡 조회 — 기능1
    // GET /api/walks/K323/hourly
    // ───────────────────────────────────────
    @GetMapping("/{id}/hourly")
    public List<HourlyStatsDto> getHourlyStats(@PathVariable String id) {
        List<HourlyStatsDto> result = new ArrayList<>();
        String sql = "SELECT id, hour, avg_entries, sample_days " +
                "FROM course_hourly_stats WHERE id = ? ORDER BY hour";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new HourlyStatsDto(
                        rs.getString("id"), rs.getInt("hour"),
                        rs.getDouble("avg_entries"), rs.getInt("sample_days")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
    }
}