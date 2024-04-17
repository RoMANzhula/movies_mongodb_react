package org.romanzhula.movies_mongodb_react.controllers;

import org.romanzhula.movies_mongodb_react.models.Review;
import org.romanzhula.movies_mongodb_react.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<>(
                reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId")), HttpStatus.OK
        );
    }
}
