package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.dao.UserDAO;
import blog.logic.PostHandler;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBObject;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

import static spark.Spark.get;
import static spark.Spark.post;


public class EditPostRoute extends BaseRoute {
    private Configuration cfg;
    private SessionDAO sessionDAO;
    private UserDAO userDAO;
    private PostsDAO postsDAO;
    Logger logger = LogManager.getLogger(MainPageRoute.class.getName());


    public EditPostRoute(final Configuration cfg, final DB blogDB){
        this.cfg = cfg;
        this.sessionDAO = new SessionDAO(blogDB);
        this.userDAO = new UserDAO(blogDB);
        this.postsDAO = new PostsDAO(blogDB);
    }

    public void initPage() throws IOException {
        get(new FreemarkerBasedRoute("/edit/:permalink", "editPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String permalink = URLDecoder.decode(request.params(":permalink"), "UTF-8");
                DBObject post = postsDAO.findByPermalink(permalink);

                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);
                Boolean isAdmin = userDAO.isAdminByUsername(username);
                if (username == null){
                    response.redirect("/login");
                } else if (!isAdmin){
                    response.redirect("/");
                } else if (post == null){
                    response.redirect("/page_not_found");
                } else {
                    SimpleHash root = new SimpleHash();
                    BasicDBList tags = (BasicDBList) post.get("tags");
                    Boolean isCommentsAvailable = true;
                    if (post.containsField("isCommentsAvailable")){
                        isCommentsAvailable = (Boolean) post.get("isCommentsAvailable");
                    }
                    if(tags != null){
                        String tagsStringRaw = "";
                        for(Object tag : tags){
                            tagsStringRaw = tagsStringRaw+tag+", ";
                        }
                        String tagsString = tagsStringRaw.substring(0, tagsStringRaw.length()-2);
                        logger.info(tagsString);
                        root.put("tags", tagsString);
                    }
                    root.put("isCommentsAvailable", isCommentsAvailable.toString());
                    root.put("post", post);
                    template.process(root, writer);
                }
            }
        });

        post(new FreemarkerBasedRoute("/edit", "editPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                logger.info(request.requestMethod().toUpperCase()+" "+request.headers("Host")+" "+request.headers("User-Agent"));
                String articleBody = request.queryParams("articleBody");
                logger.info(articleBody);
                try {
                    String id = request.queryParams("id");
                    logger.info(id);
                    String postPermalink = postsDAO.updatePost(id, PostHandler.preparePost(articleBody));
                    logger.info(postPermalink);
                    response.redirect("/post/"+postPermalink);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    response.cookie("postError", "Title should contain more than 5 characters", 10);
                    response.redirect("/addpost");
                }
            }
        });



    }

}
