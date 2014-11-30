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
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.post;


public class AddPostRoute extends BaseRoute{
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
        get(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionString(cookie);
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));

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
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));
                String articleBody = request.queryParams("articleBody");
                String cookie = BlogController.getSessionCookie(request);
                User user = sessionDAO.getUserBySessionString(cookie);
                logger.debug(articleBody);
                try {
                    Post post = PostHandler.createPost(articleBody);
                    String[] tagsArray = PostHandler.getTags(articleBody);
                    Set<Tag> tags = tagsDAO.createTags(tagsArray);
                    post.setTags(tags);
                    post.setAuthor(user);
                    postsDAO.insertPost(post);
                    response.redirect("/");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    response.cookie("postError", "Title should contain more than 5 characters", 10);
                    response.redirect("/addpost");
                }
            }
        });
    }

}
