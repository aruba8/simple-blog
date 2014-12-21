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
    <#if post.isCommentsAvailable>
        <div class="comments">
            <#list comments as comment>
                <div class="comment">${comment["author"]?trim}:${comment["date"]?trim}:${comment["text"]?trim}</div>
            </#list>
            <#macro comment_form post_id>

                <form method="post" action="/addcomment">
                    <input name="post_id" type="hidden" value="${post_id}">
                    <textarea name="commentBody"></textarea>
                    <input type="submit" value="Send">
                </form>
            </#macro>

            <@comment_form post_id=post.post_id/>
        </div>
    </#if>
    <script>hljs.initHighlightingOnLoad();</script>
</div>
    <div class="copyright">by <a href="https://github.com/biomaks/simple-java.blog">Simple Blog</a> 2013</div>
</body>


</html>