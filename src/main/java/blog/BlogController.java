package blog;

import blog.routes.*;
import freemarker.template.Configuration;
import org.hibernate.Session;
import spark.Request;

import javax.servlet.http.Cookie;
import java.io.IOException;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.setPort;


public class BlogController {

    public static void main(String[] args) throws IOException {
        String extStaticFolder = System.getenv("BLOG_DIR");
        if (args.length == 0) {
            new BlogController("mongodb://localhost", extStaticFolder);
        } else {
            new BlogController(args[0], extStaticFolder);
        }
    }

    public BlogController(String mongoURIString, String extStaticFolder) throws IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Configuration cfg = createFreemarkerConfiguration();
        cfg.setDefaultEncoding("UTF-8");
        externalStaticFileLocation(extStaticFolder);
        setPort(8080);

        MainPageRoute mainPageRoute = new MainPageRoute(cfg, session);
        AddPostRoute addPostRoute = new AddPostRoute(cfg, session);
        LoginRoute loginRoute = new LoginRoute(cfg, session);
        CategoriesRoute categoriesRoute = new CategoriesRoute(cfg, session);
        EditPostRoute editPostRoute = new EditPostRoute(cfg, session);
        AddCommentRoute addCommentRoute = new AddCommentRoute(cfg, session);

        mainPageRoute.initMainPage();
        categoriesRoute.initPage();
        addPostRoute.initPage();
        loginRoute.initPage();
        editPostRoute.initPage();
        addCommentRoute.initRoute();
    }

    private Configuration createFreemarkerConfiguration(){
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(BlogController.class, "/freemarker");
        return retVal;
    }

    public static String getSessionCookie(final Request request) {
        if (request.raw().getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.raw().getCookies()) {
            if (cookie.getName().equals("session")) {
                return cookie.getValue();
            }
        }
        return null;
    }


}
