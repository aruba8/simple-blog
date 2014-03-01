<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Main page</title>
    <link rel="stylesheet" href="style.css"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

</head>
<body>
<div id="header"><h1><a href="/">${blogName}</a></h1></div>
<div id="content">
<div class="category"><h2>Category: ${category}</h2></div>
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


<div class="copyright">by <a href="https://github.com/biomaks/simple-blog">Simple Blog</a> 2013</div>
</body>
</html>