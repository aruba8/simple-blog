<form method="post" action="/addcomment">
    <input name="post_id" type="hidden" value="${post["post_id"]}">
    <textarea name="commentBody"></textarea>
    <input type="submit" value="Send">
</form>