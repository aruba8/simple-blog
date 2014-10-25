package blog.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

@Entity("posts")
public class Post {
    @Id
    private ObjectId id;
    @Embedded
    private User author;
    private String date;
    private String title;
    private String articleBody;
    private String permalink;
    private String [] tags;
    private Boolean isCommentsAvailable;
    private Date dateTime;

    @Override
    public String toString(){
        return this.title+" : "+ dateTime;
    }

    public Date getDateTime(){
        return dateTime;
    }

    public User getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPermalink() {
        return permalink;
    }

    public String[] getTags() {
        return tags;
    }

    public Boolean getIsCommentsAvailable() {
        return isCommentsAvailable;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public void setIsCommentsAvailable(boolean isCommentsAvailable) {
        this.isCommentsAvailable = isCommentsAvailable;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setDateTime(Date date) {
        this.dateTime = date;
    }

    public ObjectId getId() {
        return id;
    }
}
