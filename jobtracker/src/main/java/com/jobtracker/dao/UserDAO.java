package com.jobtracker.dao;

import com.jobtracker.model.User;
import com.jobtracker.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

  public boolean emailExists(String email) throws SQLException {
    String sql = "SELECT 1 FROM users WHERE email = ?";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    }
  }

  public boolean register(User user) throws SQLException {
    String sql = "INSERT INTO users (full_name, email, password) VALUES (?, ?, ?)";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, user.getFullName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPassword());
      return ps.executeUpdate() > 0;
    }
  }

  public User findByEmail(String email) throws SQLException {
    String sql = "SELECT user_id, full_name, email, password, created_at FROM users WHERE email = ?";
    try (Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          User u = new User();
          u.setUserId(rs.getInt("user_id"));
          u.setFullName(rs.getString("full_name"));
          u.setEmail(rs.getString("email"));
          u.setPassword(rs.getString("password"));
          u.setCreatedAt(rs.getTimestamp("created_at"));
          return u;
        }
        return null;
      }
    }
  }
}