package blog.routes;

import blog.BlogController;
import blog.dao.CommentDAO;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.logic.CommentHandler;
import blog.logic.PostHandler;
import blog.logic.PostsHandler;
import blog.models.Comment;
import blog.models.Post;
import blog.models.User;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public class MainPageRoute extends BaseRoute{

    Logger logger = LogManager.getLogger(MainPageRoute.class.getName());

    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;
    private CommentDAO commentDAO;

    public MainPageRoute(final Configuration cfg, final Session session) {
        this.cfg = cfg;
        this.postsDAO = new PostsDAO(session);
        this.sessionDAO = new SessionDAO(session);
        this.commentDAO = new CommentDAO(session);
    }


    public void initMainPage() throws IOException {
        get(new FreemarkerBasedRoute("/", "index.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));
                List<Post> posts = postsDAO.findPostsByDescending(10);
                SimpleHash root = new SimpleHash();
                root.put("blogName", blogName);
                root.put("posts", PostsHandler.getPostsList(posts));
                template.process(root, writer);
            }
        });

        get(new FreemarkerBasedRoute("/post/:permalink", "blog_entity.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String permalink = URLDecoder.decode(request.params(":permalink"), "UTF-8");
                Post postObject = postsDAO.findByPermalink(permalink);
                String cookie = BlogController.getSessionCookie(request);
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));
                User user = sessionDAO.getUserBySessionString(cookie);
                if (postObject == null) {
                    response.redirect("/post_not_found");
                } else {
                    SimpleHash root = new SimpleHash();
                    root.put("currentUser", user);
                    root.put("diqusShortName", configParser.getDisqusShortName());
                    root.put("blogName", blogName);
                    List<Comment> commentList = commentDAO.getCommentsByPostId(postObject.getId());
                    Map post = PostHandler.getPost(postObject);
                    root.put("comments", CommentHandler.getComments(commentList));
                    root.put("post", post);
                    template.process(root, writer);
                }
            }
        });
    }
}
