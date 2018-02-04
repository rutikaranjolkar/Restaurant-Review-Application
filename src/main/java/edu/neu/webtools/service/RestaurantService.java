package edu.neu.webtools.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.neu.webtools.dao.RestaurantDao;
import edu.neu.webtools.dao.SecurityDao;
import edu.neu.webtools.domain.AddReplyToReview;
import edu.neu.webtools.domain.AddReviewRequest;
import edu.neu.webtools.domain.GetReviewRepliesRequest;
import edu.neu.webtools.domain.GetReviewRequest;
import edu.neu.webtools.domain.Restaurant;
import edu.neu.webtools.domain.RestaurantReview;
import edu.neu.webtools.domain.ReviewReply;
import edu.neu.webtools.domain.StatusObject;
import edu.neu.webtools.domain.User;

@Component
public class RestaurantService {
	@Autowired
	private RestaurantDao restaurantDao;

	@Autowired
	private SecurityDao securityDao;

	public void saveOrUpdateRestaurant(Restaurant restaurant) {
		restaurantDao.saveorUpdateRestaurant(restaurant);
	}

	public List<Restaurant> getAllRestaurants() {
		return restaurantDao.getAllRestaurants();
	}

	public List<RestaurantReview> getReviews(GetReviewRequest request) {
		return restaurantDao.getReviews(request);
	}

	public StatusObject addReview(AddReviewRequest request) {
		long restaurantId = request.getRestaurantId();
		int rating = request.getRating();
		String comments = request.getComments();
		String reviewerUsername = request.getReviewerUsername();
		User reviewer = securityDao.getByUsername(reviewerUsername);

		Restaurant restaurant = restaurantDao.getById(restaurantId);
		RestaurantReview review = new RestaurantReview();
		review.setReviewer(reviewer);
		review.setComments(comments);
		review.setRating(rating);
		review.setRestaurant(restaurant);

		restaurantDao.saveOrUpdateReview(review);
		StatusObject status = new StatusObject(true, null);
		return status;
	}

	public List<RestaurantReview> getMyReviews(String username) {
		return restaurantDao.getMyReviews(username);
	}

	public StatusObject addReplyToReviews(AddReplyToReview addReplyToReview) {
		String comments = addReplyToReview.getOwnerComments();
		long reviewId = addReplyToReview.getReviewId();
		RestaurantReview review = restaurantDao.getReviewById(reviewId);
		ReviewReply reviewReply = new ReviewReply();
		reviewReply.setComments(comments);
		reviewReply.setReview(review);
		restaurantDao.saveReviewReply(reviewReply);
		StatusObject status = new StatusObject(true, null);
		return status;
	}

	public List<ReviewReply> getReviewReplies(GetReviewRepliesRequest request) {
		long reviewId = request.getReviewId();
		List<ReviewReply> replies = restaurantDao.getRepliesByReviewId(reviewId);
		return replies;
	}
}
