package blog.dao;

import blog.models.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import java.util.Date;
import java.util.List;

public class PostsDAO {
    private Datastore ds;
    public PostsDAO(Datastore ds){this.ds = ds;}
    Logger logger = LogManager.getLogger(PostsDAO.class.getName());



    public void insertPost(Post post){
        logger.debug(post);
        ds.save(post);
    }

    public List<Post> findPostsByDescending(int limit){
        return ds.find(Post.class).order("-dateTime").limit(limit).asList();
    }

    public List<Post> findPostsByTagDesc(String tag, int limit){
        return ds.find(Post.class).field("tags").contains(tag).order("-dateTime").limit(limit).asList();
    }

    public Post findByPermalink(String permalink){
        return  ds.find(Post.class).field("permalink").equal(permalink).get();
    }


    public String updatePost(String id, Post post) {

        ds.update(ds.find(Post.class).field("id").equal(new ObjectId(id)), ds.createUpdateOperations(Post.class).
                set("title", post.getTitle()).
                set("articleBody", post.getArticleBody()).
                add("updatedTime", new Date()).
                set("isCommentsAvailable", post.getIsCommentsAvailable()).
                set("tags", post.getTags()));

        Post postFound = ds.find(Post.class).field("id").equal(new ObjectId(id)).get();
        return postFound.getPermalink();
    }
}


