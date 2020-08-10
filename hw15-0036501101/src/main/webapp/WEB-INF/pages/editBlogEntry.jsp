<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="context" value="${pageContext.request.contextPath}" />

<html>
<body>
	<c:choose>
		<c:when test="${sessionScope['current.user.id']==null}">
			<h5>Not logged in.</h5>
		</c:when>
		<c:otherwise>
			<h5>
				Current user: ${sessionScope["current.user.fn"]}
				${sessionScope["current.user.ln"]} <a href="${context}/servleti/logout">
					logout</a>
			</h5>
		</c:otherwise>
	</c:choose>

	<form action="${context}/servleti/author/${nick}/edit" method="post">
		<input type="hidden" value="${blogEntry.id}" name="eid" />
		Title: <input type="text" name="title" value="${blogEntry.title}"><br><br>
		<textarea name="text" style="width:800px; height:400px">${blogEntry.text}</textarea>
		<br><br> <input type="submit" value="Submit">
	</form>


	<br>
	<br>
	<br>
	<a href="${context}/servleti/main">Home</a>
</body>
</html>