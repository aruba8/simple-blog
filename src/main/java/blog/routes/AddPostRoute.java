package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.dao.TagsDAO;
import blog.logic.PostHandler;
import blog.models.Post;
import blog.models.Tag;
import blog.models.User;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.ModelAndView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.post;


public class AddPostRoute extends BaseRoute {
    Logger logger = LogManager.getLogger(AddPostRoute.class.getName());

    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;
    private TagsDAO tagsDAO;

    public AddPostRoute(final Configuration cfg, final Session session) {
        this.cfg = cfg;
        postsDAO = new PostsDAO(session);
        sessionDAO = new SessionDAO(session);
        tagsDAO = new TagsDAO(session);
    }

    public void initPage() throws IOException {
        get("/addpost", (request, response) -> {
            String cookie = BlogController.getSessionCookie(request);
            String username = sessionDAO.findUserNameBySessionString(cookie);
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
            if (username == null) {
                response.redirect("/login");
                return null;
            } else {
                SimpleHash root = new SimpleHash();
                root.put("blogName", blogName);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                root.put("dateTime", simpleDateFormat.format(new Date()));
                return new ModelAndView(root, "addPost.ftl");
            }
        }, new FreemarkerTemplateEngine(cfg));

        post("/addpost", (request, response) -> {
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
            String articleBody = request.queryParams("articleBody");
            String cookie = BlogController.getSessionCookie(request);
            User user = sessionDAO.getUserBySessionString(cookie);
            logger.debug(articleBody);
            Long post_id = null;
            try {
                Post post = PostHandler.createPost(articleBody);
                String[] tagsArray = PostHandler.getTags(articleBody);
                Set<Tag> tags = tagsDAO.createTags(tagsArray);
                post.setTags(tags);
                post.setAuthor(user);
                post_id = postsDAO.insertPost(post);
                response.redirect("/");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                response.redirect("/addpost");
            }
            return post_id;
        });
    }
}
