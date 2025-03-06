/**
 * 
 */
package com.ppc.mm.nickprodreporting.dao;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public abstract class AbstractDAO {

	/**
	 * Logger instance
	 */
	static Logger LOG = LoggerFactory.getLogger(AbstractDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	//@Autowired
	//private HibernateTemplate hibernateTemplate;
	
	/*protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}*/
	public Integer save(Object o) {
		
		Integer id;
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			id = (Integer) session.save(o);
			tx.commit();
		}
		return id;
		//return (Integer) hibernateTemplate.save(o);
	}
	public void update(Object o) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.update(o);
			tx.commit();

		}
		//hibernateTemplate.update(o);
	}
	public Object get(Integer id, Class<?> clazz) {
		try (Session session = sessionFactory.openSession()) {
			return session.get(clazz, id);
		}
		//return hibernateTemplate.get(clazz, id);
	}

	public void remove(Object o) {
		Transaction tx = null;
		try (Session session = sessionFactory.openSession()) {
			tx = session.beginTransaction();
			session.delete(o);
			tx.commit();

		}
		//hibernateTemplate.update(o);
	}
}
