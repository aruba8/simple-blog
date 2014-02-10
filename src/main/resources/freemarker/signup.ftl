<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Sign up</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

</head>

<body>

<form method="post" action="/signup">
    <h2>Register</h2>
    <input type="text" name="username" value="" placeholder="login"/>
    <input type="password" name="password_1" value="" placeholder="password"/>
    <input type="password" name="password_2" value="" placeholder="repeat password"/>
    <input type="submit" name="submit" value="Register"/>
<#if error??>
    <div class="error"><span>${error}</span></div>
</#if>
</form>
</body>

</html>
