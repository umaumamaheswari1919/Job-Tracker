<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <title>Login - Job Tracker</title>
      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body class="bg-light">
      <div class="container">
        <div class="row justify-content-center align-items-center min-vh-100">
          <div class="col-12 col-md-8 col-lg-5">
            <div class="card shadow-sm border-0">
              <div class="card-body p-4 p-md-5">
                <h3 class="fw-bold mb-1">Welcome back</h3>
                <p class="text-muted mb-4">Login to manage your applications.</p>

                <c:if test="${param.registered == '1'}">
                  <div class="alert alert-success">Registration successful. Please login.</div>
                </c:if>
                <c:if test="${param.loggedout == '1'}">
                  <div class="alert alert-info">You have been logged out.</div>
                </c:if>
                <c:if test="${not empty error}">
                  <div class="alert alert-danger">
                    <c:out value="${error}" />
                  </div>
                </c:if>

                <form id="loginForm" method="post" action="${pageContext.request.contextPath}/login" novalidate>
                  <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" name="email" id="email" class="form-control" value="<c:out value='${email}'/>"
                      required>
                  </div>
                  <div class="mb-3">
                    <label class="form-label">Password</label>
                    <input type="password" name="password" id="password" class="form-control" required>
                  </div>
                  <div id="clientError" class="alert alert-danger d-none"></div>
                  <button type="submit" class="btn btn-primary w-100">Login</button>
                </form>

                <p class="text-center mt-3 mb-0">
                  New here?
                  <a href="${pageContext.request.contextPath}/register">Create an account</a>
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <script>
        document.getElementById('loginForm').addEventListener('submit', function (e) {
          var email = document.getElementById('email').value.trim();
          var pwd = document.getElementById('password').value;
          var box = document.getElementById('clientError');
          if (!email || !pwd) {
            e.preventDefault();
            box.textContent = 'Please enter email and password.';
            box.classList.remove('d-none');
          }
        });
      </script>
    </body>

    </html>