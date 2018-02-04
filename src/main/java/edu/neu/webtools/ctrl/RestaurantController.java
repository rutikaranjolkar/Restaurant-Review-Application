package edu.neu.webtools.ctrl;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.neu.webtools.domain.AddReplyToReview;
import edu.neu.webtools.domain.AddReviewRequest;
import edu.neu.webtools.domain.GetMyReviewsRequest;
import edu.neu.webtools.domain.GetReviewRepliesRequest;
import edu.neu.webtools.domain.GetReviewRequest;
import edu.neu.webtools.domain.Restaurant;
import edu.neu.webtools.domain.RestaurantReview;
import edu.neu.webtools.domain.ReviewReply;
import edu.neu.webtools.domain.StatusObject;
import edu.neu.webtools.domain.User;
import edu.neu.webtools.security.RestSecurityInterceptor;
import edu.neu.webtools.security.UserSession;
import edu.neu.webtools.service.RestaurantService;
import edu.neu.webtools.service.SecurityService;

@Controller
@Path("/restaurant")
public class RestaurantController {

	private static Logger LOG = Logger.getLogger(RestaurantController.class.getName());

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private SecurityService securityService;

	@Context
	private HttpServletRequest httpRequest;

	@POST
	@Consumes("application/json")
	@Path("/add")
	@RolesAllowed("OWNER")
	public void addOrUpdateRestaurant(Restaurant restaurant) {
		String authToken = httpRequest.getHeader(RestSecurityInterceptor.AUTH_HEADER_NAME);
		UserSession userSession = (UserSession) httpRequest.getServletContext().getAttribute(authToken);
		String ownerUsername = userSession.getUsername();
		User owner = securityService.getByUsername(ownerUsername);
		restaurant.setOwner(owner);
		restaurantService.saveOrUpdateRestaurant(restaurant);
	}

	@GET
	@Produces("application/json")
	@Path("/getAll")
	@PermitAll
	public List<Restaurant> getAllRestaurants() {
		LOG.info("Returning all restaurants");
		return restaurantService.getAllRestaurants();
	}

	@POST
	@Produces("application/json")
	@Path("/getMyReviews")
	@PermitAll
	public List<RestaurantReview> getMyReviews(GetMyReviewsRequest request) {
		LOG.info("Returning all reviews for customer: " + request.getUsername());
		String username = request.getUsername();
		return restaurantService.getMyReviews(username);
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/getReviews")
	@PermitAll
	public List<RestaurantReview> getRestaurantReviews(GetReviewRequest request) {
		LOG.info("Returning all reviews for restaurant: " + request.getRestaurantId());
		return restaurantService.getReviews(request);
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/addReview")
	@RolesAllowed("CUSTOMER")
	public StatusObject addReview(AddReviewRequest request) {
		LOG.info("Adding new review for restaurant: " + request.getRestaurantId());
		String authToken = httpRequest.getHeader(RestSecurityInterceptor.AUTH_HEADER_NAME);
		UserSession userSession = (UserSession) httpRequest.getServletContext().getAttribute(authToken);
		String username = userSession.getUsername();
		request.setReviewerUsername(username);
		return restaurantService.addReview(request);
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/addReply")
	@RolesAllowed("OWNER")
	public StatusObject addReplyToReviews(AddReplyToReview addReplyToReview) {
		LOG.info("Adding reply to review: " + addReplyToReview.getReviewId());
		return restaurantService.addReplyToReviews(addReplyToReview);
	}

	@POST
	@Produces("application/json")
	@Consumes("application/json")
	@Path("/getReplies")
	@PermitAll
	public List<ReviewReply> getReviewReplies(GetReviewRepliesRequest request) {
		LOG.info("Returning replies for review: " + request.getReviewId());
		return restaurantService.getReviewReplies(request);
	}

}
