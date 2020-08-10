<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<body>
	<h1>${poll.title}:</h1>
	<p>${poll.message}</p>
	<ol>
		<c:forEach var="entry" items="${pollOptions}">
			<li><a href="/voting-app/servleti/glasanje-glasaj?id=${entry.id}">${entry.title}</a></li>
		</c:forEach>
	</ol>


	<br>
	<br>
	<br>
	<br>
	<a href="/voting-app/servleti/index.html">Home</a>
</body>
</html>