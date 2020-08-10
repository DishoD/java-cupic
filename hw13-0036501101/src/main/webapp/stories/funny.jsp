<%@page import="java.util.concurrent.ThreadLocalRandom"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%!
	private static final List<String> colorsList = new ArrayList<>();

	static {
		colorsList.add("red");
		colorsList.add("green");
		colorsList.add("blue");
		colorsList.add("cyan");
		colorsList.add("aqua");
		colorsList.add("fuchsia");
		colorsList.add("lime");
	}
	
	private static String getRandomColor() {
		return colorsList.get(ThreadLocalRandom.current().nextInt(colorsList.size()));
	}
%>

<!DOCTYPE html>
<html>

<body bgcolor=${sessionScope["pickedBgCol"]}>
	
	<p style="color:<%=getRandomColor()%>">
	Algorithm (noun.)
	<br> A word used by programmers when they don't want to explain what they did.
	</p>


	<br>
	<br>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Home</a>
</body>
</html>