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

	<c:choose>
		<c:when test="${blogEntries != null}">
			<c:forEach var="e" items="${blogEntries}">
				<ul>
					<li><a href="${context}/servleti/author/${nick}/${e.id}">${e.title} 
						<c:if test="${validLogin}">
							<a href="${context}/servleti/author/${nick}/edit?eid=${e.id}"> -------> (edit blog entry)</a>
						</c:if>
					</a>
					</li>
				</ul>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<h2>No blog entries from this user yet.</h2>
		</c:otherwise>
	</c:choose>

	<br>
	<br>
	<br>
	<c:if test="${validLogin}">
		<a href="${context}/servleti/author/${nick}/new">Add new blog entry.</a>
	</c:if>
	
	
	<br>
	<br>
	<br>
	<a href="${context}/servleti/main">Home</a>
</body>
</html>