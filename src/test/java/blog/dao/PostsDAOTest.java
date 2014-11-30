package blog.dao;

import blog.models.Post;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostsDAOTest {


    PostsDAO postsDAOmock = mock(PostsDAO.class);

    @Before
    public void setUp(){
        Post post2 = new Post();
        Post post = new Post();
        post.setTitle("AAAAAAA");
        post2.setTitle("EEEAAAAAAA");
        post.setIsCommentsAvailable(true);
        post2.setIsCommentsAvailable(true);
        post.setPermalink("wwwwwww");
        post2.setPermalink("QQQQQQQ");
        post.setDateTime(new Date());
        post2.setDateTime(new Date());
        post.setArticleBody("qweqweqweqweqweqweqw");
        post2.setArticleBody("ertertertertertertert");
        List<Post> postList = new ArrayList<Post>();
        postList.add(post);
        postList.add(post2);

        when(postsDAOmock.findPostsByDescending(10)).thenReturn(postList);
    }

}
