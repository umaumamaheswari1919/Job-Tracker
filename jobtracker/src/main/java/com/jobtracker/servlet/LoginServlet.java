package com.jobtracker.servlet;

import com.jobtracker.dao.UserDAO;
import com.jobtracker.model.User;
import com.jobtracker.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

  private final UserDAO userDAO = new UserDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session != null && session.getAttribute("userId") != null) {
      resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
      return;
    }
    req.getRequestDispatcher("/login.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    String email = req.getParameter("email") == null ? "" : req.getParameter("email").trim().toLowerCase();
    String password = req.getParameter("password") == null ? "" : req.getParameter("password");

    String error = null;

    if (email.isEmpty() || password.isEmpty()) {
      error = "Please enter email and password.";
    } else {
      try {
        User user = userDAO.findByEmail(email);
        if (user == null || !PasswordUtil.verify(password, user.getPassword())) {
          error = "Invalid email or password.";
        } else {
          HttpSession old = req.getSession(false);
          if (old != null)
            old.invalidate();

          HttpSession session = req.getSession(true);
          session.setAttribute("userId", user.getUserId());
          session.setAttribute("userName", user.getFullName());
          session.setMaxInactiveInterval(30 * 60);

          resp.sendRedirect(req.getContextPath() + "/dashboard.jsp");
          return;
        }
      } catch (SQLException e) {
        e.printStackTrace();
        error = "Something went wrong. Please try again.";
      }
    }

    req.setAttribute("error", error);
    req.setAttribute("email", email);
    req.getRequestDispatcher("/login.jsp").forward(req, resp);
  }
}