package blog.dao;

import blog.logic.Post;
import blog.logic.Translit;
import com.mongodb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsDAO {
    private DBCollection postCollection;
    Logger logger = LogManager.getLogger(PostsDAO.class.getName());

    public PostsDAO(final DB blogDB){
        postCollection = blogDB.getCollection("posts");
    }

    public void insertPost(Post post){
        String permalink = createPermalink(post.getTitle());
        String [] tags = post.getTagsString().split(",");
        logger.info(post.getTitle());
        logger.info(permalink);

        DBObject queryToInsertPost = new BasicDBObject("dateTime", new Date())
                .append("title", post.getTitle())
                .append("articleBody", post.getArticleBody())
                .append("tags", tags)
                .append("permalink", permalink);

        postCollection.insert(queryToInsertPost);

    }

    public List<DBObject> findPostsByDescending(int limit){
        List<DBObject> posts = new ArrayList<DBObject>();

        DBObject sortQuery = new BasicDBObject("dateTime", -1);
        DBCursor cursor = postCollection.find().sort(sortQuery).limit(limit);

        while (cursor.hasNext()){
            posts.add(cursor.next());
        }

        return posts;
    }

    public DBObject findByPermalink(String permalink){
        DBObject query = new BasicDBObject("permalink", permalink);
        return postCollection.findOne(query);
    }

    private String createPermalink(String title){
        String titleInTranslit = Translit.toTranslit(title);
        String permalink = titleInTranslit.replaceAll("\\s", "-");
        permalink = permalink.replaceAll("\\W", "");
        permalink = permalink.toLowerCase();
        if (permalink.length() > 60) {
            return permalink.substring(0, 60);
        } else {
            return permalink;
        }
    }

}


