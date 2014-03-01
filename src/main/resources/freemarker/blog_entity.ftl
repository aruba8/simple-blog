<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
<div id="comments">
    <div id="disqus_thread"></div>
    <script type="text/javascript">
        /* * * CONFIGURATION VARIABLES: EDIT BEFORE PASTING INTO YOUR WEBPAGE * * */
        var disqus_shortname = '${diqusShortName}'; // required: replace example with your forum shortname

        /* * * DON'T EDIT BELOW THIS LINE * * */
        (function() {
            var dsq = document.createElement('script'); dsq.type = 'text/javascript'; dsq.async = true;
            dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
            (document.getElementsByTagName('head')[0] || document.getElementsByTagName('body')[0]).appendChild(dsq);
        })();
    </script>
    <noscript>Please enable JavaScript to view the <a href="http://disqus.com/?ref_noscript">comments powered by Disqus.</a></noscript>
    <a href="http://disqus.com" class="dsq-brlink">comments powered by <span class="logo-disqus">Disqus</span></a>
</div>
    <script>hljs.initHighlightingOnLoad();</script>
</div>
    <div class="copyright">by <a href="https://github.com/biomaks/simple-blog">Simple Blog</a> 2013</div>
</body>
</html>