package edu.neu.webtools.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.neu.webtools.domain.GetReviewRequest;
import edu.neu.webtools.domain.Restaurant;
import edu.neu.webtools.domain.RestaurantReview;
import edu.neu.webtools.domain.ReviewReply;

@Component
public class RestaurantDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void saveorUpdateRestaurant(Restaurant restaurant) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(restaurant);
		tx.commit();
		session.close();

	}

	public List<Restaurant> getAllRestaurants() {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Restaurant r");
		List<Restaurant> restaurants = (List<Restaurant>) query.getResultList();
		session.close();
		return restaurants;
	}

	public List<RestaurantReview> getReviews(GetReviewRequest request) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from RestaurantReview r where r.restaurant.id = :id");
		query.setParameter("id", request.getRestaurantId());
		List<RestaurantReview> reviews = query.getResultList();
		session.close();
		return reviews;

	}

	public Restaurant getById(long restaurantId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Restaurant r where r.id = :id");
		query.setParameter("id", restaurantId);
		List<Restaurant> restaurants = (List<Restaurant>) query.getResultList();
		session.close();
		return restaurants.get(0);
	}

	public void saveOrUpdateReview(RestaurantReview review) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(review);
		tx.commit();
		session.close();

	}

	public List<RestaurantReview> getMyReviews(String username) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from RestaurantReview r where r.reviewer.username = :username");
		query.setParameter("username", username);
		List<RestaurantReview> myReviews = query.getResultList();
		session.close();
		return myReviews;
	}

	public RestaurantReview getReviewById(long reviewId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from RestaurantReview r where r.id = :id");
		query.setParameter("id", reviewId);
		List<RestaurantReview> reviews = (List<RestaurantReview>) query.getResultList();
		session.close();
		return reviews.get(0);
	}

	public void saveReviewReply(ReviewReply reviewReply) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(reviewReply);
		tx.commit();
		session.close();
	}

	public List<ReviewReply> getRepliesByReviewId(long reviewId) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from ReviewReply r where r.review.id = :id");
		query.setParameter("id", reviewId);
		List<ReviewReply> replies = (List<ReviewReply>) query.getResultList();
		session.close();
		return replies;
	}

}
