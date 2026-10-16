<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="includes/header.jsp" />
<section class="panel confirmation"><p class="eyebrow">Something went wrong</p><h1>We could not complete that request.</h1><p>Please return to the dashboard and try again. The technical details were logged on the server.</p><a class="button" href="<c:url value='/dashboard'/>">Go to dashboard</a></section>
<jsp:include page="includes/footer.jsp" />
