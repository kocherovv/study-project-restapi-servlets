<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Список файлов</title>
</head>
<body>
<%@include file="header.jsp" %>
<h1>Список файлов пользователя</h1>
<form action="${pageContext.request.contextPath}/files?event=UPLOAD" method="post" enctype="multipart/form-data">
    <label for="uploadFile">
        <input type="file" name="uploadFile" id="uploadFile">
        <button type="submit">Загрузить файл в базу</button>
    </label>
</form>
<ul>
    <c:forEach var="files" items="${requestScope.files}">
        <li>${files.name}.${files.extension}
            <form action=${pageContext.request.contextPath}/files?event=DOWNLOAD&file=${files.id}" method="post">
                <button type="submit">Скачать</button>
                <button formaction="${pageContext.request.contextPath}/files?event=DELETE&file=${files.id}" type="submit">Удалить</button>
            </form>
        </li>
    </c:forEach>
</ul>
</body>
</html>
