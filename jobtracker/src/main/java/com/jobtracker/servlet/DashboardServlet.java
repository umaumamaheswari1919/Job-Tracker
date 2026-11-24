package com.jobtracker.servlet;

import com.jobtracker.dao.DashboardDAO;
import com.jobtracker.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/dashboard.jsp")
public class DashboardServlet extends HttpServlet {

  private final DashboardDAO dao = new DashboardDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int userId = (int) req.getSession().getAttribute("userId");

    Map<String, Integer> counts = new LinkedHashMap<>();
    for (String s : Constants.STATUSES)
      counts.put(s, 0);

    try {
      counts = dao.countByStatus(userId);
    } catch (SQLException e) {
      e.printStackTrace();
      req.setAttribute("error", "Could not load dashboard data. Please try again.");
    }

    int total = counts.values().stream().mapToInt(Integer::intValue).sum();

    req.setAttribute("counts", counts);
    req.setAttribute("total", total);
    req.setAttribute("pageTitle", "Dashboard");
    req.setAttribute("activePage", "dashboard");
    req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
  }
}