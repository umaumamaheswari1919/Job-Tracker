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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/app/application-form")
public class ApplicationFormServlet extends HttpServlet {

  private final ApplicationDAO dao = new ApplicationDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    int userId = (int) req.getSession().getAttribute("userId");
    JobApplication app = new JobApplication();
    app.setStatus("Applied");
    app.setApplicationDate(Date.valueOf(LocalDate.now()));

    String idParam = req.getParameter("id");
    if (idParam != null) {
      try {
        JobApplication found = dao.findById(userId, Integer.parseInt(idParam));
        if (found == null) {
          req.getSession().setAttribute("flash", "Application not found.");
          resp.sendRedirect(req.getContextPath() + "/app/applications");
          return;
        }
        app = found;
      } catch (NumberFormatException | SQLException e) {
        e.printStackTrace();
        resp.sendRedirect(req.getContextPath() + "/app/applications");
        return;
      }
    }
    showForm(req, resp, app, null);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    HttpSession session = req.getSession();
    int userId = (int) session.getAttribute("userId");

    JobApplication app = new JobApplication();
    app.setUserId(userId);
    app.setCompanyName(value(req, "companyName"));
    app.setJobRole(value(req, "jobRole"));
    app.setLocation(value(req, "location"));
    app.setJobType(value(req, "jobType"));
    app.setJobDescription(value(req, "jobDescription"));
    app.setJobUrl(value(req, "jobUrl"));
    app.setStatus(value(req, "status"));

    String idParam = req.getParameter("id");
    boolean editing = idParam != null && !idParam.isBlank();
    if (editing) {
      try {
        app.setApplicationId(Integer.parseInt(idParam));
      } catch (NumberFormatException e) {
        resp.sendRedirect(req.getContextPath() + "/app/applications");
        return;
      }
    }

    String error = null;
    try {
      app.setApplicationDate(Date.valueOf(LocalDate.parse(value(req, "applicationDate"))));
    } catch (DateTimeParseException e) {
      error = "Please enter a valid application date.";
    }
    if (error == null)
      error = validate(app);

    if (error == null) {
      try {
        boolean ok = editing ? dao.update(app) : dao.add(app);
        session.setAttribute("flash", !ok ? "Application not found."
            : editing ? "Application updated." : "Application added.");
        resp.sendRedirect(req.getContextPath() + "/app/applications");
        return;
      } catch (SQLException e) {
        e.printStackTrace();
        error = "Something went wrong. Please try again.";
      }
    }
    showForm(req, resp, app, error);
  }

  private String validate(JobApplication a) {
    if (a.getCompanyName().isEmpty() || a.getCompanyName().length() > 150)
      return "Company name is required (max 150 characters).";
    if (a.getJobRole().isEmpty() || a.getJobRole().length() > 150)
      return "Job role is required (max 150 characters).";
    if (a.getLocation().length() > 150)
      return "Location is too long (max 150 characters).";
    if (!Constants.JOB_TYPES.contains(a.getJobType()))
      return "Please choose a valid job type.";
    if (!Constants.STATUSES.contains(a.getStatus()))
      return "Please choose a valid status.";
    if (a.getJobUrl().length() > 500)
      return "Job URL is too long (max 500 characters).";
    if (!a.getJobUrl().isEmpty()
        && !a.getJobUrl().matches("(?i)^https?://\\S+$"))
      return "Job URL must start with http:// or https://";
    if (a.getJobDescription().length() > 5000)
      return "Description is too long (max 5000 characters).";
    return null;
  }

  private void showForm(HttpServletRequest req, HttpServletResponse resp,
      JobApplication app, String error) throws ServletException, IOException {
    req.setAttribute("app", app);
    req.setAttribute("error", error);
    req.setAttribute("statuses", Constants.STATUSES);
    req.setAttribute("jobTypes", Constants.JOB_TYPES);
    boolean editing = app.getApplicationId() > 0;
    req.setAttribute("editing", editing);
    req.setAttribute("pageTitle", editing ? "Edit Application" : "Add Application");
    req.setAttribute("activePage", editing ? "applications" : "add");
    req.getRequestDispatcher("/WEB-INF/views/application-form.jsp").forward(req, resp);
  }

  private String value(HttpServletRequest req, String name) {
    String v = req.getParameter(name);
    return v == null ? "" : v.trim();
  }
}