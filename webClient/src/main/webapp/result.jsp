<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 14.01.2023
  Time: 01:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Chat-App</title>
<%--    <meta charset="UTF-8">--%>
</head>
<body>
<h2 align="center">Chat app</h2>

<c:if test="${not empty nick}">
    <h3 align="center"> ${nick}</h3>
</c:if>

<h3 align="center">Change nickname
<form align = center action="/nickname" method="post">
    <p><input type="text" name="nickname"></p>
    <p><input type="submit" value="Enter"></p>
</form>
</h3>

<div  class="mytext" style="text-align: center">
    <textarea readonly cols="150" rows="25">
    ${conv}
</textarea>
</div>

<form align = center action="/send" method="post" accept-charset="ISO-8859-1">
    <p><input style="width: 700px" type="text" name="message" ><input type="submit" value="Send"></p>
</form>

</body>
</html>
