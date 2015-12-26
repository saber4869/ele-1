import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTestDriver {
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans-config.xml");
		System.out.println(ctx);
		PersonService p = ctx.getBean("personService", PersonService.class);
		p.info();
	}
}
