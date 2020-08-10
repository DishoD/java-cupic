<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<body bgcolor=${sessionScope["pickedBgCol"]}>
	<a href="/webapp2/colors.jsp">Background color chooser</a>
	<br><a href="/webapp2/trigonometric?a=0&b=90">Trigonometric values for angles between 0 and 90 degrees</a>
	<br><a href="/webapp2/stories/funny.jsp">Read a funny story.</a>
	<br><a href="/webapp2/report.jsp">OS usage statistics</a>
	<br><a href="/webapp2/powers?a=1&b=100&n=3">Generate excel document</a>
	<br><a href="/webapp2/appinfo.jsp">Application information</a>
	<br><a href="/webapp2/glasanje">Voting for the favorite band</a>

	<br><br><form action="trigonometric" method="GET">
		Početni kut:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset" value="Reset">
	</form>

</body>
</html>