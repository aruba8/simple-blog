<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Вход</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

</head>

<body>

<#if authorized == 'true'>
Already authorized!
<form class="form-logout" method="post" action="/logout">
    <input type="submit" name="logout" value="logout">
</form>
<#else>
<form method="post" action="/login">
    <h2>Вход</h2>
    <input type="text" name="username" value="" placeholder="логин"/>
    <input type="password" name="password" value="" placeholder="пароль"/>
    <button type="submit">Войти</button>
</form>
</#if>
</body>

</html>
