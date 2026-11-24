<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%@ taglib prefix="c" uri="jakarta.tags.core" %>
    <%@ include file="/WEB-INF/layout/header.jspf" %>

      <h3 class="fw-bold mb-3">
        <c:out value="${pageTitle}" />
      </h3>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">
          <c:out value="${error}" />
        </div>
      </c:if>

      <div class="card border-0 shadow-sm">
        <div class="card-body p-4">
          <form method="post" action="${pageContext.request.contextPath}/app/application-form">
            <c:if test="${editing}">
              <input type="hidden" name="id" value="${app.applicationId}">
            </c:if>

            <div class="row g-3">
              <div class="col-12 col-md-6">
                <label class="form-label">Company Name *</label>
                <input type="text" name="companyName" class="form-control" maxlength="150" required
                  value="<c:out value='${app.companyName}'/>">
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label">Job Role *</label>
                <input type="text" name="jobRole" class="form-control" maxlength="150" required
                  value="<c:out value='${app.jobRole}'/>">
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label">Job Location</label>
                <input type="text" name="location" class="form-control" maxlength="150"
                  value="<c:out value='${app.location}'/>">
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label">Job Type *</label>
                <select name="jobType" class="form-select" required>
                  <c:forEach var="t" items="${jobTypes}">
                    <option value="${t}" ${t==app.jobType ? 'selected' : '' }>${t}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label">Application Date *</label>
                <input type="date" name="applicationDate" class="form-control" required value="${app.applicationDate}">
              </div>
              <div class="col-12 col-md-6">
                <label class="form-label">Application Status *</label>
                <select name="status" class="form-select" required>
                  <c:forEach var="s" items="${statuses}">
                    <option value="${s}" ${s==app.status ? 'selected' : '' }>${s}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="col-12">
                <label class="form-label">Job Posting URL</label>
                <input type="url" name="jobUrl" class="form-control" maxlength="500" pattern="https?://.+"
                  placeholder="https://..." value="<c:out value='${app.jobUrl}'/>">
              </div>
              <div class="col-12">
                <label class="form-label">Job Description</label>
                <textarea name="jobDescription" class="form-control" rows="5"
                  maxlength="5000"><c:out value="${app.jobDescription}"/></textarea>
              </div>
            </div>

            <div class="mt-4 d-flex gap-2">
              <button type="submit" class="btn btn-primary">
                <c:out value="${editing ? 'Update' : 'Save'}" />
              </button>
              <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/app/applications">Cancel</a>
            </div>
          </form>
        </div>
      </div>

      <%@ include file="/WEB-INF/layout/footer.jspf" %>