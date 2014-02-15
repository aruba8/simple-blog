package blog.logic;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostHandler {

    public static Map<String, Object> getPost(DBObject postObject) {
        Post post = new Post();
        Date dateTime = (Date) postObject.get("dateTime");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        post.setDate(sdf.format(dateTime));

        String title = (String) postObject.get("title");
        post.setTitle(title);

        String articleBody = (String) postObject.get("articleBody");
        post.setArticleBody(articleBody);

        String permalink = (String) postObject.get("permalink");
        post.setPermalink(permalink);

        BasicDBList tags = (BasicDBList) postObject.get("tags");

        try {
            post.setArticlePreview(createArticlePreview(post.getArticleBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Object> postEntity = new HashMap<String, Object>();

        try {
            String htmlArticle = new Markdown4jProcessor().process(post.getArticleBody());
            postEntity.put("dateTime", post.getDate());
            postEntity.put("title", post.getTitle());
            postEntity.put("articleBody", htmlArticle);
            postEntity.put("permalink", post.getPermalink());
            postEntity.put("articlePreview", post.getArticlePreview());
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
            String htmlPreview = new Markdown4jProcessor().process(article);
            return htmlPreview.substring(0, 600);
        } else {
            return new Markdown4jProcessor().process(article);
        }
    }


    public static Post preparePost(String rawPost) {
        String splitRawPost[] = rawPost.split("~~~~~~~~~~~~~~");
        String head = splitRawPost[0];
        head = head.replaceAll("\n|\r\n", "");
        String body = splitRawPost[1];
        String[] splitHead = head.split("Categories:");
        String title = splitHead[0].substring(6, splitHead[0].length());
        String categories = null;
        if (splitHead.length > 1) {
            categories = splitHead[1];
            categories = categories.trim();
        }

        Post post = new Post();
        post.setArticleBody(body.trim());
        post.setTitle(title.trim());
        post.setTagsString(categories);
        return post;
    }
}
