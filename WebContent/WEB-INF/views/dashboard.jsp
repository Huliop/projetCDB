<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false"%>

<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="<c:url value="/index?numPage=1" />"> Application - Computer Database </a>
        </div>
    </header>
    
    <c:if test='${ param.success == "true" }'>
        <section id="main">
            <div class="container">
                <div class="alert alert-success">
                    The computer has been edited!
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
                            ${ param.length } computer has been successfully deleted !
                        </c:when>
                        <c:otherwise>
                            ${ param.length } computers have been successfully deleted !
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>
    </c:if>
    
    <c:if test='${ !empty errors }'>
        <section id="main">
            <div class="container">
                <div class="alert alert-danger">
                    ${ errors.errorDeleting }
                </div>
            </div>
        </section>
    </c:if>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${ nbComputers } Computers found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">
                        <c:choose>
                            <c:when test='${ param.search != null || param.search != "" }'>
                                <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" value="<c:out value='${ param.search }' />" />
                            </c:when>
                            <c:otherwise>
                                <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                            </c:otherwise>
                        </c:choose>
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="<c:url value='/addComputer' />">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Delete</a>
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
                        <th class="editMode" style="width: 60px; height: 22px;">
                            <input type="checkbox" id="selectall" /> 
                            <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                        </th>
                        <th>Computer name</th>
                        <th>Introduced date</th>
                        <th>Discontinued date</th>
                        <th>Company</th>
                    </tr>
                </thead>
                <!-- Browse attribute computers -->
                <tbody id="results">
                    <c:forEach var="computer" items="${ requestScope.computers }">
                        <tr>
                            <td class="editMode"><input type="checkbox" name="cb" class="cb" value='${ computer.id }'></td>
                            <td><a href="<c:url value='/editComputer'><c:param name="idComputer" value="${ computer.id }" /></c:url>" onclick=""><c:out value="${ computer.name }" /></a></td>
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
                <li>
                   <a href="<c:url value='/index'>
                                <c:param name="numPage" value='${ requestScope.numPages - 1 }'/>
                                <c:if test="${ param.search != null && param.search != '' }">
                                    <c:param name="search" value="${ param.search }" />
                                </c:if>
                            </c:url>" aria-label="Previous">
                       <span aria-hidden="true">&laquo;</span>
                      </a>
                 </li>
                <c:forEach var="number" items="${ requestScope.navFooter }">
                        <li>
                            <a href="   <c:url value="/index">
                                            <c:if test="${ number != '...'}">
                                                <c:param name="numPage" value="${ number }"/>
                                            </c:if> 
                                            <c:if test="${ param.search != null && param.search != '' }">
                                                <c:param name="search" value="${ param.search }" />
                                            </c:if>
                                        </c:url>">${ number }
                            </a>
                        </li>
                </c:forEach>
                <li>
                <a href="<c:url value='/index'>
                              <c:param name="numPage" value='${ requestScope.numPages + 1 }'/>
                              <c:if test="${ param.search != null && param.search != '' }">
                                <c:param name="search" value="${ param.search }" />
                              </c:if>
                         </c:url>" aria-label="Next">
                         <span aria-hidden="true">&raquo;</span>
                </a>
                </li>
           </ul>

        <div class="btn-group btn-group-sm pull-right" role="group" >
            <a href="   <c:url value="/index">
                            <c:param name="offset" value="10"/>
                        </c:url>">
                        <button type="button" class="btn btn-default">10</button>
            </a>
            <a href="   <c:url value="/index">
                            <c:param name="offset" value="50"/>
                        </c:url>">
                        <button type="button" class="btn btn-default">50</button>
            </a>
            <a href="   <c:url value="/index">
                            <c:param name="offset" value="100"/>
                        </c:url>">
                        <button type="button" class="btn btn-default">100</button>
            </a>
        </div>
        </div>

    </footer>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/dashboard.js"></script>

</body>
</html>