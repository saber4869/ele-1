package actionclass;

import java.io.IOException;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import dao.NewsDao;
import distance.MyDistance;
import entity.News;

public class HelloWorldAction {
	
	private OtherObj otherObj;
	private NewsDao newsDao;
	private MyDistance distanceObj;
	private String anstr;

	public OtherObj getOtherObj() {
		return otherObj;
	}

	public void setOtherObj(OtherObj otherObj) {
		this.otherObj = otherObj;
	}

	public NewsDao getNewsDao() {
		return newsDao;
	}

	public void setNewsDao(NewsDao newsDao) {
		this.newsDao = newsDao;
	}

	public String hello(){
//		News news = new News();
//		news.setTitle("Action写进来的,用了Dao！");
//		newsDao.save(news);
		try {
			anstr = distanceObj.interaction("本科", "22", "北京邮电大学", "软件工程", "软件工程师");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	public MyDistance getDistanceObj() {
		return distanceObj;
	}

	public void setDistanceObj(MyDistance distanceObj) {
		this.distanceObj = distanceObj;
	}

	public String getAnstr() {
		return anstr;
	}

	public void setAnstr(String anstr) {
		this.anstr = anstr;
	}
	
	
}
