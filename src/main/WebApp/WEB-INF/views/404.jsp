<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />' rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/font-awesome.css" />' rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/main.css" />' rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="<c:url value="/index" />">
				Application - Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
				Error 404: Page not found 
				<c:if test="${ error != null || error != '' }">
					: ${ error }
				</c:if>
			</div>
		</div>
	</section>

	<script src='<c:url value="/resources/js/jquery.min.js" />'></script>
    <script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
    <script src='<c:url value="/resources/js/dashboard.js" />'></script>

</body>
</html>