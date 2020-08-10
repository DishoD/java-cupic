<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

	<body bgcolor=${sessionScope["pickedBgCol"]}>
		<a href="/webapp2/setcolor?color=white">WHITE</a>
		<a href="/webapp2/setcolor?color=red">RED</a>
		<a href="/webapp2/setcolor?color=green">GREEN</a>
		<a href="/webapp2/setcolor?color=cyan">CYAN</a>
		
		<br><br><br><br><a href="/webapp2/index.jsp">Home</a>
	</body>
</html>