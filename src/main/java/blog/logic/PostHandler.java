package blog.logic;

import blog.models.Post;
import blog.models.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PostHandler {
    private static Logger logger = LogManager.getLogger(PostHandler.class.getName());

    public static Map<String, Object> getPost(Post post) {
        Date dateTime = post.getDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        Set<Tag> tags = post.getTags();
        Boolean isCommentsAvailable = true;
        if (post.getIsCommentsAvailable() != null){
            isCommentsAvailable = post.getIsCommentsAvailable();
        }
        Map<String, Object> postMap = new HashMap<String, Object>();
        try {
            String htmlArticle = new Markdown4jProcessor().process(post.getArticleBody());

            postMap.put("dateTime", sdf.format(dateTime));
            postMap.put("title", post.getTitle());
            postMap.put("articleBody", htmlArticle);
            postMap.put("permalink", post.getPermalink());
            postMap.put("articlePreview", createArticlePreview(post.getArticleBody()));
            postMap.put("isCommentsAvailable", isCommentsAvailable);
            if(tags != null){
                postMap.put("tags", tags);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return postMap;
    }

    private static String createArticlePreview(String article) throws IOException {
        if (article.length() > 600) {
            return new Markdown4jProcessor().process(article.substring(0, 600));
        } else {
            return new Markdown4jProcessor().process(article);
        }
    }

    public static Post createPost(String rawPost){
        String splitRawPost [] = rawPost.split("~~~~~~~~~~~~~~");
        String head = splitRawPost[0];
        String body = splitRawPost[1];
        String title = "";
        String isCommentsAvailable = "";
        String splitHead [] = head.split("\n|\r\n");
        for (String aSplitHead : splitHead) {
            if (aSplitHead.startsWith("Title:")) {
                title = aSplitHead.replace("Title:", "").trim();
            } else if (aSplitHead.startsWith("Comments:")) {
                isCommentsAvailable = aSplitHead.replace("Comments:", "").toLowerCase().trim();
            }
        }
        Post post = new Post();
        post.setDateTime(new Date());
        post.setTitle(title);
        post.setArticleBody(body);
        post.setIsCommentsAvailable(Boolean.parseBoolean(isCommentsAvailable));
        post.setPermalink(createPermalink(title));
        return post;
    }

    public static String[] getTags(String rawPost){
        String splitRawPost [] = rawPost.split("~~~~~~~~~~~~~~");
        String head = splitRawPost[0];
        String categories;
        String splitHead [] = head.split("\n|\r\n");
        String [] categoriesArray = null;
        for (String aSplitHead : splitHead) {
            if (aSplitHead.startsWith("Categories")) {
                categories = aSplitHead.replace("Categories:", "").trim().replaceAll("\\s", "");
                categoriesArray = categories.split(",");
            }
        }
        return categoriesArray;
    }

    public static String createPermalink(String title){
        String titleInTranslit = Translit.toTranslit(title);
        String permalink = titleInTranslit.replaceAll("\\s", "-");
        permalink = permalink.replaceAll("\\W", "-");
        permalink = permalink.toLowerCase();
        if (permalink.length() > 60) {
            return permalink.substring(0, 60);
        } else {
            return permalink;
        }
    }

}
