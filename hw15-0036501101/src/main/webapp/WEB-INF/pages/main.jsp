<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
<body>
	<c:choose>
		<c:when test="${sessionScope['current.user.id']==null}">
			<h5>Not logged in.</h5>

			<form action="${context}/servleti/login" method="POST">
				Nick: <br> <input type="text" name="nick" value="${nick}"> <br>
				Password: <br> <input type="password" name="password"> <br>
				<br> <input type="submit" value="Log in">
			</form>
		
		<p style="color:red;">${errorMsg}</p>
		</c:when>
		<c:otherwise>
			<h5>
				Current user: ${sessionScope["current.user.fn"]}
				${sessionScope["current.user.ln"]} <a href="${context}/servleti/logout">
					logout</a>
			</h5>
		</c:otherwise>
		</c:choose>

		<br>
		<br>
		<br>
		<a href="${context}/servleti/register">Register a new blog user.</a>

		<br>
		<br>
		<br>
		<h3>A list of blog authors:</h3>
		<ul>
			<c:forEach var="e" items="${authors}">
				<li><a href="${context}/servleti/author/${e}">${e}</a></li>
			</c:forEach>
		</ul>
</body>
</html>