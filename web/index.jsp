<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <link href="index.css" type="text/css" rel="stylesheet">
</head>
<body>
<h1>Вход в систему</h1>
<div class="registration-cssave">
    <form>
        <div class="form-group">
            <a href="${pageContext.request.contextPath}/login">
                <button class="btn btn-primary btn-block menu-button"  type="button">Войти</button>
            </a>
        </div>
        <div class="form-group">
            <a href="${pageContext.request.contextPath}/registration">
            <button class="btn btn-primary btn-block menu-button" type="button">Зарегистрироваться</button>
            </a>
        </div>
    </form>
</div>
</body>
</html>