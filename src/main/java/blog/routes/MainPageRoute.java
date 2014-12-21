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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.ModelAndView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;

public class MainPageRoute extends BaseRoute {

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
        get("/", (request, response) -> {
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
            String pageParam = request.queryMap().get("page").value();
            int pageNumber = 1;
            if (pageParam != null){
                pageNumber = Integer.parseInt(pageParam);
            }
            int pageSize = 5;
            logger.debug("pageParam :"+pageParam);

            List<Post> posts = postsDAO.findPostsByDescending(pageSize, (pageNumber-1)*pageSize);
            SimpleHash root = new SimpleHash();
            root.put("pageNumber", pageNumber);
            root.put("blogName", blogName);
            root.put("posts", PostsHandler.getPostsList(posts));
            return new ModelAndView(root, "index.ftl");
        }, new FreemarkerTemplateEngine(cfg));

        get("/post/:permalink", (request, response) -> {
            String permalink = null;
            try {
                permalink = URLDecoder.decode(request.params(":permalink"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Post postObject = postsDAO.findByPermalink(permalink);
            String cookie = BlogController.getSessionCookie(request);
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
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
                return new ModelAndView(root, "blog_entity.ftl");
            }
            return null;
        }, new FreemarkerTemplateEngine(cfg));
    }
}
