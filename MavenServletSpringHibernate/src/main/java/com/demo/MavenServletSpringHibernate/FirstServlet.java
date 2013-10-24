package com.demo.MavenServletSpringHibernate;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@WebServlet("/firstservlet")
public class FirstServlet extends HttpServlet {
	
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
    public FirstServlet() {
        super();        
    }
    
	static {
		
		// Getting a SessionFactory object:
		// Session factory is created once per application.
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException e) {
			System.err.println("Initial SessionFactory creation failed." + e);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("Hello from the servlet)");
		
		// Using Spring:
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BeanInterface beanObj = (BeanInterface)context.getBean("myBean");
		String message = beanObj.sayHello();
		out.println(message);
		
		// Using Hibernate:
		UserDetails user = new UserDetails();
		user.setUserName("The first user)");
		
		// Creating a session factory:
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
		session.close();
		
		out.close();
	}
}