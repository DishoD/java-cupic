<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<body>
	<h1>Lista ponuđenih anketa:</h1>
	<p>Kliknite na jednu od sljedećih anketa kako biste glasali:</p>
	<ol>
		<c:forEach var="entry" items="${polls}">
			<li><a href="/voting-app/servleti/glasanje?pollID=${entry.id}">${entry.title}</a></li>
		</c:forEach>
	</ol>


	<br>
	<br>
	<br>
	<br>
	<a href="/voting-app/servleti/index.html">Home</a>
</body>
</html>