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
            <a class="navbar-brand" href="dashboard.html"> Application - Computer Database </a>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <h1 id="homeTitle">
                ${ nbComputers } Computers found
            </h1>
            <div id="actions" class="form-horizontal">
                <div class="pull-left">
                    <form id="searchForm" action="#" method="GET" class="form-inline">

                        <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name" />
                        <input type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary" />
                    </form>
                </div>
                <div class="pull-right">
                    <a class="btn btn-success" id="addComputer" href="addComputer.html">Add Computer</a> 
                    <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">Edit</a>
                </div>
            </div>
        </div>

        <form id="deleteForm" action="#" method="POST">
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
                            <td class="editMode"><input type="checkbox" name="cb" class="cb" value="0"></td>
                            <td><a href="editComputer.jsp" onclick=""><c:out value="${ computer.name }" /></a></td>
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
				   <a href="#" aria-label="Previous">
				       <span aria-hidden="true">&laquo;</span>
				      </a>
			     </li>
                 <li>
	                <a href="   <c:url value="/index">
	                                <c:param name="nbPage" value="1"/>
	                            </c:url>">1
	                </a>
                 </li>
                 <li>
                    <a href="   <c:url value="/index">
                                    <c:param name="nbPage" value="2"/>
                                </c:url>">2
                    </a>
                 </li>
                 <li>
                    <a href="   <c:url value="/index">
                                    <c:param name="nbPage" value="3"/>
                                </c:url>">3
                    </a>
                 </li>
                 <li>
                    <a href="   <c:url value="/index">
                                    <c:param name="nbPage" value="4"/>
                                </c:url>">4
                    </a>
                 </li>
                 <li>
                    <a href="   <c:url value="/index">
                                    <c:param name="nbPage" value="5"/>
                                </c:url>">5
                    </a>
                 </li>
                <li>
                <a href="#" aria-label="Next">
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