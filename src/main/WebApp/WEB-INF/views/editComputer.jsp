<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
			<a class="navbar-brand" href="<c:url value='/index' />">
				Application - Computer Database </a>
		</div>
	</header>

	<c:if test="${ !empty requestScope.errors }">
		<section id="main">
			<div class="container">
				<div class="alert alert-danger">Try again!</div>
			</div>
		</section>
	</c:if>

	<section id="main">
		<div class="container">
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2 box">
					<div class="label label-default pull-right">id: ${ param.idComputer }
					</div>
					<h1>Edit Computer</h1>

					<form:form action="editComputer" method="POST">
					<form:input path="id" type="hidden" value="${ computer.id }"
						id="id" name="idComputer" />
					<!-- TODO: Change this value with the computer id -->
					<fieldset>
						<div class="form-group">
							<form:label path="name" for="computerName">Computer name</form:label>
							<c:choose>
								<c:when test='${ empty requestScope.errors }'>
									<form:input path="name" type="text" autocomplete="false"
										class="form-control" id="computerName" name="computerName"
										placeholder="Ex : PC de Schwarzeneger"
										value="<c:out value='${ computer.name }' />" />
								</c:when>
								<c:otherwise>
									<form:input path="name" type="text" autocomplete="false"
										class="form-control" id="computerName" name="computerName"
										placeholder="Ex : PC de Schwarzeneger" />
								</c:otherwise>
							</c:choose>
							<span id="erreurName" class="text-danger" style="display: none;">You
								must give a computer name</span>
						</div>
						<div class="form-group">
							<form:label path="introduced" for="introduced">Introduced date</form:label>
							<form:input path="introduced" type="date" class="form-control"
								id="introduced" placeholder="Introduced date" name="introduced"
								value="<c:out value='${ computer.introduced }' />" />
							<span class="erreur text-danger">${ errors.introduced }</span> <span
								id="erreurIntroduced" class="text-danger" style="display: none;">You
								must give a date after January 1st 1970 and before January 27th
								2038</span>
						</div>
						<div class="form-group">
							<form:label path="discontinued" for="discontinued">Discontinued date</form:label>
							<form:input path="discontinued" type="date" class="form-control"
								id="discontinued" placeholder="Discontinued date"
								name="discontinued"
								value="<c:out value='${ computer.discontinued }' />" />
							<span class="erreur text-danger">${ errors.discontinued }</span>
							<span id="erreurDiscontinued" class="text-danger"
								style="display: none;">You must give a discontinued date
								which is later than introduced's</span> <span
								id="erreurDiscontinuedFormat" class="text-danger"
								style="display: none;">You must give a date after January
								1st 1970 and before January 27th 2038</span>
						</div>
						<div class="form-group">
							<form:label path="companyId" for="companyId">Company</form:label>
							<form:select path="companyId" class="form-control" name="company"
								id="companyId">
								<option value="0">--</option>
								<c:forEach var="comp" items="${ requestScope.companies }">
									<form:option value="${ comp.id }"
										<c:if test="${ comp.id == computer.company.id }">selected</c:if>>${ comp.name }</form:option>
								</c:forEach>
							</form:select>
						</div>
					</fieldset>
					<div class="actions pull-right">
						<input type="submit" id="submit" value="Edit"
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


