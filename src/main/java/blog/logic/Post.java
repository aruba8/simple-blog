package blog.logic;

public class Post {
    private String date;
    private String title;
    private String articleBody;
    private String permalink;
    private String articlePreview;
    private String [] tags;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getArticlePreview() {
        return articlePreview;
    }

    public void setArticlePreview(String articlePreview) {
        this.articlePreview = articlePreview;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
