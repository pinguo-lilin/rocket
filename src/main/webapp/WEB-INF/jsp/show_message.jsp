<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<form action="/rocket/msg/send" method="post">
    <table>
        <tr>
            <td>appName</td>
            <td><input type="text" name="appName"></td>
        </tr>
        <tr>
            <td>opcode</td>
            <td><input type="text" name="opcode"></td>
        </tr>
        <tr>
            <td>info</td>
            <td><input type="text" name="info"></td>
        </tr>
        <tr>
            <td>time</td>
            <td><input type="text" name="time"></td>
        </tr>
        <tr>
            <td>key</td>
            <td><input type="text" name="key"></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="send"></td>
        </tr>
    </table>
</form>
</body>
</html>