package blog.logic;

import blog.models.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentHandler {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public static List<Map> getComments(List<Comment> comments){
        List<Map> commentsList = new ArrayList<Map>();
        for (Comment comment : comments){
            Map<String, Object> commentMap = new HashMap<String, Object>();
            commentMap.put("text", comment.getText());
            commentMap.put("author", comment.getAuthor().getUsername());
            commentMap.put("date", dateFormat.format(comment.getDate()));
            commentsList.add(commentMap);
        }
        return commentsList;
    }

}
