<!DOCTYPE html>
<html>
<head>
    <title>Main page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="../style.css"/>
    <link rel="stylesheet" href="../github.css"/>
    <script src="../highlight.pack.js"></script>

</head>
<body>
<div id="header"><h1><a href="/">${blogName}</a></h1></div>

<div id="content">
<div class="date">${post["dateTime"]}
    <#if currentUser??>
        <#if currentUser["id"] == post["author_id"] >
            <a class="edit" href="/edit/${post["permalink"]}"> edit</a>
        </#if></div>
    </#if>

<div class="title">${post["title"]}</div>
    <ul class="post-info">
    <#if post.tags ??>
        <#list post.tags as tag>
            <li><a href="/category?c=${tag["name"]}">${tag["name"]}</a></li>
        </#list>
    </#if>
    </ul>

    <div class="article">
    <article>
    ${post["articleBody"]?trim}
    </article>
</div>
    <div class="comments">
        <#list comments as comment>
            <div class="comment">
                ${comment["author"]}:
                ${comment["date"]}:
                ${comment["text"]}
            </div>
        </#list>

        <#include "comment_form.ftl">
    </div>

    <script>hljs.initHighlightingOnLoad();</script>
</div>
    <div class="copyright">by <a href="https://github.com/biomaks/simple-java.blog">Simple Blog</a> 2013</div>
</body>
</html>