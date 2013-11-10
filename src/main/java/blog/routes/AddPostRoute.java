package blog.routes;

import blog.dao.PostsDAO;
import com.mongodb.DB;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
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


                System.out.println(request.queryMap());
                response.redirect("/");


            }
        });
    }

}
