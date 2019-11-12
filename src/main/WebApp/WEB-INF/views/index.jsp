<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page isELIgnored="false"%>

<spring:message code="index.applicationName" var="applicationName"/>
<spring:message code="index.successEditing" var="successEditing"/>
<spring:message code="index.successDeletingSolo" var="successDeletingSolo"/>
<spring:message code="index.successDeleting" var="successDeleting"/>
<spring:message code="index.computersFound" var="computersFound"/>
<spring:message code="index.searchPlaceholder" var="searchPlaceholder"/>
<spring:message code="index.filterValue" var="filterValue"/>
<spring:message code="index.addComputer" var="addComputer"/>
<spring:message code="index.delete" var="delete"/>
<spring:message code="index.compName" var="compName"/>
<spring:message code="index.intDate" var="intDate"/>
<spring:message code="index.disDate" var="disDate"/>
<spring:message code="index.companyField" var="companyField"/>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
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
			<a class="navbar-brand" href="<c:url value="/index?numPage=1" />">${ applicationName }</a>
		</div>
	</header>

	<c:if test='${ param.success == "true" }'>
		<section id="main">
			<div class="container">
				<div class="alert alert-success">
					${ successEditing } />
				</div>
			</div>
		</section>
	</c:if>
	<c:if test='${ param.successDelete == "true" }'>
		<section id="main">
			<div class="container">
				<div class="alert alert-success">
					<c:choose>
						<c:when test='${ param.length <= 1 }'>
                            ${ param.length } ${ successDeletingSolo }
						</c:when>
						<c:otherwise>
                            ${ param.length } ${ successDeleting }
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</section>
	</c:if>

	<c:if test='${ !empty errors }'>
		<section id="main">
			<div class="container">
				<div class="alert alert-danger">${ errors.errorDeleting }</div>
			</div>
		</section>
	</c:if>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">${ nbComputers } ${ computersFound }</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="#" method="GET" class="form-inline">
						<c:choose>
							<c:when test='${ param.search != null && param.search != "" }'>
								<input type="search" id="searchbox" name="search"
									class="form-control"
									value="<c:out value='${ param.search }' />" />
							</c:when>
							<c:otherwise>
								<input type="search" id="searchbox" name="search"
									class="form-control" placeholder="${ searchPlaceholder }" />
							</c:otherwise>
						</c:choose>
						<input type="submit" id="searchsubmit" value="${ filterValue }"
							class="btn btn-primary" />
					</form>
				</div>
				<div class="pull-right">
					<a class="btn btn-success" id="addComputer"
						href="<c:url value='/addComputer' />">${ addComputer }</a> <a
						class="btn btn-default" id="editComputer" href="#"
						onclick="$.fn.toggleEditMode();">${ delete }</a>
				</div>
			</div>
		</div>

		<form id="deleteForm" action="" method="POST">
			<input type="hidden" name="selection" value="">
		</form>

		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->
						<th class="editMode"
							style="width: 60px; height: 22px; display: none;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th>${ compName }</th>
						<th>${ intDate }</th>
						<th>${ disDate }</th>
						<th>${ companyField }</th>
					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="computer" items="${ requestScope.computers }">
						<tr>
							<td class="editMode" style="display: none;"><input
								type="checkbox" name="cb" class="cb" value='${ computer.id }'></td>
							<td><a
								href="<c:url value='/editComputer'><c:param name="idComputer" value="${ computer.id }" /></c:url>"
								onclick=""><c:out value="${ computer.name }" /></a></td>
							<td><c:out value="${ computer.introduced }" /></td>
							<td><c:out value="${ computer.discontinued }" /></td>
							<td><c:out value="${ computer.company.name }" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<li><a
					href="<c:url value='/index'>
                                <c:param name="numPage" value='${ requestScope.numPages - 1 > 1 ? requestScope.numPages - 1 : 1 }'/>
                                <c:if test="${ param.search != null && param.search != '' }">
                                    <c:param name="search" value="${ param.search }" />
                                </c:if>
                            </c:url>"
					aria-label="Previous"> <span aria-hidden="true">&laquo;</span>
				</a></li>
				<c:forEach var="number" items="${ requestScope.navFooter }">
					<li><a
						href="   <c:url value="/index">
                                            <c:if test="${ number != '...'}">
                                                <c:param name="numPage" value="${ number }"/>
                                            </c:if> 
                                            <c:if test="${ param.search != null && param.search != '' }">
                                                <c:param name="search" value="${ param.search }" />
                                            </c:if>
                                        </c:url>">${ number }
					</a></li>
				</c:forEach>
				<li><a
					href="<c:url value='/index'>
                              <c:param name="numPage" value='${ requestScope.numPages + 1 < requestScope.nbPages  ? requestScope.numPages + 1 : requestScope.nbPages }'/>
                              <c:if test="${ param.search != null && param.search != '' }">
                                <c:param name="search" value="${ param.search }" />
                              </c:if>
                         </c:url>"
					aria-label="Next"> <span aria-hidden="true">&raquo;</span>
				</a></li>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<a
					href="   <c:url value="/index">
                            <c:param name="offset" value="10"/>
                        </c:url>">
					<button type="button" class="btn btn-default">10</button>
				</a> <a
					href="   <c:url value="/index">
                            <c:param name="offset" value="50"/>
                        </c:url>">
					<button type="button" class="btn btn-default">50</button>
				</a> <a
					href="   <c:url value="/index">
                            <c:param name="offset" value="100"/>
                        </c:url>">
					<button type="button" class="btn btn-default">100</button>
				</a>
			</div>
		</div>

	</footer>

	<script src='<c:url value="/resources/js/jquery.min.js" />'></script>
	<script src='<c:url value="/resources/js/bootstrap.min.js" />'></script>
	<script src='<c:url value="/resources/js/dashboard.js" />'></script>

</body>
</html>