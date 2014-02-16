package blog.logic;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ConfigParser {

    private String blogName;


    private String disqusShortName;


    public ConfigParser(){
        Yaml rawYaml = new Yaml();
        Map yaml;
        try {
            yaml = (Map)rawYaml.load(new FileInputStream(new File("config.yml")));
            this.blogName = (String) yaml.get("blog-name");
            this.disqusShortName = (String) yaml.get("disqus-short-name");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getDisqusShortName() {
        return disqusShortName;
    }

    public String getBlogName() {
        return blogName;
    }
}
