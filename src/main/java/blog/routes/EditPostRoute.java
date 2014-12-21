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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static spark.Spark.get;
import static spark.Spark.post;


public class EditPostRoute extends BaseRoute {
    private Configuration cfg;
    private SessionDAO sessionDAO;
    private PostsDAO postsDAO;
    private TagsDAO tagsDAO;
    Logger logger = LogManager.getLogger(MainPageRoute.class.getName());
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");


    public EditPostRoute(final Configuration cfg, final Session session) {
        this.cfg = cfg;
        this.sessionDAO = new SessionDAO(session);
        this.postsDAO = new PostsDAO(session);
        this.tagsDAO = new TagsDAO(session);
    }

    public void initPage() throws IOException {
        get("/edit/:permalink", (request, response) -> {
            String permalink = "";
            try {
                permalink = URLDecoder.decode(request.params(":permalink"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Post post = postsDAO.findByPermalink(permalink);

            String cookie = BlogController.getSessionCookie(request);
            String username = sessionDAO.findUserNameBySessionString(cookie);
            User user = sessionDAO.getUserBySessionString(cookie);
            if (post.getAuthor().getId() != user.getId()) {
                response.status(403);
                response.redirect("/forbidden");
            }
            if (username == null) {
                response.redirect("/login");
                return null;
            }
            if (post == null) {
                response.redirect("/page_not_found");
                return null;
            }
            SimpleHash root = new SimpleHash();
            String[] tags = post.getTagNames();
            Boolean isCommentsAvailable = true;
            if (post.getIsCommentsAvailable() != null) {
                isCommentsAvailable = post.getIsCommentsAvailable();
            }
            if (tags != null && tags.length > 0) {
                String tagsStringRaw = "";
                for (String tag : tags) {
                    tagsStringRaw = tagsStringRaw + tag + ", ";
                }
                String tagsString = tagsStringRaw.substring(0, tagsStringRaw.length() - 2);
                root.put("tags", tagsString);
            }
            root.put("isCommentsAvailable", isCommentsAvailable.toString());
            Map<String, Object> postMap = new HashMap<String, Object>();
            postMap.put("id", post.getId());
            postMap.put("title", post.getTitle());
            postMap.put("articleBody", post.getArticleBody());
            postMap.put("dateTime", dateFormat.format(post.getDateTime()));
            root.put("post", postMap);

            return new ModelAndView(root, "editPost.ftl");
        }, new FreemarkerTemplateEngine(cfg));

        post("/edit", (request, response) -> {
            logger.info(request.requestMethod().toUpperCase() + " " + request.headers("Host") + " " + request.headers("User-Agent"));
            String articleBody = request.queryParams("articleBody");
            String cookie = BlogController.getSessionCookie(request);
            User user = sessionDAO.getUserBySessionString(cookie);
            String id = request.queryParams("id");
            Post postToUpdate = postsDAO.findPostById(Long.parseLong(id));
            if (user.getId() != postToUpdate.getAuthor().getId()) {
                response.redirect("/page_not_found");
            }
            logger.info(articleBody);
            try {
                Post post = PostHandler.createPost(articleBody);
                String[] tagsArray = PostHandler.getTags(articleBody);
                Set<Tag> tags = tagsDAO.createTags(tagsArray);
                post.setTags(tags);

                String postPermalink = postsDAO.updatePost(id, post);
                logger.info(postPermalink);
                response.redirect("/post/" + postPermalink);
            } catch (Exception e) {
                logger.error(e.getMessage());
                response.cookie("postError", "Title should contain more than 5 characters", 10);
                response.redirect("/addpost");
            }
            return id;
        });
    }
}
