package blog.logic;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostHandler {
    private static Logger logger = LogManager.getLogger(PostHandler.class.getName());

    public static Map<String, Object> getPost(DBObject postObject) {
        Date dateTime = (Date) postObject.get("dateTime");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        BasicDBList tags = (BasicDBList) postObject.get("tags");
        Boolean isCommentsAvailable = true;
        if (postObject.containsField("isCommentsAvailable")){
            isCommentsAvailable = (Boolean)postObject.get("isCommentsAvailable");
        }
        Map<String, Object> postEntity = new HashMap<String, Object>();
        try {
            String htmlArticle = new Markdown4jProcessor().process((String) postObject.get("articleBody"));

            postEntity.put("dateTime", sdf.format(dateTime));
            postEntity.put("title", postObject.get("title"));
            postEntity.put("articleBody", htmlArticle);
            postEntity.put("permalink", postObject.get("permalink"));
            postEntity.put("articlePreview", createArticlePreview((String) postObject.get("articleBody")));
            postEntity.put("isCommentsAvailable", isCommentsAvailable);
            if(tags != null){
                postEntity.put("tags", tags.toArray());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return postEntity;
    }

    private static String createArticlePreview(String article) throws IOException {
        if (article.length() > 600) {
            return new Markdown4jProcessor().process(article.substring(0, 600));
        } else {
            return new Markdown4jProcessor().process(article);
        }
    }


    public static Post preparePost2(String rawPost) throws Exception {
        String splitRawPost[] = rawPost.split("~~~~~~~~~~~~~~");
        String head = splitRawPost[0];
        head = head.replaceAll("\n|\r\n", "");
        String body = splitRawPost[1];
        String[] splitHead = head.split("Categories:");
        String title = splitHead[0].substring(6, splitHead[0].length());
        if (title.length() < 5){
            throw new Exception("Title is less than 5 characters");
        }
        String [] categories = null;
        if (splitHead.length > 1) {
            String categoriesString = splitHead[1];
            categoriesString = categoriesString.trim();
            categories = categoriesString.split(", ");
        }

        Post post = new Post();
        post.setArticleBody(body.trim());
        post.setTitle(title.trim());
        post.setTags(categories);
        logger.debug(post.getArticleBody());
        return post;
    }

    public static Post preparePost(String rawPost){
        String splitRawPost [] = rawPost.split("~~~~~~~~~~~~~~");
        String head = splitRawPost[0];
        String body = splitRawPost[1];
        String title = "";
        String categories;
        String isCommentsAvailable = "";
        String [] categoriesArray = null;

        String splitHead [] = head.split("\n|\r\n");
        for (String aSplitHead : splitHead) {
            if (aSplitHead.startsWith("Title:")) {
                title = aSplitHead.replace("Title:", "").trim();
            } else if (aSplitHead.startsWith("Categories")) {
                categories = aSplitHead.replace("Categories:", "").trim().replaceAll("\\s", "");
                categoriesArray = categories.split(",");
            } else if (aSplitHead.startsWith("Comments:")) {
                isCommentsAvailable = aSplitHead.replace("Comments:", "").toLowerCase().trim();
            }
        }
        Post post = new Post();
        post.setTitle(title);
        post.setArticleBody(body);
        post.setIsCommentsAvailable(Boolean.parseBoolean(isCommentsAvailable));
        post.setTags(categoriesArray);
        return post;
    }
}
