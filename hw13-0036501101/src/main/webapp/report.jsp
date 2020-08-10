<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

	<body bgcolor=${sessionScope["pickedBgCol"]}>
		<h1>OS usage</h1>
		<p>Here are the results of OS usage in survey that we completed:</p>
		<br><br><img alt="Error occured while showing a chart" src="/webapp2/reportImage">
		
		<br><br><br><br><a href="/webapp2/index.jsp">Home</a>
	</body>
</html>