<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">
table.rez td {
	text-align: center;
}
</style>
</head>
<body>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" class="rez">
		<thead>
			<tr>
				<th>Odabir</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${results}">
				<tr>
				<td>${entry.title}</td>
				<td>${entry.votes}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>

	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/voting-app/servleti/glasanje-grafika?pollID=${param.pollID}">

	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a href="/voting-app/servleti/glasanje-xls?pollID=${param.pollID}">ovdje</a>
	</p>

	<h2>Razno</h2>
	<p>Zanimljivi linkovi pobjedničkih odabira:</p>
	<ul>
		<c:forEach var="entry" items="${bestOptions}">
			<li><a href="${entry.link}" target="_blank">${entry.title}</a></li>
		</c:forEach>
	</ul>


	<br>
	<br>
	<br>
	<a href="/voting-app/index.html">Home</a>
</body>
</html>