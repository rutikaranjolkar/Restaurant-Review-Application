package edu.neu.webtools.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "owner_reply")
public class ReviewReply {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "comments")
	private String comments;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "review_id")
	private RestaurantReview review;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public RestaurantReview getReview() {
		return review;
	}

	public void setReview(RestaurantReview review) {
		this.review = review;
	}

}
