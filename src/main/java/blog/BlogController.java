package blog;

import blog.routes.AddPostRoute;
import blog.routes.MainPageRoute;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import freemarker.template.Configuration;
import spark.Request;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.UnknownHostException;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.setPort;


public class BlogController {
    private Configuration cfg;
    private MainPageRoute mainPageRoute;
    private AddPostRoute addPostRoute;

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
        cfg = createFreemarkerConfiguration();
        cfg.setDefaultEncoding("UTF-8");
        externalStaticFileLocation(extStaticFolder);
        setPort(8080);

        mainPageRoute = new MainPageRoute(cfg, blogDB);
        addPostRoute = new AddPostRoute(cfg, blogDB);

        mainPageRoute.initMainPage();
        addPostRoute.initPage();

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
