<!DOCTYPE html>
<html>
<head>
    <title>Main page</title>
    <link rel="stylesheet" href="style.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

</head>
<body>
<h1><a href="/">BLOG</a></h1>

<#list posts as post>
    <h3><a href="/post/${post["permalink"]}">${post["title"]} ${post["dateTime"]}</a></h3>
    <article>${post["articlePreview"]}</article>
</#list>






<div class="copyright">Erik Khalimov 2013</div>
</body>
</html>