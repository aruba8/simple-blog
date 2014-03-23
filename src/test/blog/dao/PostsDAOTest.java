package blog.dao;

import blog.logic.Post;
import com.mongodb.BasicDBObject;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostsDAOTest {


    PostsDAO postsDAOmock = mock(PostsDAO.class);

    @Test
    public void testInsertPost() {
        Post post = new Post();
        post.setArticleBody("Test");
        post.setTitle("Test title");
        post.setPermalink("test-title");

        when(postsDAOmock.insertPost(post)).thenReturn(true);
        assertThat(postsDAOmock.insertPost(post), equalTo(true));
    }


    @Test
    public void testFindByPermalink() {
        when(postsDAOmock.findByPermalink("_____")).thenReturn(new BasicDBObject("_id", "52fbb0010364d57bd7784f36")
                .append("dateTime", "2014-02-12T17:31:45.750Z"));

    }
}
