<!DOCTYPE html>
<html>
<head>
    <title>Main page</title>
    <link rel="stylesheet" href="../style.css">
</head>
<body>
<h1><a href="/">BLOG</a></h1>


<div class="nav"><- <a href="/">.</a> -></div>

<div class="date">${post["dateTime"]}</div>
<div class="title">${post["title"]}</div>
<div class="article">
    <article>
        ${post["articleBody"]}
    </article>
</div>
<div class="copyright">Erik Khalimov 2013</div>
</body>
</html>