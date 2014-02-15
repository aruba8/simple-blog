package blog.routes;

import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.logic.PostsHandler;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public class CategoriesRoute {
    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;

    public CategoriesRoute(final Configuration cfg, final DB blogDB){
        this.cfg = cfg;
        this.postsDAO = new PostsDAO(blogDB);
        this.sessionDAO = new SessionDAO(blogDB);
    }

    public void initPage()throws IOException{
        get(new FreemarkerBasedRoute("/category", "category.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String tag = request.queryParams("c");

                DBCursor postsCursor = postsDAO.findPostsByTagDesc(tag, 10);
                List<Map> posts = PostsHandler.getPostsList(postsCursor);
                SimpleHash root = new SimpleHash();

                root.put("posts", posts);
                root.put("category", tag);
                template.process(root, writer);
            }
        });
    }

}