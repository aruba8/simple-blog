package blog.routes;

import blog.dao.PostsDAO;
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

import static spark.Spark.get;
import static spark.Spark.post;


public class AddPostRoute {
    private Configuration cfg;
    private PostsDAO postsDAO;

    public AddPostRoute(final Configuration cfg, final DB blogDB){
        this.cfg = cfg;
        postsDAO = new PostsDAO(blogDB);
    }

    public void initPage() throws IOException {
        get(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();

                template.process(root, writer);
            }
        });

        post(new FreemarkerBasedRoute("/addpost", "addPost.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String articleBody = request.queryParams("articleBody");

                String article = StringEscapeUtils.escapeHtml4(articleBody);
                String title = StringEscapeUtils.escapeHtml4(request.queryParams("title"));
                String tags = StringEscapeUtils.escapeHtml4(request.queryParams("tags"));

                Post post = new Post();

                post.setArticleBody(article);
                post.setTitle(title);
                post.setTagsString(tags);

                postsDAO.insertPost(post);

                System.out.println(title);
                System.out.println(article);
                System.out.println(tags);
                response.redirect("/");


            }
        });
    }

}
