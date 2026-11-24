<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <%@ include file="/WEB-INF/layout/header.jspf" %>

      <h3 class="fw-bold mb-1">Dashboard</h3>
      <p class="text-muted">Hello,
        <c:out value="${sessionScope.userName}" />. Here is your application summary.
      </p>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">
          <c:out value="${error}" />
        </div>
      </c:if>

      <div class="row g-3 mb-4">
        <div class="col-6 col-lg">
          <div class="card border-0 shadow-sm h-100">
            <div class="card-body">
              <div class="text-muted small">Total Applications</div>
              <div class="fs-2 fw-bold">${total}</div>
            </div>
          </div>
        </div>
        <div class="col-6 col-lg">
          <div class="card border-0 shadow-sm h-100">
            <div class="card-body">
              <div class="text-muted small">Applied Jobs</div>
              <div class="fs-2 fw-bold text-primary">${counts['Applied']}</div>
            </div>
          </div>
        </div>
        <div class="col-6 col-lg">
          <div class="card border-0 shadow-sm h-100">
            <div class="card-body">
              <div class="text-muted small">Interviews Scheduled</div>
              <div class="fs-2 fw-bold text-warning">${counts['Interview']}</div>
            </div>
          </div>
        </div>
        <div class="col-6 col-lg">
          <div class="card border-0 shadow-sm h-100">
            <div class="card-body">
              <div class="text-muted small">Selected Jobs</div>
              <div class="fs-2 fw-bold text-success">${counts['Selected']}</div>
            </div>
          </div>
        </div>
        <div class="col-6 col-lg">
          <div class="card border-0 shadow-sm h-100">
            <div class="card-body">
              <div class="text-muted small">Rejected Applications</div>
              <div class="fs-2 fw-bold text-danger">${counts['Rejected']}</div>
            </div>
          </div>
        </div>
      </div>

      <div class="card border-0 shadow-sm">
        <div class="card-body">
          <h5 class="fw-semibold mb-3">Applications by status</h5>
          <c:choose>
            <c:when test="${total == 0}">
              <p class="text-muted mb-0">No applications yet.
                <a href="${pageContext.request.contextPath}/app/application-form">Add your first one</a>.
              </p>
            </c:when>
            <c:otherwise>
              <div style="max-width: 420px; margin: 0 auto;">
                <canvas id="statusChart"></canvas>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </div>

      <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.3/dist/chart.umd.min.js"></script>
      <script>
        var canvas = document.getElementById('statusChart');
        if (canvas) {
          var labels = [<c:forEach var="e" items="${counts}" varStatus="st">'${e.key}'<c:if test="${!st.last}">,</c:if></c:forEach>];
          var values = [<c:forEach var="e" items="${counts}" varStatus="st">${e.value}<c:if test="${!st.last}">,</c:if></c:forEach>];
          new Chart(canvas, {
            type: 'doughnut',
            data: {
              labels: labels,
              datasets: [{
                data: values,
                backgroundColor: ['#0d6efd', '#0dcaf0', '#ffc107', '#198754', '#dc3545', '#212529']
              }]
            },
            options: { plugins: { legend: { position: 'bottom' } } }
          });
        }
      </script>

      <%@ include file="/WEB-INF/layout/footer.jspf" %>