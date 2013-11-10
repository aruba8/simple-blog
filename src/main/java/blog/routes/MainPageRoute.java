package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.logic.PostHandler;
import blog.logic.PostsHandler;
import com.mongodb.DB;
import com.mongodb.DBObject;
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

public class MainPageRoute {

    private Configuration cfg;
    private SessionDAO sessionDAO;
    private PostsDAO postsDAO;

    public MainPageRoute(final Configuration cfg, final DB blogDB){
        this.cfg = cfg;
        this.sessionDAO = new SessionDAO(blogDB);
        this.postsDAO = new PostsDAO(blogDB);
    }


    public void initMainPage() throws IOException{
        get(new FreemarkerBasedRoute("/", "index.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                String username = sessionDAO.findUserNameBySessionId(BlogController.getSessionCookie(request));
                List<DBObject> posts = postsDAO.findPostsByDescending(10);
                PostsHandler postsHandler = new PostsHandler(posts);
                if (posts != null){
                    root.put("posts", postsHandler.getPostsList());
                }else {
                    response.redirect("/internal_error");
                }
                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/post/:permalink", "blog_entity.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String permalink = request.params(":permalink");
                DBObject postDBObject = postsDAO.findByPermalink(permalink);
                if(postDBObject == null){
                    response.redirect("/post_not_found");
                } else {
                    SimpleHash root = new SimpleHash();
                    PostHandler postHandler = new PostHandler(postDBObject);
                    Map post =  postHandler.getPost();
                    root.put("post", post);
                    template.process(root, writer);
                }


                System.out.println(permalink);

            }
        });


    }

}
