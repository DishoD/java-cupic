<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%!
	private static String timeDifference(long from, long to) {
		StringBuilder sb = new StringBuilder();
		long delta = to - from;
		long days = delta/(1000*60*60*24);
		sb.append(days).append(" days ");
		long hours = (delta - days*1000*60*60*24)/(1000*60*60);
		sb.append(hours).append(" hours ");
		long minutes = (delta - days*1000*60*60*24 - hours*1000*60*60)/(1000*60);
		sb.append(minutes).append(" minutes ");
		long seconds = (delta - days*1000*60*60*24 - hours*1000*60*60 - minutes*1000*60)/(1000);
		sb.append(seconds).append(" seconds");
		
		return sb.toString();
	}	
%>

<!DOCTYPE html>
<html>

	<body bgcolor=${sessionScope["pickedBgCol"]}>
		<h1>This server has been running:</h1>
		<h2><%=timeDifference((long)application.getAttribute("startTime"), System.currentTimeMillis())%></h2> 
		
		<br><br><br><br><a href="/webapp2/index.jsp">Home</a>
	</body>
</html>