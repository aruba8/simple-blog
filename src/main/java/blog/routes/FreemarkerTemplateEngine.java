package blog.routes;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.ModelAndView;
import spark.TemplateEngine;

import java.io.IOException;
import java.io.StringWriter;

public class FreemarkerTemplateEngine extends TemplateEngine {
    final Configuration configuration;

    public FreemarkerTemplateEngine(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String render(ModelAndView modelAndView) {
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = configuration.getTemplate(modelAndView.getViewName());
            template.process(modelAndView.getModel(), stringWriter);
            return stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        } catch (TemplateException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }
}
