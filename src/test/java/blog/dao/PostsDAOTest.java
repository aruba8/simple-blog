package blog.dao;

import blog.models.Post;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostsDAOTest {
    PostsDAO postsDAOmock = mock(PostsDAO.class);
    Post post;
    Post post2;

    @Before
    public void setUp(){
        post2 = new Post();
        post = new Post();
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

    @Test
    public void test(){

    }

}
