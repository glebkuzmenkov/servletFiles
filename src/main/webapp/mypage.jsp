<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.nio.charset.StandardCharsets" %>
<html>
<head>
    <title>Проводник</title>
</head>
    <body>
        <form action="${pageContext.request.contextPath}/logout" method="get">
            <button type="submit">Выход</button>
        </form>
        <b> ${generatedTime}</b>
        <h2>${currentPath}</h2>
        <%
            String parent = (String) request.getAttribute("parentPath");
            if (parent != null) {
                String encodedParent = URLEncoder.encode(parent, StandardCharsets.UTF_8);
        %>
        <a href="files?path=<%= encodedParent %>">Вверх</a>
        <%
            }
        %>
        <table>
            <tr>
                <th>Имя</th>
                <th>Размер</th>
                <th>Дата последнего изменения</th>
            </tr>

            <%
                File[] files = (File[]) request.getAttribute("content");
                if (files != null) {
                    for (File file : files) {
                        String encodedPath = URLEncoder.encode(file.getAbsolutePath(), StandardCharsets.UTF_8);
                        String name = file.getName();
                        if (file.isDirectory()) {
                            name += "/";
                        }
            %>
            <tr>
                <td>
                    <a href="files?path=<%= encodedPath %>"><%= name %></a>
                </td>
                <td>
                    <% if (file.isFile()) { %>
                        <%= file.length() %> байт
                    <% } else { %>
                        &mdash;
                    <% } %>
                </td>
                <td>
                    <%= new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm")
                            .format(new java.util.Date(file.lastModified())) %>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="3">Директория пуста</td></tr>
            <%
                }
            %>
        </table>
    </body>
</html>