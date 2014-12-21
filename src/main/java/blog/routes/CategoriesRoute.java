package blog.routes;

import blog.dao.PostsDAO;
import blog.logic.PostsHandler;
import blog.models.Post;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public class CategoriesRoute extends BaseRoute {
    Logger logger = LogManager.getLogger(CategoriesRoute.class.getName());
    private Configuration cfg;
    private PostsDAO postsDAO;

    public CategoriesRoute(final Configuration cfg, final Session session) {
        this.cfg = cfg;
        this.postsDAO = new PostsDAO(session);
    }

    public void initPage() throws IOException {
        get("/category", (request, response) -> {
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
            String tag = request.queryParams("c");

            List<Post> postsCursor = postsDAO.findPostsByTagDesc(tag, 10);
            List<Map> posts = PostsHandler.getPostsList(postsCursor);
            SimpleHash root = new SimpleHash();

            root.put("blogName", blogName);
            root.put("posts", posts);
            root.put("category", tag);
            return new ModelAndView(root, "category.ftl");
        }, new FreemarkerTemplateEngine(cfg));
    }
}
