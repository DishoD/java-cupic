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

	<h2>${blogEntry.title}</h2>
	<h4>of user: ${blogEntry.creator.nick}</h4>
	<p>${blogEntry.text}</p>
	<h4>Creation at: ${blogEntry.createdAt}</h4>
	<h4>Last modified at: ${blogEntry.lastModifiedAt}</h4>

	<c:if test="${validLogin}">
		<br><br><br><a href="${context}/servleti/author/${nick}/edit?eid=${blogEntry.id}">Edit this blog entry</a>
	</c:if>
	
	<hr>
	
	<c:forEach var="comment" items="${blogEntry.comments}">
		<br><br><br>
		<h4>Comment posted by: ${comment.usersEMail}</h4>
		<p>${comment.message}</p>
		<h4>Posted on: ${comment.postedOn}</h4>
	</c:forEach>
	
	<hr>
	<br><br><br>
	<h4>Post a comment:</h4>
	<form action="${context}/servleti/author/${nick}/${blogEntry.id}" method="post">
		E-mail: <input type="text" name="email"><br><br>
		<textarea name="message" style="width:400px; height:200px">Enter your comment here!</textarea>
		<br><br> <input type="submit" value="Submit">
	</form>

	<br>
	<br>
	<br>
	<a href="${context}/servleti/main">Home</a>
</body>
</html>