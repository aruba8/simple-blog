<!DOCTYPE html>
<html>
<head>
    <title>Add post page</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/themes/smoothness/jquery-ui.css"/>
    <link rel="stylesheet" href="style.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.10.4/jquery-ui.min.js"></script>
    <script src="http://rangy.googlecode.com/svn/trunk/currentrelease/rangy-core.js"></script>
    <script type="application/javascript" src="hallo.js"></script>
</head>
<body>

<!-- Place this in the body of the page content -->
<form method="post" action="/addpost">
    <textarea id="article-body" name="articleBody">
Title:
Date:${dateTime}
Categories:
Comments:true
~~~~~~~~~~~~~~

</textarea>
    <input type="submit" name="submit" value="submit" />
</form>

<script>
    $().ready(function () {
        $('p').hallo();
    });
</script>

</body>
</html>