package blog.dao;

import blog.models.User;
import com.mongodb.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Datastore;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * author: erik
 */
public class UserDAO {
//    private DBCollection usersCollection = null;
    private Datastore ds;

    private Random random = new SecureRandom();

    Logger logger = LogManager.getLogger(UserDAO.class.getName());

    public UserDAO(Datastore ds) {this.ds = ds;}

    // validates that username is unique and insert into db
    public boolean addUser(String username, String password, String email, Boolean isAdmin) {

        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));

        User user_m = new User();

        user_m.setUsername(username);
        user_m.setPassword(passwordHash);
        user_m.setIsAdmin(isAdmin);

        if (email != null && !email.equals("")) {
            // the provided email address
            user_m.setEmail(email);
        }

        try {
            this.ds.save(user_m);
            return true;
        } catch (MongoException.DuplicateKey e) {
            logger.info("Username already in use: " + username);
            return false;
        }
    }

    public User validateLogin(String username, String password) {
        User user;

        user = ds.find(User.class).field("username").equal(username).get();

        if (user == null) {
            logger.info("User not in database");
            return null;
        }

        String hashedAndSalted = user.getPassword();

        String salt = hashedAndSalted.split(",")[1];

        if (!hashedAndSalted.equals(makePasswordHash(password, salt))) {
            logger.info("Submitted password is not a match");
            return null;
        }

        return user;
    }


    private String makePasswordHash(String password, String salt) {
        try {
            String saltedAndHashed = password + "," + salt;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(saltedAndHashed.getBytes());
            Base64 encoder = new Base64();
            byte hashedBytes[] = (new String(digest.digest(), "UTF-8")).getBytes();
            return encoder.encodeToString(hashedBytes) + "," + salt;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 is not available", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 unavailable?  Not a chance", e);
        }
    }

    public String getUserIdByUsername(String username) {
        return null;
    }

    public Boolean isAdminByUsername(String username) {
        User user = ds.find(User.class).field("username").equal(username).get();
        if (user == null) {
            return false;
        } else {
            return user.getIsAdmin();
        }
    }
}
