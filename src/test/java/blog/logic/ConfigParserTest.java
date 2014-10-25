package blog.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigParserTest {
    ConfigParser configParser;
    @Before
    public void setUp(){
        configParser = mock(ConfigParser.class);
        when(configParser.getBlogName()).thenReturn("Blog");
        when(configParser.getDisqusShortName()).thenReturn("Disqus short name");
        when(configParser.getPages()).thenReturn(new String[]{"Blog", "About"});
    }

    @Test
    public void correctParsingTest(){
        Assert.assertThat(configParser.getBlogName(), equalTo("Blog"));
        Assert.assertThat(configParser.getDisqusShortName(), equalTo("Disqus short name"));
        Assert.assertThat(configParser.getPages().length, equalTo(2));
    }
}
