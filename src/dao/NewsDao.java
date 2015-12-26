package dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import entity.News;

public class NewsDao extends HibernateDaoSupport {
	public Integer save(News news){
		return (Integer)getHibernateTemplate().save(news);
	}
}
