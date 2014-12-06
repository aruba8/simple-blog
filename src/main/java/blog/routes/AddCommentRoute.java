package blog.routes;

import blog.BlogController;
import blog.dao.CommentDAO;
import blog.dao.PostsDAO;
import blog.dao.SessionDAO;
import blog.models.Comment;
import blog.models.Post;
import blog.models.User;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import static spark.Spark.post;




public class AddCommentRoute {
    Logger logger = LogManager.getLogger(AddCommentRoute.class.getName());

    private Configuration cfg;
    private PostsDAO postsDAO;
    private SessionDAO sessionDAO;
    private CommentDAO commentDAO;

    public AddCommentRoute(final Configuration cfg, final Session session){
        this.cfg = cfg;
        this.postsDAO = new PostsDAO(session);
        this.sessionDAO = new SessionDAO(session);
        this.commentDAO = new CommentDAO(session);
    }

    public void initRoute() throws IOException {
        post(new FreemarkerBasedRoute("/addcomment", "comment_form.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer) throws IOException, TemplateException {
                String message = request.queryParams("commentBody");
                String cookie = BlogController.getSessionCookie(request);
                User user = sessionDAO.getUserBySessionString(cookie);
                Long post_id = Long.parseLong(request.queryParams("post_id"));
                Post post = postsDAO.findPostById(post_id);
                Comment comment = new Comment(message, post, user, new Date());
                commentDAO.insertComment(comment);
                response.redirect("/post/"+post.getPermalink());
            }
        });

    }


}
