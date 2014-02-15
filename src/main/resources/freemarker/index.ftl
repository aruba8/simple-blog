<!DOCTYPE html>
<html>
<head>
    <title>Main page</title>
    <link rel="stylesheet" href="style.css">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

</head>
<body>
<div id="header"><h1><a href="/">BLOG</a></h1></div>
<div id="content">
<#list posts as post>
    <h3><a href="/post/${post["permalink"]}">${post["title"]} ${post["dateTime"]}</a></h3>
    <article class="article-preview">${post["articlePreview"]}</article>
    <ul class="post-info">
    <#if post.tags ??>
    <#list post.tags as tag>
    <li><a href="/category?c=${tag}">${tag}</a></li>
    </#list>
    </#if>
    </ul>
</#list>

</div>


<div class="copyright">Erik Khalimov 2013</div>
</body>
</html>