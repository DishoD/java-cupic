<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<body bgcolor=${sessionScope["pickedBgCol"]}>
	<h1>Illegal parameters passed to the /powers action</h1>
	<p>
		An /powers action generates an excel sheet with n pages. Each page
		contains two columns. <br>First column contains numbers from a to
		b and second column contains n-th power of that number. <br>Consequently,
		/powers action takes 3 parameters: a, b and n.
	</p>
	<p>a is interval [-100, 100]</p>
	<p>b is interval [-100, 100]</p>
	<p>n is interval [1, 5]</p>

	<form action="powers" method="GET">
		a=<br>
		<input type="number" name="a" min="-100" max="100" step="1" value="0"><br>
		b=<br>
		<input type="number" name="b" min="-100" max="100" step="1" value="0"><br>
		n=<br>
		<input type="number" name="n" min="1" max="5" step="1" value="0"><br>
		<input type="submit" value="Create table">
		<input type="reset"	value="Reset">
	</form>

	<br>
	<br>
	<br>
	<br>
	<a href="/webapp2/index.jsp">Home</a>
</body>
</html>