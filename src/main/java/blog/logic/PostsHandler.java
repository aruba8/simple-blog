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
        for (int i =0; i < posts.size(); i++) {
            DBObject post = posts.get(i);
            PostHandler postHandler = new PostHandler(post);
            Map postsMap = postHandler.getPost();
            postsList.add(postsMap);
        }
        return postsList;
    }


}
