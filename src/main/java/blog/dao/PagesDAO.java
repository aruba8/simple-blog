package blog.dao;


import org.hibernate.Session;

public class PagesDAO {
    private Session hibernateSession;

    public PagesDAO(Session session){
        this.hibernateSession = session;
    }

    public Boolean checkPageExist(String pageName){
        return false;
    }

    public void createPage(String pageName){
    }
}
