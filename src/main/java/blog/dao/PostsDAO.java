package blog.dao;

import blog.logic.Post;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostsDAO {
    private DBCollection postCollection;

    public PostsDAO(final DB blogDB){
        postCollection = blogDB.getCollection("posts");
    }

    public void insertPost(Post post){
        String permalink = createPermalink(post.getTitle());
        String [] tags = post.getTagsString().split(",");
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
        DBObject post = null;

        DBObject query = new BasicDBObject("permalink", permalink);
        post = postCollection.findOne(query);

        return post;
    }

    private String createPermalink(String title){
        String permalink = title.replaceAll("\\s", "_");
        permalink.replaceAll("\\W", "");
        permalink.toLowerCase();
        return permalink;
    }




}


