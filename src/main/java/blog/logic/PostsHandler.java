package blog.logic;

import blog.models.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PostsHandler {
    private static Logger logger = LogManager.getLogger(PostHandler.class.getName());

    public static List<Map> getPostsList(List<Post> posts) {

        List<Map> postsList = new ArrayList<Map>();
        for (Post post : posts) {
            Map postsMap = PostHandler.getPost(post);
            postsList.add(postsMap);
        }
        return postsList;
    }


}
