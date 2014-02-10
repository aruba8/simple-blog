package blog.routes;

import blog.BlogController;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.logic.Post;
import com.mongodb.DB;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.net.URLDecoder;

import static spark.Spark.get;
import static spark.Spark.post;


public class AddPostRoute {
    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;

    public AddPostRoute(final Configuration cfg, final DB blogDB) {
        this.cfg = cfg;
        postsDAO = new PostsDAO(blogDB);
        sessionDAO = new SessionDAO(blogDB);
    }

    public void initPage() throws IOException {
        get(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String cookie = BlogController.getSessionCookie(request);
                String username = sessionDAO.findUserNameBySessionId(cookie);

                if (username == null) {
                    response.redirect("/login");
                } else {
                    SimpleHash root = new SimpleHash();
                    template.process(root, writer);
                }

            }
        });

        post(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String articleBody = request.queryParams("articleBody");

                String article = URLDecoder.decode(StringEscapeUtils.escapeHtml4(articleBody), "UTF-8");
                String title = URLDecoder.decode(StringEscapeUtils.escapeHtml4(request.queryParams("title")), "UTF-8");
                String tags = URLDecoder.decode(StringEscapeUtils.escapeHtml4(request.queryParams("tags")), "UTF-8");

                Post post = new Post();

                post.setArticleBody(article);
                post.setTitle(title);
                post.setTagsString(tags);

                postsDAO.insertPost(post);

                response.redirect("/");


            }
        });
    }

}
