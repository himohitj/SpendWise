<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="includes/header.jsp" />
<section class="panel confirmation"><p class="eyebrow">Confirm deletion</p><h1>Delete this transaction?</h1><p>This will permanently remove <strong><c:out value="${transaction.category.name}"/></strong>: ₹<fmt:formatNumber value="${transaction.amount}" minFractionDigits="2" maxFractionDigits="2"/> on <c:out value="${transaction.transactionDate}"/>.</p><form method="post" action="<c:url value='/transactions/delete'/>"><input type="hidden" name="id" value="<c:out value='${transaction.id}'/>"><button class="button danger" type="submit">Delete transaction</button> <a href="<c:url value='/transactions'/>">Cancel</a></form></section>
<jsp:include page="includes/footer.jsp" />
