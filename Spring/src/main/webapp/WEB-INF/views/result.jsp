<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>결과 페이지</title>
</head>
<body>
<h1>결과 페이지</h1>
<table>
    <tr>
        <th>ID</th>
        <th>결과</th>
    </tr>
    <% if (resultsList != null && !resultsList.isEmpty()) { %>
    <% for (Result result : resultsList) { %>
    <tr>
        <td><%= result.getId() %></td>
        <td><%= result.getResult() %></td>
    </tr>
    <% } %>
    <% } else { %>
    <tr>
        <td colspan="2">결과가 없습니다.</td>
    </tr>
    <% } %>
</table>
</body>
</html>
