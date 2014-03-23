package blog.dao;

import com.mongodb.*;

import java.util.Date;

public class PagesDAO {
    private DBCollection pagesCollection;

    public PagesDAO(final DB blogDB){
        this.pagesCollection = blogDB.getCollection("pages");
    }

    public Boolean checkPageExist(String pageName){
        DBObject query = new BasicDBObject("pageName", pageName);
        DBCursor resultSet = this.pagesCollection.find(query);
        return resultSet.count() < 1;
    }

    public void createPage(String pageName){
        DBObject query = new BasicDBObject("pageName", pageName)
                .append("pageContent", "")
                .append("createdDateTime", new Date())
                .append("permalink", pageName);
        this.pagesCollection.insert(query);
    }
}
