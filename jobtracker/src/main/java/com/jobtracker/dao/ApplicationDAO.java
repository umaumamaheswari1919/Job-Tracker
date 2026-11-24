package com.jobtracker.dao;

import com.jobtracker.model.JobApplication;
import com.jobtracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

  private static final String COLUMNS = "application_id, user_id, company_name, job_role, location, job_type, "
      + "application_date, job_description, job_url, status, created_at";

  public boolean add(JobApplication a) throws SQLException {
    String sql = "INSERT INTO job_applications "
        + "(user_id, company_name, job_role, location, job_type, application_date, "
        + "job_description, job_url, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, a.getUserId());
      ps.setString(2, a.getCompanyName());
      ps.setString(3, a.getJobRole());
      ps.setString(4, a.getLocation());
      ps.setString(5, a.getJobType());
      ps.setDate(6, a.getApplicationDate());
      ps.setString(7, a.getJobDescription());
      ps.setString(8, a.getJobUrl());
      ps.setString(9, a.getStatus());
      return ps.executeUpdate() > 0;
    }
  }

  public List<JobApplication> list(int userId, String search, String status,
      boolean newestFirst) throws SQLException {
    StringBuilder sql = new StringBuilder(
        "SELECT " + COLUMNS + " FROM job_applications WHERE user_id = ?");
    List<Object> params = new ArrayList<>();
    params.add(userId);

    if (search != null && !search.isBlank()) {
      String escaped = search.trim()
          .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
      sql.append(" AND company_name LIKE ?");
      params.add("%" + escaped + "%");
    }
    if (status != null && !status.isBlank()) {
      sql.append(" AND status = ?");
      params.add(status);
    }
    // sort direction is chosen from two fixed strings, never from user text
    sql.append(newestFirst
        ? " ORDER BY application_date DESC, application_id DESC"
        : " ORDER BY application_date ASC, application_id ASC");

    List<JobApplication> result = new ArrayList<>();
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql.toString())) {
      for (int i = 0; i < params.size(); i++) {
        ps.setObject(i + 1, params.get(i));
      }
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next())
          result.add(map(rs));
      }
    }
    return result;
  }

  public JobApplication findById(int userId, int applicationId) throws SQLException {
    String sql = "SELECT " + COLUMNS
        + " FROM job_applications WHERE application_id = ? AND user_id = ?";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, applicationId);
      ps.setInt(2, userId);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next() ? map(rs) : null;
      }
    }
  }

  public boolean update(JobApplication a) throws SQLException {
    String sql = "UPDATE job_applications SET company_name = ?, job_role = ?, location = ?, "
        + "job_type = ?, application_date = ?, job_description = ?, job_url = ?, status = ? "
        + "WHERE application_id = ? AND user_id = ?";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, a.getCompanyName());
      ps.setString(2, a.getJobRole());
      ps.setString(3, a.getLocation());
      ps.setString(4, a.getJobType());
      ps.setDate(5, a.getApplicationDate());
      ps.setString(6, a.getJobDescription());
      ps.setString(7, a.getJobUrl());
      ps.setString(8, a.getStatus());
      ps.setInt(9, a.getApplicationId());
      ps.setInt(10, a.getUserId());
      return ps.executeUpdate() > 0;
    }
  }

  public boolean delete(int userId, int applicationId) throws SQLException {
    String sql = "DELETE FROM job_applications WHERE application_id = ? AND user_id = ?";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setInt(1, applicationId);
      ps.setInt(2, userId);
      return ps.executeUpdate() > 0;
    }
  }

  private JobApplication map(ResultSet rs) throws SQLException {
    JobApplication a = new JobApplication();
    a.setApplicationId(rs.getInt("application_id"));
    a.setUserId(rs.getInt("user_id"));
    a.setCompanyName(rs.getString("company_name"));
    a.setJobRole(rs.getString("job_role"));
    a.setLocation(rs.getString("location"));
    a.setJobType(rs.getString("job_type"));
    a.setApplicationDate(rs.getDate("application_date"));
    a.setJobDescription(rs.getString("job_description"));
    a.setJobUrl(rs.getString("job_url"));
    a.setStatus(rs.getString("status"));
    a.setCreatedAt(rs.getTimestamp("created_at"));
    return a;
  }
}