<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            <a class="navbar-brand" href="<c:url value="/index" />"> Application - Computer Database </a>
        </div>
    </header>
    
    
    <section id="main">
        <c:if test="${ requestScope.success == true }">
        <div class="container">
            <div class="alert alert-success">
                The computer has been created!
            </div>
        </div>
        </c:if>
    </section>
    
    <c:if test="${ !empty requestScope.errors }">
        <section id="main">
            <div class="container">
                <div class="alert alert-danger">
                    Try again!
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
                                <input type="text" class="form-control" id="computerName" name="computerName" value="<c:out value="${ param.computerName }" />" placeholder="Ex : PC de Schwarzeneger">
                                <span class="erreur text-danger">${ errors.computerName }</span>
                            </div>
                            <div class="form-group">
                                <label for="introduced">Introduced date</label>
                                <input type="date" class="form-control" id="introduced" name="introducedDate" value="<c:out value="${ param.introducedDate }" />" placeholder="Ex : 2001-11-23">
                                <span class="erreur text-danger">${ errors.introducedDate }</span>
                            </div>
                            <div class="form-group">
                                <label for="discontinued">Discontinued date</label>
                                <input type="date" class="form-control" id="discontinued" name="discontinuedDate" value="<c:out value="${ param.discontinuedDate }" />" placeholder="Doit être posterieure à celle d'au dessus">
                                <span class="erreur text-danger">${ errors.discontinuedDate }</span>
                            </div>
                            <div class="form-group">
                                <label for="companyId">Company</label>
                                <select class="form-control" id="companyId" name="company">
                                    <option value="0" selected>--</option>
                                    <c:forEach var="comp" items="${ requestScope.companies }">
                                        <option value="${ comp.id }" <c:if test="${ comp.id == param.company }">selected</c:if>>${ comp.name }</option>
                                    </c:forEach>
                                </select>
                            </div>                 
                        </fieldset>
                        <span style="text-weight:bold;">Field marked with (*) is mandatory !</span>
                        <div class="actions pull-right">
                            <input type="submit" value="Add" class="btn btn-primary">
                            or
                            <a href="<c:url value='/index' />" class="btn btn-default">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</body>
</html>