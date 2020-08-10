<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html>

<html>
	
	<head>
		<meta charset="utf-8">
	</head>

	<body bgcolor=${sessionScope["pickedBgCol"]}>
		<table border="1">
			<tr><th>degrees</th><th>sin</th><th>cos</th></tr>
			<c:forEach var="entry" items="${trigList}">
				<tr><td>${entry.deg}</td><td>${entry.sin}</td><td>${entry.cos}</td></tr>
			</c:forEach>
		</table>
		
		<br><a href="/webapp2/index.jsp">Home</a>
	</body>
</html>