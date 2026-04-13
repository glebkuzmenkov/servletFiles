<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Вход</title>
    </head>
    <body>
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
        %>
        <p style="color: red;"><%= error %></p>
        <%
            }
        %>
        <form method="post" action="${pageContext.request.contextPath}/login">
            <label>Логин: <input type="text" name="login" required></label><br><br>
            <label>Пароль: <input type="password" name="password" required></label><br><br>
            <button type="submit">Войти</button>
            <p>Если у вас ещё нет аккаунта: <a href="${pageContext.request.contextPath}/register">Зарегистрироваться</a></p>
        </form>
    </body>
</html>