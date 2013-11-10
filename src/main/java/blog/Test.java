package blog;

import blog.dao.PostsDAO;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;
import java.util.List;

public class Test {
    public static void main(String[] args) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
        DB db = mongoClient.getDB("blog");
        PostsDAO postsDAO = new PostsDAO(db);

        List<DBObject> list = postsDAO.findPostsByDescending(10);

        String s = "sdfgsdgsdg";

        System.out.println(s.substring(0, 600));


    }

    private static String createPermalink(String title) {
        String permalink = title.replaceAll("\\s", "_");
        permalink.replaceAll("\\W", "");
        permalink.toLowerCase();
        return permalink;
    }


}
