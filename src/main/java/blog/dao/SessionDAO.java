package blog.dao;

import com.mongodb.DBCollection;
import org.apache.commons.codec.binary.Base64;
import org.mongodb.morphia.Datastore;

import blog.models.Session;
import java.security.SecureRandom;

/**
 * author: erik
 */
public class SessionDAO {
    private DBCollection sessionsCollection;
    private Datastore ds;

    public SessionDAO(final Datastore ds) {
        this.ds = ds;
    }

    public String findUserNameBySessionId(String sessionId) {
        Session session = getSession(sessionId);

        if (session == null) {
            return null;
        } else {
            return session.getUsername();
        }
    }


    // starts a new session in the sessions table
    public String startSession(String username) {

        // get 32 byte random number. that's a lot of bits.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);

        Base64 encoder = new Base64();

        String sessionID = encoder.encodeToString(randomBytes);

        Session session = new Session();

        // build the BSON object
//        BasicDBObject session = new BasicDBObject("username", username);
        session.setUsername(username);

        session.setId(sessionID);

//        sessionsCollection.insert(session);
        ds.save(session);

        return session.getId();
    }

    // ends the session by deleting it from the sesisons table
    public void endSession(String sessionID) {
//        sessionsCollection.remove(new BasicDBObject("_id", sessionID));
        ds.delete(ds.createQuery(Session.class).field("id").equal(sessionID));
    }

    // retrieves the session from the sessions table
    public Session getSession(String sessionID) {
//        return sessionsCollection.findOne(new BasicDBObject("_id", sessionID));
        return ds.find(Session.class).field("id").equal(sessionID).get();
    }

}
