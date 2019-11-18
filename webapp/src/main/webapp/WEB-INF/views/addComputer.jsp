<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href='<c:url value="/resources/css/bootstrap.min.css" />'
	rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/font-awesome.css" />'
	rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/main.css" />' rel="stylesheet"
	media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="<c:url value="/index" />">
				Application - Computer Database </a>
		</div>
	</header>

	<c:set var="s" value="false" />

	<c:if test="${ requestScope.success == true }">
		<section id="main">
			<div class="container">
				<div class="alert alert-success">
					The computer has been created!
					<c:set var="s" value="false" />
				</div>
			</div>
		</section>
	</c:if>

	<c:if test="${ !empty requestScope.errors }">
		<section id="main">
			<div class="container">
				<div class="alert alert-danger">
					Try again! ${ errors }
					<c:set var="s" value="true" />
				</div>
			</div>
		</section>
	</c:if>


	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<h1>Add Computer</h1>
					<form:form action="addComputer" method="POST"
						modelAttribute="computer">
						<fieldset>
							<div class="form-group">
								<form:label path="name" for="computerName">Computer name (*)</form:label>
								<form:input path="name" type="text" autocomplete="false"
									class="form-control" id="computerName" name="computerName"
									placeholder="Ex : PC de Schwarzeneger" />
								<span id="erreurName" class="text-danger">You must give a
									computer name</span>
							</div>
							<div class="form-group">
								<form:label path="introduced" for="introduced">Introduced date</form:label>
								<form:input path="introduced" type="date" class="form-control"
									id="introduced" name="introduced" placeholder="Ex : 2001-11-23" />
								<span class="erreur text-danger">${ errors.introduced }</span> <span
									id="erreurIntroduced" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<form:label path="discontinued" for="discontinued">Discontinued date</form:label>
								<form:input path="discontinued" type="date" class="form-control"
									id="discontinued" name="discontinued"
									placeholder="Doit être posterieure à celle d'au dessus" />
								<span class="erreur text-danger">${ errors.discontinued }</span>
								<span id="erreurDiscontinued" class="text-danger"
									style="display: none;">You must give a discontinued date
									which is later than introduced's</span> <span
									id="erreurDiscontinuedFormat" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<form:label path="companyId" for="companyId">Company</form:label>
								<form:select path="companyId" class="form-control"
									id="companyId" name="companyId">
									<form:option value="0">--</form:option>
									<form:options items="${ companies }" />
								</form:select>
							</div>
						</fieldset>
						<span style="text-weight: bold;">Field marked with (*) is
							mandatory !</span>
						<div class="actions pull-right">
							<input type="submit" id="submit" disabled value="Add"
								class="btn btn-primary"> or <a
								href="<c:url value='/index' />" class="btn btn-default">Cancel</a>
						</div>
					</form:form>
				</div>
			</div>
		</div>
	</section>

	<script src='<c:url value="/resources/js/jquery.min.js" />'></script>
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script src='<c:url value="/resources/js/verifFields.js" />'></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

</body>
</html>