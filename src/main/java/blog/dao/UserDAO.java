package blog.dao;

import blog.models.User;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * author: erik
 */
public class UserDAO {
    private Random random = new SecureRandom();
    Logger logger = LogManager.getLogger(UserDAO.class.getName());
    org.hibernate.Session hibernateSession;

    public UserDAO(org.hibernate.Session session) {this.hibernateSession = session;}

    // validates that username is unique and insert into db
    public Boolean addUser(String username, String password, String email) {
        String passwordHash = makePasswordHash(password, Integer.toString(random.nextInt()));
        User user = new User(username, passwordHash, email);
        System.out.println(username+":"+password+":"+email);

        try {
            hibernateSession.getTransaction().begin();
            hibernateSession.persist(user);
            hibernateSession.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            logger.info("Username already in use: " + username);
            e.printStackTrace();
            return false;
        }
    }

    public User validateLogin(String username, String password) {
        User user = (User) hibernateSession.createCriteria(User.class).add(Restrictions.eq("username", username)).uniqueResult();
        if (user == null) {
            logger.info("User not in database");
            return null;
        }
        String hashedAndSalted = user.getPassword();
        logger.info("User found");

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
}
