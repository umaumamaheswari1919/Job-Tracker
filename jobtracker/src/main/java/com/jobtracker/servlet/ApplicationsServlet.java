package com.jobtracker.servlet;

import com.jobtracker.dao.ApplicationDAO;
import com.jobtracker.model.JobApplication;
import com.jobtracker.util.Constants;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/app/applications")
public class ApplicationsServlet extends HttpServlet {

  private final ApplicationDAO dao = new ApplicationDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int userId = (int) req.getSession().getAttribute("userId");

    String search = req.getParameter("search");
    String status = req.getParameter("status");
    String sort = req.getParameter("sort");
    if (status != null && !Constants.STATUSES.contains(status))
      status = null;
    boolean newestFirst = !"asc".equals(sort);

    try {
      List<JobApplication> apps = dao.list(userId, search, status, newestFirst);
      req.setAttribute("apps", apps);
    } catch (SQLException e) {
      e.printStackTrace();
      req.setAttribute("error", "Could not load applications. Please try again.");
    }

    req.setAttribute("search", search == null ? "" : search);
    req.setAttribute("selectedStatus", status == null ? "" : status);
    req.setAttribute("sort", newestFirst ? "desc" : "asc");
    req.setAttribute("statuses", Constants.STATUSES);
    req.setAttribute("badges", Constants.BADGES);
    req.setAttribute("pageTitle", "Applications");
    req.setAttribute("activePage", "applications");
    req.getRequestDispatcher("/WEB-INF/views/applications.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpSession session = req.getSession();
    int userId = (int) session.getAttribute("userId");

    if ("delete".equals(req.getParameter("action"))) {
      try {
        int id = Integer.parseInt(req.getParameter("id"));
        boolean deleted = dao.delete(userId, id);
        session.setAttribute("flash", deleted
            ? "Application deleted."
            : "Application not found.");
      } catch (NumberFormatException | SQLException e) {
        e.printStackTrace();
        session.setAttribute("flash", "Could not delete the application.");
      }
    }
    resp.sendRedirect(req.getContextPath() + "/app/applications");
  }
}