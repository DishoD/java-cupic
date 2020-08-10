<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<body bgcolor=${sessionScope["pickedBgCol"]}>
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach var="entry" items="${bandList}">
			<li><a href="glasanje-glasaj?id=${entry.id}">${entry.name}</a></li>
		</c:forEach>
	</ol>


	<br>
	<br>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Home</a>
</body>
</html>