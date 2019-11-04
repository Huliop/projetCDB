<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
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
					Try again!
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
					<form action="addComputer" method="POST">
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name (*)</label>
								<c:choose>
									<c:when test='${ s != "false" }'>
										<input type="text" autocomplete="false" class="form-control"
											id="computerName" name="computerName"
											placeholder="Ex : PC de Schwarzeneger"
											value="<c:out value='${ param.computerName }' />">
									</c:when>
									<c:otherwise>
										<input type="text" autocomplete="false" class="form-control"
											id="computerName" name="computerName"
											placeholder="Ex : PC de Schwarzeneger">
									</c:otherwise>
								</c:choose>
								<span id="erreurName" class="text-danger">You must give a
									computer name</span>
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced"
									name="introducedDate"
									value="<c:out value="${ param.introducedDate }" />"
									placeholder="Ex : 2001-11-23"> <span
									class="erreur text-danger">${ errors.introducedDate }</span> <span
									id="erreurIntroduced" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued"
									name="discontinuedDate"
									value="<c:out value="${ param.discontinuedDate }" />"
									placeholder="Doit être posterieure à celle d'au dessus">
								<span class="erreur text-danger">${ errors.discontinuedDate }</span>
								<span id="erreurDiscontinued" class="text-danger"
									style="display: none;">You must give a discontinued date
									which is later than introduced's</span> <span
									id="erreurDiscontinuedFormat" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" id="companyId" name="company">
									<option value="0" selected>--</option>
									<c:forEach var="comp" items="${ requestScope.companies }">
										<option value="${ comp.id }"
											<c:if test="${ comp.id == param.company }">selected</c:if>>${ comp.name }</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<span style="text-weight: bold;">Field marked with (*) is
							mandatory !</span>
						<div class="actions pull-right">
							<input type="submit" id="submit" disabled value="Add"
								class="btn btn-primary"> or <a
								href="<c:url value='/index' />" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
	<script src="js/verifFields.js"></script>

</body>
</html>