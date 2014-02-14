package blog.logic;

import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsHandler {
    List<DBObject> posts;

    public PostsHandler(List<DBObject> posts) {
        this.posts = posts;
    }

    public List<Map> getPostsList() {
        List<Map> postsList = new ArrayList<Map>();
        for (DBObject post : posts) {
            PostHandler postHandler = new PostHandler();
            Map postsMap = postHandler.getPost(post);
            postsList.add(postsMap);
        }
        return postsList;
    }


}
