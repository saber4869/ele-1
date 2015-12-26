import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import entity.News;

public class HibernateTestDriver {

	public static void main(String[] args) {
		Configuration conf = new Configuration().configure();
		SessionFactory sf = conf.buildSessionFactory();
		Session sess = sf.openSession();
		Transaction tx = sess.beginTransaction();
		List<News> pl = sess.createQuery("select n.title from News n").list();
		for(Iterator pit = pl.iterator();pit.hasNext();){
			System.out.println(pit.next());
		}
		News n = new News();
		n.setTitle("hh");
		sess.save(n);
		tx.commit();
		sess.close();
		sf.close();
	}
}
