package blog.dao;

import blog.models.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.Set;

public class TagsDAO {
    private Session hibernateSession;
    public TagsDAO(Session session){
        this.hibernateSession = session;
    }
    private static Logger logger = LogManager.getLogger(TagsDAO.class.getName());

    public Set<Tag> createTags(String [] tagNames){
        Set<Tag> tagSet = new HashSet<Tag>();
        for (String tagName : tagNames){
            if (hibernateSession.createCriteria(Tag.class).add(Restrictions.eq("name", tagName)).uniqueResult() == null){
                Tag tag = new Tag(tagName);
                addTag(tag);
                tagSet.add(tag);
            } else {
                Tag tag = (Tag)hibernateSession.createCriteria(Tag.class).add(Restrictions.eq("name", tagName)).uniqueResult();
                tagSet.add(tag);
            }
        }
        return tagSet;
    }

    public Long addTag(Tag tag){
        hibernateSession.getTransaction().begin();
        Long id = (Long)hibernateSession.save(tag);
        hibernateSession.getTransaction().commit();
        return id;
    }

}
