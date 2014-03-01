package blog.routes;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * author: erik
 */
public abstract class FreemarkerBasedRoute extends Route {
    final Template template;
    /**
     * Constructor
     *
     * @param path The route path which is used for matching. (e.g. /hello, users/:name)
     */
    protected FreemarkerBasedRoute(final String path, final String templateName, final Configuration cfg) throws IOException {
        super(path);
        template = cfg.getTemplate(templateName);
    }

    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            response.header("Transfer-Encoding","chunked");
            response.header("Content-Type", "text/html; charset=UTF-8");
            doHandle(request, response, writer);

        } catch (Exception e) {
            e.printStackTrace();
            response.redirect("/internal_error");
        }
        return writer;
    }

    protected abstract void doHandle(final Request request, final Response response, final Writer writer)
            throws IOException, TemplateException;

}
