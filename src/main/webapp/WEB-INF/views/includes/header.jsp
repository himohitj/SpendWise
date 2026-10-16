<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SpendWise</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
</head>
<body>
<header class="site-header">
    <a class="brand" href="<c:url value='/dashboard'/>">SpendWise</a>
    <nav aria-label="Main navigation">
        <a href="<c:url value='/dashboard'/>">Dashboard</a>
        <a href="<c:url value='/transactions'/>">Transactions</a>
        <a href="<c:url value='/budget'/>">Budget</a>
    </nav>
</header>
<main class="page">
