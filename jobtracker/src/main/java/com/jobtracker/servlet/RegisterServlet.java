package com.jobtracker.servlet;

import com.jobtracker.dao.UserDAO;
import com.jobtracker.model.User;
import com.jobtracker.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private final UserDAO userDAO = new UserDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.getRequestDispatcher("/register.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");

    String fullName = trim(req.getParameter("fullName"));
    String email = trim(req.getParameter("email")).toLowerCase();
    String password = req.getParameter("password");
    String confirm = req.getParameter("confirmPassword");
    if (password == null)
      password = "";
    if (confirm == null)
      confirm = "";

    String error = validate(fullName, email, password, confirm);

    try {
      if (error == null && userDAO.emailExists(email)) {
        error = "This email is already registered. Please login.";
      }
      if (error == null) {
        String hash = PasswordUtil.hash(password);
        userDAO.register(new User(fullName, email, hash));
        resp.sendRedirect(req.getContextPath() + "/login.jsp?registered=1");
        return;
      }
    } catch (SQLException e) {
      if (e.getErrorCode() == 1062) {
        error = "This email is already registered. Please login.";
      } else {
        e.printStackTrace();
        error = "Something went wrong. Please try again.";
      }
    }

    req.setAttribute("error", error);
    req.setAttribute("fullName", fullName);
    req.setAttribute("email", email);
    req.getRequestDispatcher("/register.jsp").forward(req, resp);
  }

  private String validate(String name, String email, String pwd, String confirm) {
    if (name.length() < 2 || name.length() > 100)
      return "Full name must be 2 to 100 characters.";
    if (email.length() > 150 || !EMAIL_PATTERN.matcher(email).matches())
      return "Please enter a valid email address.";
    if (pwd.length() < 8 || pwd.length() > 72)
      return "Password must be 8 to 72 characters.";
    if (!pwd.matches(".*[A-Za-z].*") || !pwd.matches(".*\\d.*"))
      return "Password must contain at least one letter and one number.";
    if (!pwd.equals(confirm))
      return "Passwords do not match.";
    return null;
  }

  private String trim(String s) {
    return s == null ? "" : s.trim();
  }
}