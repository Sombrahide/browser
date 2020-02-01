package hibernate;

//First, the imports
import java.io.File;
import java.util.logging.Level;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//The class HibernateUtil, with this we can open and close our hibernate session
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	static {
		try {
			// We call the configure () method to configure the hibernate, we must remember
			// to put the path to the file well
			sessionFactory = new Configuration().configure(new File("src/main/resources/hibernate.cfg.xml"))
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed" + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	// The current method with which we will create our SessionFactory
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
