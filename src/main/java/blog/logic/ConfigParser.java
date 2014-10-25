package blog.logic;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ConfigParser {
    private String blogName;
    private String disqusShortName;
    private String [] pages;
    private Integer sessionMaxAge;

    public ConfigParser(){
        Yaml rawYaml = new Yaml();
        Map yaml;
        try {
            yaml = (Map)rawYaml.load(new FileInputStream(new File("config.yml")));
            this.blogName  = (String) yaml.get("java.blog-name");
            this.disqusShortName = (String) yaml.get("disqus-short-name");
            Map navbar = (Map)yaml.get("navbar");
            Map settings = (Map) yaml.get("settings");
            this.sessionMaxAge = (Integer) settings.get("session-max-age");
            String rawPages = (String)navbar.get("pages");
            this.pages = rawPages.split(",");

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

    public String[] getPages() {
        return pages;
    }

    public Integer getSessionMaxAge(){ return sessionMaxAge; }
}
