package com.jobtracker.dao;

import com.jobtracker.util.Constants;
import com.jobtracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDAO {

  public Map<String, Integer> countByStatus(int userId) throws SQLException {
    Map<String, Integer> counts = new LinkedHashMap<>();
    for (String s : Constants.STATUSES)
      counts.put(s, 0);

    String sql = "SELECT status, COUNT(*) AS total FROM job_applications "
        + "WHERE user_id = ? GROUP BY status";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, userId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          counts.put(rs.getString("status"), rs.getInt("total"));
        }
      }
    }
    return counts;
  }
}