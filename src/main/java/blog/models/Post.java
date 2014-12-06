package blog.models;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User author;
    @Column(name = "title")
    private String title;
    @Column(name = "articleBody", length = 5000)
    private String articleBody;
    @Column(name = "permalink")
    private String permalink;
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.REFRESH
    )
    @JoinTable(
            name = "post_tag",
            joinColumns={@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags = new HashSet<Tag>();
    @Column(name = "isCommentsAvailable")
    private Boolean isCommentsAvailable;
    @Column(name = "datetime")
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

    public Set<Tag> getTags() {
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

    public void setTags(Set tags) {
        this.tags = tags;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setDateTime(Date date) {
        this.dateTime = date;
    }
    public Long getId() {
        return id;
    }

    public String [] getTagNames(){
        String [] tagNames = new String[this.tags.size()];
        int i = 0;
        for(Tag tag : this.tags){
            tagNames[i] = tag.getName();
            i++;
        }
        return tagNames;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
