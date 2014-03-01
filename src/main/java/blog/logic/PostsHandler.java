package blog.logic;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsHandler {
    private static Logger logger = LogManager.getLogger(PostHandler.class.getName());

    public static List<Map> getPostsList(DBCursor cursor) {
        List<DBObject> posts = new ArrayList<DBObject>();
        while (cursor.hasNext()){
            DBObject post = cursor.next();
            posts.add(post);
        }

        List<Map> postsList = new ArrayList<Map>();
        for (DBObject post : posts) {
            Map postsMap = PostHandler.getPost(post);
            postsList.add(postsMap);
        }
        return postsList;
    }


}
