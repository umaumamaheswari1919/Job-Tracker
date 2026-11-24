<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>Register - Job Tracker</title>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body class="bg-light">
      <div class="container">
        <div class="row justify-content-center align-items-center min-vh-100">
          <div class="col-12 col-md-8 col-lg-5">
            <div class="card shadow-sm border-0">
              <div class="card-body p-4 p-md-5">
                <h3 class="fw-bold mb-1">Create account</h3>
                <p class="text-muted mb-4">Track every job application in one place.</p>

                <c:if test="${not empty error}">
                  <div class="alert alert-danger">
                    <c:out value="${error}" />
                  </div>
                </c:if>

                <form id="regForm" method="post" action="${pageContext.request.contextPath}/register" novalidate>
                  <div class="mb-3">
                    <label class="form-label">Full Name</label>
                    <input type="text" name="fullName" id="fullName" class="form-control"
                      value="<c:out value='${fullName}'/>" maxlength="100" required>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" id="email" class="form-control" value="<c:out value='${email}'/>"
                      maxlength="150" required>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" minlength="8"
                      maxlength="72" required>
                    <div class="form-text">At least 8 characters, with a letter and a number.</div>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">Confirm Password</label>
                    <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" required>
                  </div>
                  <div id="clientError" class="alert alert-danger d-none"></div>
                  <button type="submit" class="btn btn-primary w-100">Register</button>
                </form>

                <p class="text-center mt-3 mb-0">
                  Already have an account?
                  <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <script>
        document.getElementById('regForm').addEventListener('submit', function (e) {
          var name = document.getElementById('fullName').value.trim();
          var email = document.getElementById('email').value.trim();
          var pwd = document.getElementById('password').value;
          var confirm = document.getElementById('confirmPassword').value;
          var box = document.getElementById('clientError');
          var msg = '';

          if (name.length < 2) msg = 'Full name must be at least 2 characters.';
          else if (!/^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/.test(email)) msg = 'Please enter a valid email.';
          else if (pwd.length < 8 || !/[A-Za-z]/.test(pwd) || !/\d/.test(pwd))
            msg = 'Password needs 8+ characters with a letter and a number.';
          else if (pwd !== confirm) msg = 'Passwords do not match.';

          if (msg) {
            e.preventDefault();
            box.textContent = msg;
            box.classList.remove('d-none');
          }
        });
      </script>
    </body>

    </html>