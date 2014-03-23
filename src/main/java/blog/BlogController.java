package blog;

import blog.routes.*;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import freemarker.template.Configuration;
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
        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final DB blogDB = mongoClient.getDB("blog");
        Configuration cfg = createFreemarkerConfiguration();
        cfg.setDefaultEncoding("UTF-8");
        externalStaticFileLocation(extStaticFolder);
        setPort(8080);

        MainPageRoute mainPageRoute = new MainPageRoute(cfg, blogDB);
        AddPostRoute addPostRoute = new AddPostRoute(cfg, blogDB);
        LoginRoute loginRoute = new LoginRoute(cfg, blogDB);
        CategoriesRoute categoriesRoute = new CategoriesRoute(cfg, blogDB);
        EditPostRoute editPostRoute = new EditPostRoute(cfg, blogDB);

        mainPageRoute.initMainPage();
        categoriesRoute.initPage();
        addPostRoute.initPage();
        loginRoute.initPage();
        editPostRoute.initPage();

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
