<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <%@ include file="/WEB-INF/layout/header.jspf" %>

      <div class="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
        <h3 class="fw-bold mb-0">Applications</h3>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/app/application-form">+ Add Application</a>
      </div>

      <c:if test="${not empty sessionScope.flash}">
        <div class="alert alert-success alert-dismissible fade show">
          <c:out value="${sessionScope.flash}" />
          <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="flash" scope="session" />
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert alert-danger">
          <c:out value="${error}" />
        </div>
      </c:if>

      <form method="get" action="${pageContext.request.contextPath}/app/applications"
        class="card card-body border-0 shadow-sm mb-3">
        <div class="row g-2">
          <div class="col-12 col-md-5">
            <input type="text" name="search" class="form-control" placeholder="Search by company"
              value="<c:out value='${search}'/>">
          </div>
          <div class="col-6 col-md-3">
            <select name="status" class="form-select">
              <option value="">All statuses</option>
              <c:forEach var="s" items="${statuses}">
                <option value="${s}" ${s==selectedStatus ? 'selected' : '' }>${s}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-6 col-md-2">
            <select name="sort" class="form-select">
              <option value="desc" ${sort=='desc' ? 'selected' : '' }>Newest first</option>
              <option value="asc" ${sort=='asc' ? 'selected' : '' }>Oldest first</option>
            </select>
          </div>
          <div class="col-12 col-md-2 d-flex gap-2">
            <button type="submit" class="btn btn-dark flex-fill">Apply</button>
            <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/app/applications">Clear</a>
          </div>
        </div>
      </form>

      <c:choose>
        <c:when test="${empty apps}">
          <div class="card card-body border-0 shadow-sm text-center text-muted py-5">
            No applications found.
          </div>
        </c:when>
        <c:otherwise>
          <div class="card border-0 shadow-sm">
            <div class="table-responsive">
              <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                  <tr>
                    <th>Company</th>
                    <th>Role</th>
                    <th>Location</th>
                    <th>Type</th>
                    <th>Applied on</th>
                    <th>Status</th>
                    <th class="text-end">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="a" items="${apps}">
                    <tr>
                      <td class="fw-semibold">
                        <c:out value="${a.companyName}" />
                        <c:if test="${not empty a.jobUrl}">
                          <a href="<c:out value='${a.jobUrl}'/>" target="_blank" rel="noopener noreferrer"
                            class="small ms-1">link</a>
                        </c:if>
                      </td>
                      <td>
                        <c:out value="${a.jobRole}" />
                      </td>
                      <td>
                        <c:out value="${a.location}" />
                      </td>
                      <td>
                        <c:out value="${a.jobType}" />
                      </td>
                      <td>${a.applicationDate}</td>
                      <td><span class="badge ${badges[a.status]}">
                          <c:out value="${a.status}" />
                        </span></td>
                      <td class="text-end text-nowrap">
                        <a class="btn btn-sm btn-outline-primary"
                          href="${pageContext.request.contextPath}/app/application-form?id=${a.applicationId}">Edit</a>
                        <form method="post" action="${pageContext.request.contextPath}/app/applications"
                          class="d-inline" onsubmit="return confirm('Delete this application?');">
                          <input type="hidden" name="action" value="delete">
                          <input type="hidden" name="id" value="${a.applicationId}">
                          <button type="submit" class="btn btn-sm btn-outline-danger">Delete</button>
                        </form>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
          <p class="text-muted small mt-2">${apps.size()} application(s)</p>
        </c:otherwise>
      </c:choose>

      <%@ include file="/WEB-INF/layout/footer.jspf" %>