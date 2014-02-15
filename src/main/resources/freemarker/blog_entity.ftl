<!DOCTYPE html>
<html>
<head>
    <title>Main page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="../style.css">
</head>
<body>
<div id="header"><h1><a href="/">BLOG</a></h1></div>

<div id="content">

<div class="date">${post["dateTime"]}</div>
<div class="title">${post["title"]}</div>
    <ul class="post-info">
    <#if post.tags ??>
        <#list post.tags as tag>
            <li><a href="/category?c=${tag}">${tag}</a></li>
        </#list>
    </#if>
    </ul>

    <div class="article">
    <article>
    ${post["articleBody"]}
    </article>
</div>
</div>
<div class="copyright">by <a href="https://github.com/biomaks/simple-blog">Simple Blog</a> 2013</div>
</body>
</html>