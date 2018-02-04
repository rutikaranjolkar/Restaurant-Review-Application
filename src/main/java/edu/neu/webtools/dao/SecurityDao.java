package edu.neu.webtools.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.neu.webtools.domain.User;

@Repository
public class SecurityDao {

	@Autowired
	private SessionFactory sessionFactory;

	public User authenticate(String username, String passwordHash) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("passwordHash", passwordHash);
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User u WHERE u.username = :username AND u.password = :passwordHash");
		query.setParameter("username", username);
		query.setParameter("passwordHash", passwordHash);
		List resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			User user = (User) resultList.get(0);
			return user;
		} else {
			return null;
		}
	}

	public User getByUsername(String username) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("FROM User u WHERE u.username = :username");
		query.setParameter("username", username);
		List resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			User user = (User) resultList.get(0);
			return user;
		} else {
			return null;
		}

	}

	public void saveNewUser(User user) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(user);
		tx.commit();
		session.close();
	}

}
