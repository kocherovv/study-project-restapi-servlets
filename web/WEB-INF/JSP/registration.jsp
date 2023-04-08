<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <link href="authorization.css" type="text/css" rel="stylesheet">
    <title>Регистрация</title>
</head>
<body>
<h1>Регистрация</h1>
<div class="registration-cssave">
    <form action="${pageContext.request.contextPath}/registration" method="post">
        <label class="form-group" for="username">
            <input class="form-control item" type="text" name="username" id="username" maxlength="15" minlength="4" pattern="^[a-zA-Z0-9_.-]*$" placeholder="Имя пользователя" required>
        </label><br>
        <label class="form-group" for="email">
            <input class="form-control item" type="email" name="email" id="email" placeholder="Email" required>
        </label><br>
        <label class="form-group" for="password">
            <input class="form-control item" type="password" name="password" minlength="6" id="password" placeholder="Пароль" required>
        </label><br>
        <button class="btn btn-primary btn-block create-account" type="submit">Зарегистрироваться</button><br>
        <a href="${pageContext.request.contextPath}/login">
            <button type="button">У меня уже есть профиль</button>
        </a>
    </form>
</div>
</body>
</html>
