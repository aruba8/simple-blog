package blog.dao;

import blog.models.Comment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public class CommentDAO {
    private Session hibernateSession;
    Logger logger = LogManager.getLogger(CommentDAO.class.getName());

    public CommentDAO(Session session){
        this.hibernateSession = session;
    }

    public Long insertComment(Comment comment){
        hibernateSession.getTransaction().begin();
        Long commentId = (Long) hibernateSession.save(comment);
        hibernateSession.getTransaction().commit();
        return commentId;
    }

    public List<Comment> getCommentsByPostId(Long postId){
        hibernateSession.getTransaction().begin();
        String hql = "select c from comment as c where c.post.id = ? order by c.date asc";
        List<Comment> comments = hibernateSession.createQuery(hql).setLong(0, postId).list();
        hibernateSession.getTransaction().commit();
        return comments;
    }




}
