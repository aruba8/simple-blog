package blog.routes;

import blog.BlogController;
import blog.dao.SessionDAO;
import blog.dao.UserDAO;
import com.mongodb.DB;
import com.mongodb.DBObject;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.Writer;

import static spark.Spark.get;
import static spark.Spark.post;


public class LoginRoute {
    private Configuration cfg;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    Logger logger = LogManager.getLogger(LoginRoute.class.getName());

    public LoginRoute(final Configuration cfg, final DB blogDB){
        this.cfg = cfg;
        this.userDAO = new UserDAO(blogDB);
        this.sessionDAO = new SessionDAO(blogDB);
    }

    public void initPage() throws IOException{
        get(new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);
                Boolean isAdmin = userDAO.isAdminByUsername(username);
                System.out.println(isAdmin);
                if (isAdmin) {
                    root.put("authorized", "true");
                } else {
                    root.put("authorized", "false");
                }


                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                String logoutParam = request.queryParams("logout");

                logger.trace("Login: User submitted: " + username + "  " + password);
                DBObject user = userDAO.validateLogin(username, password);
                if (user != null) {
                    // valid user, let's log them in
                    String sessionID = sessionDAO.startSession(user.get("username").toString());

                    if (sessionID == null) {
                        response.redirect("/internal_error");
                    } else {
                        // set the cookie for the user's browser
                        Cookie cookie = new Cookie("session", sessionID);
                        cookie.setMaxAge(14400);
                        response.raw().addCookie(cookie);
                        response.redirect("/addpost");
                    }
                } else {
                    SimpleHash root = new SimpleHash();

                    root.put("username", StringEscapeUtils.escapeHtml4(username));
                    root.put("password", "");
                    root.put("login_error", "error");
                    template.process(root, writer);
                }
            }
        });

        post(new FreemarkerBasedRoute("/logout", "logout.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String sessionId = BlogController.getSessionCookie(request);
                if (sessionId == null) {
                    response.redirect("/");
                } else {
                    sessionDAO.endSession(sessionId);

                    Cookie c = getSessionCookieActual(request);
                    c.setMaxAge(0);
                    response.raw().addCookie(c);
                    response.redirect("/");
                }

            }
        });
    }

    // helper function to get session cookie as string
    private Cookie getSessionCookieActual(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie;
            }
        }
        return null;
    }


}
