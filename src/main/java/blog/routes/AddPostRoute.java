package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.logic.PostHandler;
import com.mongodb.DB;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;

import static spark.Spark.get;
import static spark.Spark.post;


public class AddPostRoute extends BaseRoute{
    Logger logger = LogManager.getLogger(AddPostRoute.class.getName());

    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;

    public AddPostRoute(final Configuration cfg, final DB blogDB) {
        this.cfg = cfg;
        postsDAO = new PostsDAO(blogDB);
        sessionDAO = new SessionDAO(blogDB);
    }

    public void initPage() throws IOException {
        get(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);
                logger.info(request.requestMethod()+" "+request.headers("Referer"));

                if (username == null) {
                    response.redirect("/login");
                } else {
                    SimpleHash root = new SimpleHash();
                    root.put("blogName", blogName);
                    template.process(root, writer);
                }

            }
        });

        post(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Referer"));
                String articleBody = request.queryParams("articleBody");
                try {
                    postsDAO.insertPost(PostHandler.preparePost(articleBody));
                    response.redirect("/");
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    response.cookie("postError", "Title should contain more than 5 characters", 10);
                    response.redirect("/addpost");
                }
            }
        });
    }

}
