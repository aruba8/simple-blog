package blog.routes;

import blog.logic.ConfigParser;

public class BaseRoute {
    ConfigParser configParser = new ConfigParser();
    String blogName = configParser.getBlogName();
}
