package blog.dao;

import blog.logic.Post;
import com.mongodb.BasicDBObject;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostsDAOTest {
    PostsDAO postsDAOmock = mock(PostsDAO.class);

    @Test
    public void testInsertPost() throws Exception {
        Post post = new Post();
        post.setArticleBody("Test");
    }

    @Test
    public void testFindPostsByDescending() throws Exception {

    }

    @Test
    public void testFindByPermalink() throws Exception {
        when(postsDAOmock.findByPermalink("_____")).thenReturn(new BasicDBObject("_id", "52fbb0010364d57bd7784f36")
                .append("dateTime", "2014-02-12T17:31:45.750Z"));

    }
}
