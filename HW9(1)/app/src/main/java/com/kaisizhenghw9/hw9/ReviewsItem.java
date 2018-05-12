package com.kaisizhenghw9.hw9;

public class ReviewsItem {
    private String reviewIcon;
    private String reviewName;
    private int reviewRating;
    private String reviewTime;
    private String reviewTxt;
    private String author_url;
    private int order;
    public ReviewsItem(String reviewIcon, String reviewName, int reviewRating, String reviewTime, String reviewTxt, String author_url, int order) {
        this.reviewIcon = reviewIcon;
        this.reviewName = reviewName;
        this.reviewRating = reviewRating;
        this.reviewTime = reviewTime;
        this.reviewTxt = reviewTxt;
        this.author_url = author_url;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public String getAuthor_url() {
        return author_url;
    }

    public String getReviewIcon() {
        return reviewIcon;
    }

    public String getReviewName() {
        return reviewName;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public String getReviewTxt() {
        return reviewTxt;
    }
}
