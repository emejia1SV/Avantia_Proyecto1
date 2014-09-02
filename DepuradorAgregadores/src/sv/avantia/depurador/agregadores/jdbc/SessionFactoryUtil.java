package sv.avantia.depurador.agregadores.jdbc;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {
	
	/* Get actual class name to be printed on */
	public static Logger logger = Logger.getLogger("avantiaLogger");

	// Annotation based configuration
	private static SessionFactory sessionAnnotationFactory;
	
	private static SessionFactory buildSessionAnnotationFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			Configuration configuration = new Configuration();
			configuration.configure("hibernate-annotation.cfg.xml");
			logger.info("Hibernate Annotation Configuration loaded");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
			logger.info("Hibernate Annotation serviceRegistry created");

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			return sessionFactory;
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			logger.error("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static SessionFactory getSessionAnnotationFactory() {
		if (sessionAnnotationFactory == null || sessionAnnotationFactory.isClosed())
			sessionAnnotationFactory = buildSessionAnnotationFactory();
		return sessionAnnotationFactory;
	}
	
	public static void closeSession(){
		if(sessionAnnotationFactory.getCurrentSession().isOpen())
			sessionAnnotationFactory.getCurrentSession().close();
		
		sessionAnnotationFactory = null;
	}
}