<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href='<c:url value="/resources/css/bootstrap.min.css" />' rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/font-awesome.css" />' rel="stylesheet" media="screen">
<link href='<c:url value="/resources/css/main.css" />' rel="stylesheet" media="screen">
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

					<form action="editComputer" method="POST">
						<input type="hidden" value="${ computer.id }" id="id"
							name="idComputer" />
						<!-- TODO: Change this value with the computer id -->
						<fieldset>
							<div class="form-group">
								<label for="computerName">Computer name</label>
								<c:choose>
									<c:when test='${ empty requestScope.errors }'>
										<input type="text" autocomplete="false" class="form-control"
											id="computerName" name="computerName"
											placeholder="Ex : PC de Schwarzeneger"
											value="<c:out value='${ computer.name }' />">
									</c:when>
									<c:otherwise>
										<input type="text" autocomplete="false" class="form-control"
											id="computerName" name="computerName"
											placeholder="Ex : PC de Schwarzeneger">
									</c:otherwise>
								</c:choose>
								<span id="erreurName" class="text-danger" style="display: none;">You
									must give a computer name</span>
							</div>
							<div class="form-group">
								<label for="introduced">Introduced date</label> <input
									type="date" class="form-control" id="introduced"
									placeholder="Introduced date" name="introducedDate"
									value="<c:out value='${ computer.introduced }' />"> <span
									class="erreur text-danger">${ errors.introducedDate }</span> <span
									id="erreurIntroduced" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<label for="discontinued">Discontinued date</label> <input
									type="date" class="form-control" id="discontinued"
									placeholder="Discontinued date" name="discontinuedDate"
									value="<c:out value='${ computer.discontinued }' />"> <span
									class="erreur text-danger">${ errors.discontinuedDate }</span>
								<span id="erreurDiscontinued" class="text-danger"
									style="display: none;">You must give a discontinued date
									which is later than introduced's</span> <span
									id="erreurDiscontinuedFormat" class="text-danger"
									style="display: none;">You must give a date after
									January 1st 1970 and before January 27th 2038</span>
							</div>
							<div class="form-group">
								<label for="companyId">Company</label> <select
									class="form-control" name="company" id="companyId">
									<option value="0">--</option>
									<c:forEach var="comp" items="${ requestScope.companies }">
										<option value="${ comp.id }"
											<c:if test="${ comp.id == computer.company.id }">selected</c:if>>${ comp.name }</option>
									</c:forEach>
								</select>
							</div>
						</fieldset>
						<div class="actions pull-right">
							<input type="submit" id="submit" value="Edit"
								class="btn btn-primary"> or <a
								href="<c:url value='/index' />" class="btn btn-default">Cancel</a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</section>
    
    <script src='<c:url value="/resources/js/jquery.min.js" />'></script>
    <script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
    <script src='<c:url value="/resources/js/verifFields.js" />'></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>

</body>

</html>


