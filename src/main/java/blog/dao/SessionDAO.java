package blog.dao;

import blog.models.Session;
import blog.models.User;
import org.apache.commons.codec.binary.Base64;
import org.hibernate.criterion.Restrictions;

import java.security.SecureRandom;

/**
 * author: erik
 */
public class SessionDAO {
    private org.hibernate.Session hibernateSession;

    public SessionDAO(org.hibernate.Session session) {
        this.hibernateSession = session;
    }

    public String findUserNameBySessionString(String sessionString) {
        Session session = getSession(sessionString);

        if (session == null) {
            return null;
        } else {
            return session.getUsername();
        }
    }

    public User getUserBySessionString(String sessionString){
        Session session = getSession(sessionString);
        if (session == null){
            return null;
        } else {
            return (User) hibernateSession.createCriteria(User.class).add(Restrictions.eq("username", session.getUsername())).uniqueResult();
        }
    }


    // starts a new session in the sessions table
    public String startSession(String username) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);
        Session session = new Session();

        Base64 encoder = new Base64();
        String sessionString = encoder.encodeToString(randomBytes);

        // build the BSON object
//        BasicDBObject session = new BasicDBObject("username", username);
        session.setUsername(username);
        session.setSessionString(sessionString);


//        sessionsCollection.insert(session);
        hibernateSession.getTransaction().begin();
        hibernateSession.save(session);
        hibernateSession.getTransaction().commit();

        return session.getSessionString();
    }

    // ends the session by deleting it from the sesisons table
    public void endSession(String sessionString) {
        hibernateSession.getTransaction().begin();
        hibernateSession.delete(getSession(sessionString));
        hibernateSession.getTransaction().commit();
    }

    // retrieves the session from the sessions table
    public Session getSession(String sessionString) {
        return (Session) hibernateSession.createCriteria(Session.class).add(Restrictions.eq("sessionString", sessionString)).uniqueResult();
    }

}
