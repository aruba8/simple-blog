<!DOCTYPE html>
<html>
<head>
    <title>Add post page</title>
</head>
<body>

<!-- Place this in the body of the page content -->

<form name="form" method="post" action="/addpost">
    <label>
        <input type="text" name="title" placeholder="Title">
    </label>
    <br/>
    <label>
        <textarea name="articleBody" cols="40" rows="30" ></textarea>
    </label>
    <br/>
    <label>
        <input type="text" name="tags" placeholder="tags">
    <input type="submit" name="submit" value="submit">
    </label>
    <br/>
</form>

</body>
</html>