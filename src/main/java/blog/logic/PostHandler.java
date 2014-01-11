package blog.logic;

import com.mongodb.DBObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostHandler {
    private DBObject postObject;
    private Post post;

    public PostHandler(DBObject postObject) {
        this.postObject = postObject;
        post = new Post();
    }

    public Map<String, String> getPost() {
        Date dateTime = (Date) postObject.get("dateTime");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY HH:mm");
        post.setDate(sdf.format(dateTime));

        String title = (String) postObject.get("title");
        post.setTitle(title);

        String articleBody = (String) postObject.get("articleBody");
        post.setArticleBody(articleBody);

        String permalink = (String) postObject.get("permalink");
        post.setPermalink(permalink);

        post.setArticlePreview(createArticlePreview(post.getArticleBody()));

        Map<String, String> postEntity = new HashMap<String, String>();

        postEntity.put("dateTime", post.getDate());
        postEntity.put("title", post.getTitle());
        postEntity.put("articleBody", post.getArticleBody());
        postEntity.put("permalink", post.getPermalink());
        postEntity.put("articlePreview", post.getArticlePreview());

        return postEntity;
    }

    private String createArticlePreview(String article){
        if (article.length() > 600){
            return article.substring(0, 600)+"...";
        } else {
            return article;
        }
    }


}
