package edu.neu.webtools.domain;

public class AddReplyToReview {
	private int reviewId;
	private String ownerComments;

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int revirewId) {
		this.reviewId = revirewId;
	}

	public String getOwnerComments() {
		return ownerComments;
	}

	public void setOwnerComments(String ownerComments) {
		this.ownerComments = ownerComments;
	}

}
