<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="includes/header.jsp" />
<section class="page-heading"><div><p class="eyebrow">Overview</p><h1>Your money at a glance</h1></div><a class="button" href="<c:url value='/transactions/add'/>">+ Add transaction</a></section>
<section class="cards" aria-label="Financial summary">
    <article class="card income"><p>Total income</p><strong>₹<fmt:formatNumber value="${dashboard.totalIncome}" minFractionDigits="2" maxFractionDigits="2"/></strong><span>All time</span></article>
    <article class="card expense"><p>Total expenses</p><strong>₹<fmt:formatNumber value="${dashboard.totalExpenses}" minFractionDigits="2" maxFractionDigits="2"/></strong><span>All time</span></article>
    <article class="card"><p>Balance</p><strong>₹<fmt:formatNumber value="${dashboard.balance}" minFractionDigits="2" maxFractionDigits="2"/></strong><span>Income − expenses</span></article>
    <article class="card"><p>Budget status</p><strong><c:out value="${dashboard.budgetStatus}"/></strong><span>This month</span></article>
</section>
<section class="two-columns">
    <article class="panel"><h2>This month</h2><dl class="summary-list"><div><dt>Income</dt><dd>+ ₹<fmt:formatNumber value="${dashboard.monthIncome}" minFractionDigits="2" maxFractionDigits="2"/></dd></div><div><dt>Expenses</dt><dd>− ₹<fmt:formatNumber value="${dashboard.monthExpenses}" minFractionDigits="2" maxFractionDigits="2"/></dd></div></dl></article>
    <article class="panel"><h2>Monthly budget</h2><c:choose><c:when test="${dashboard.hasBudget()}"><p><strong><c:out value="${dashboard.budgetStatus}"/></strong> · ₹<fmt:formatNumber value="${dashboard.remainingBudget}" minFractionDigits="2" maxFractionDigits="2"/> remaining</p><progress value="${dashboard.budgetPercent}" max="100"><c:out value="${dashboard.budgetPercent}"/>%</progress><p class="muted">Limit: ₹<fmt:formatNumber value="${dashboard.budgetLimit}" minFractionDigits="2" maxFractionDigits="2"/></p></c:when><c:otherwise><p>No budget set for this month.</p><a href="<c:url value='/budget'/>">Set a budget</a></c:otherwise></c:choose></article>
</section>
<section class="panel"><h2>Spending by category this month</h2><c:choose><c:when test="${empty dashboard.categorySpending}"><p>No transactions yet. <a href="<c:url value='/transactions/add'/>">Add your first transaction.</a></p></c:when><c:otherwise><table><thead><tr><th>Category</th><th>Spent</th></tr></thead><tbody><c:forEach items="${dashboard.categorySpending}" var="entry"><tr><td><c:out value="${entry.key}"/></td><td>₹<fmt:formatNumber value="${entry.value}" minFractionDigits="2" maxFractionDigits="2"/></td></tr></c:forEach></tbody></table></c:otherwise></c:choose></section>
<jsp:include page="includes/footer.jsp" />
