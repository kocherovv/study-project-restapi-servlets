<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Вход в систему</h1>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label class="form-group" for="username">
        <input class="form-control item" type="text" name="username" id="username" maxlength="15" minlength="4"
               pattern="^[a-zA-Z0-9_.-]*$" placeholder="Имя пользователя" required>
    </label><br>
    <label class="form-group" for="password">
        <input class="form-control item" type="password" name="password" minlength="6" id="password"
               placeholder="Пароль" required>
    </label><br>
    <button class="btn btn-primary btn-block create-account" type="submit">Войти</button><br>
    <a href="${pageContext.request.contextPath}/registration">
        <button type="button">Зарегистрироваться</button>
    </a>
</form>
</body>
</html>
