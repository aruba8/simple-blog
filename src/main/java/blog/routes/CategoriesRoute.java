package blog.routes;

import blog.dao.PostsDAO;
import blog.logic.PostsHandler;
import blog.models.Post;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Datastore;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public class CategoriesRoute extends BaseRoute{
    Logger logger = LogManager.getLogger(CategoriesRoute.class.getName());
    private Configuration cfg;
    private PostsDAO postsDAO;
    private Datastore ds;

    public CategoriesRoute(final Configuration cfg, final Datastore ds){
        this.cfg = cfg;
        this.postsDAO = new PostsDAO(ds);
    }

    public void initPage()throws IOException{
        get(new FreemarkerBasedRoute("/category", "category.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));
                String tag = request.queryParams("c");

                List<Post> postsCursor = postsDAO.findPostsByTagDesc(tag, 10);
                List<Map> posts = PostsHandler.getPostsList(postsCursor);
                SimpleHash root = new SimpleHash();

                root.put("blogName", blogName);
                root.put("posts", posts);
                root.put("category", tag);
                template.process(root, writer);
            }
        });
    }

}
