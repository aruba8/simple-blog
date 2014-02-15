package blog.logic;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsHandler {

    public static List<Map> getPostsList(DBCursor cursor) {
        List<DBObject> posts = new ArrayList<DBObject>();
        while (cursor.hasNext()){
            posts.add(cursor.next());
        }

        List<Map> postsList = new ArrayList<Map>();
        for (DBObject post : posts) {
            Map postsMap = PostHandler.getPost(post);
            postsList.add(postsMap);
        }
        return postsList;
    }


}
