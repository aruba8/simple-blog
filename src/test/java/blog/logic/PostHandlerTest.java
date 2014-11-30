package blog.logic;

import blog.models.Post;
import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class PostHandlerTest {
    private String rawPost = "Title: new title\n" +
            "Categories:category, second_cat\n" +
            "Comments:false\n" +
            "~~~~~~~~~~~~~~\n" +
            "hello this is content";
    @Test
    public void testPreparePost(){
        Post post = PostHandler.createPost(rawPost);
        assertThat(post.getTitle(), equalTo("new title"));
        assertThat(post.getArticleBody(), equalTo("\nhello this is content"));
        assertThat(post.getPermalink(), equalTo("new-title"));
        assertThat(post.getIsCommentsAvailable(), Is.is(false));
    }

    @Test
    public void testCreatePermalink(){
        String rawTitle = "Перевод книги «The Little Book on CoffeeScript»";
        assertThat(PostHandler.createPermalink(rawTitle), equalTo("perevod-knigi--the-little-book-on-coffeescript-"));
    }


}
