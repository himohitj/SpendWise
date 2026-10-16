<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="includes/header.jsp" />
<section class="page-heading"><div><p class="eyebrow">Planning</p><h1>Monthly budget</h1></div></section>
<c:if test="${param.msg == 'saved'}"><p class="notice">Budget saved for this month.</p></c:if>
<form class="panel form-card" method="post" action="<c:url value='/budget'/>"><fieldset><legend>Set this month's spending limit</legend><label for="limit">Monthly limit (₹)</label><input id="limit" name="limit" type="text" inputmode="decimal" required value="<c:choose><c:when test='${not empty enteredLimit}'><c:out value='${enteredLimit}'/></c:when><c:otherwise><fmt:formatNumber value='${budget.monthlyLimit}' minFractionDigits='2' maxFractionDigits='2'/></c:otherwise></c:choose>"><c:if test="${not empty budgetError}"><span class="field-error"><c:out value="${budgetError}"/></span></c:if><p class="muted">One general limit for the current calendar month. Saving again updates it.</p><button class="button" type="submit">Save budget</button></fieldset></form>
<jsp:include page="includes/footer.jsp" />
