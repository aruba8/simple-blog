package blog.dao;

import blog.logic.Post;
import blog.logic.Translit;
import com.mongodb.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class PostsDAO {
    private DBCollection postCollection;
    Logger logger = LogManager.getLogger(PostsDAO.class.getName());

    public PostsDAO(final DB blogDB){
        postCollection = blogDB.getCollection("posts");
    }

    public void insertPost(Post post){
        String permalink = createPermalink(post.getTitle());

        BasicDBObject queryToInsertPost = new BasicDBObject("dateTime", new Date())
                .append("title", post.getTitle())
                .append("articleBody", post.getArticleBody())
                .append("permalink", permalink);

        if (post.getTags() != null) {
            String[] tags = post.getTags();
            queryToInsertPost.append("tags", tags);
        }

        postCollection.insert(queryToInsertPost);

    }

    public DBCursor findPostsByDescending(int limit){
        DBObject sortQuery = new BasicDBObject("dateTime", -1);
        DBCursor cursor = postCollection.find().sort(sortQuery).limit(limit);
        return cursor;
    }

    public DBCursor findPostsByTagDesc(String tag, int limit){
        DBObject sortQuery = new BasicDBObject("dateTime", -1);
        DBObject query = new BasicDBObject("tags", tag);
        DBCursor cursor = postCollection.find(query).sort(sortQuery).limit(limit);
        return cursor;
    }

    public DBObject findByPermalink(String permalink){
        DBObject query = new BasicDBObject("permalink", permalink);
        return postCollection.findOne(query);
    }

    private String createPermalink(String title){
        String titleInTranslit = Translit.toTranslit(title);
        String permalink = titleInTranslit.replaceAll("\\s", "-");
        permalink = permalink.replaceAll("\\W", "-");
        permalink = permalink.toLowerCase();
        if (permalink.length() > 60) {
            return permalink.substring(0, 60);
        } else {
            return permalink;
        }
    }

}


