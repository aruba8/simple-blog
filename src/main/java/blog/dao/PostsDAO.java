package blog.dao;

import blog.models.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class PostsDAO {
    private Session hibernateSession;
    public PostsDAO(Session session){this.hibernateSession = session;}
    Logger logger = LogManager.getLogger(PostsDAO.class.getName());



    public void insertPost(Post post){
        hibernateSession.getTransaction().begin();
        hibernateSession.save(post);
        hibernateSession.getTransaction().commit();
    }

    public List<Post> findPostsByDescending(int limit){
        hibernateSession.getTransaction().begin();
        List<Post> postList = hibernateSession.createCriteria(Post.class).addOrder(Order.desc("dateTime")).setMaxResults(limit).list();
        hibernateSession.getTransaction().commit();
        return postList;
    }

    public List<Post> findPostsByTagDesc(String tagName, int limit){
        hibernateSession.getTransaction().begin();
        String hql = "select p from Post as p inner join p.tags as t where t.name = ?";
        List<Post> posts = hibernateSession.createQuery(hql).setString(0, tagName).setMaxResults(limit).list();
        hibernateSession.getTransaction().commit();
        return posts;
    }

    public Post findByPermalink(String permalink){
        hibernateSession.getTransaction().begin();
        Post post = (Post) hibernateSession.createCriteria(Post.class).add(Restrictions.eq("permalink", permalink)).uniqueResult();
        hibernateSession.getTransaction().commit();
        logger.debug(post.getTagNames().length);
        return post;
    }


    public String updatePost(String id, Post post) {
        hibernateSession.getTransaction().begin();
        Post updPost = (Post) hibernateSession.get(Post.class, Long.parseLong(id));

        updPost.setArticleBody(post.getArticleBody());
        updPost.setTitle(post.getTitle());
        updPost.setDateTime(post.getDateTime());
        updPost.setIsCommentsAvailable(post.getIsCommentsAvailable());
        updPost.setTags(post.getTags());
        logger.debug("tags:"+post.getTagNames().length);
        updPost.setPermalink(post.getPermalink());

        hibernateSession.update(updPost);
        hibernateSession.getTransaction().commit();
        return updPost.getPermalink();
    }


}


