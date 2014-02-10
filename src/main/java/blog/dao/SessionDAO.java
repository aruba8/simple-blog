package blog.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

/**
 * author: erik
 */
public class SessionDAO {
    private DBCollection sessionsCollection;

    public SessionDAO(final DB blogDB) {
        sessionsCollection = blogDB.getCollection("sessions");
    }

    public String findUserNameBySessionId(String sessionId) {
        DBObject session = getSession(sessionId);

        if (session == null) {
            return null;
        } else {
            return session.get("username").toString();
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

        // build the BSON object
        BasicDBObject session = new BasicDBObject("username", username);

        session.append("_id", sessionID);

        sessionsCollection.insert(session);

        return session.getString("_id");
    }

    // ends the session by deleting it from the sesisons table
    public void endSession(String sessionID) {
        sessionsCollection.remove(new BasicDBObject("_id", sessionID));
    }

    // retrieves the session from the sessions table
    public DBObject getSession(String sessionID) {
        return sessionsCollection.findOne(new BasicDBObject("_id", sessionID));
    }

}
