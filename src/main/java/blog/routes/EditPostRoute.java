package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.dao.TagsDAO;
import blog.logic.PostHandler;
import blog.models.Post;
import blog.models.Tag;
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


    public EditPostRoute(final Configuration cfg, final Session session){
        this.cfg = cfg;
        this.sessionDAO = new SessionDAO(session);
        this.postsDAO = new PostsDAO(session);
        this.tagsDAO = new TagsDAO(session);
    }

    public void initPage() throws IOException {
        get(new FreemarkerBasedRoute("/edit/:permalink", "editPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String permalink = URLDecoder.decode(request.params(":permalink"), "UTF-8");
                Post post = postsDAO.findByPermalink(permalink);

                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionString(cookie);
                if (username == null){
                    response.redirect("/login");
                } else if (post == null){
                    response.redirect("/page_not_found");
                } else {
                    SimpleHash root = new SimpleHash();
                    String [] tags = post.getTagNames();
                    Boolean isCommentsAvailable = true;
                    if (post.getIsCommentsAvailable() != null){
                        isCommentsAvailable = post.getIsCommentsAvailable();
                    }
                    if(tags != null && tags.length > 0){
                        String tagsStringRaw = "";
                        for(String tag : tags){
                            tagsStringRaw = tagsStringRaw+tag+", ";
                        }
                        String tagsString = tagsStringRaw.substring(0, tagsStringRaw.length()-2);
                        logger.info(tagsString);
                        root.put("tags", tagsString);
                    }
                    root.put("isCommentsAvailable", isCommentsAvailable.toString());
                    Map<String, Object> postMap = new HashMap<String, Object>();
                    postMap.put("id", post.getId());
                    postMap.put("title", post.getTitle());
                    postMap.put("articleBody", post.getArticleBody());
                    root.put("post", postMap);
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
                    //todo post doesn't work
                    Post post = PostHandler.createPost(articleBody);
                    String[] tagsArray = PostHandler.getTags(articleBody);
                    Set<Tag> tags = tagsDAO.createTags(tagsArray);
                    post.setTags(tags);

                    String postPermalink = postsDAO.updatePost(id, post);
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
