package com.krenai.reviewandrating.review;

import com.krenai.reviewandrating.constants.ApiResponse;
import com.krenai.reviewandrating.entities.Business;
import com.krenai.reviewandrating.entities.Review;
import com.krenai.reviewandrating.requestdto.ReviewRequestDto;
import com.krenai.reviewandrating.responsedto.BusinessDto;
import com.krenai.reviewandrating.responsedto.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import static com.krenai.ReviewAndRating.masterTable.ApiConstants.BASE_API_PATH;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> postReview(@RequestBody ReviewRequestDto reviewRequestDto,
                                                            @RequestParam String userUuid,
                                                            @RequestParam String businessUuid) {
        //Review rev=reviewService.postReview(review, userId, businessId);

        log.info("Posting a new review for user UUID: {}, business UUID: {}", userUuid, businessUuid);
        ReviewDto savedReview = reviewService.postReview(reviewRequestDto, userUuid, businessUuid);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Review posted successfully", savedReview));




        //Review savedReview = reviewService.postReview(reviewRequestDto, userId, businessId);
        //return new ResponseEntity<>(reviewService.postReview(reviewRequestDto,userUuid,businessUuid), HttpStatus.CREATED);
    //return new ResponseEntity<Review>(reviewService.postReview(review,userId,businessId), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getAllReviews( @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching all reviews");

        List<ReviewDto> reviews = reviewService.getAllReviews(page,size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "All reviews fetched successfully",
                        reviews,
                        reviews.size()
                )
        );
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable String uuid) {

        log.info("Fetching review with UUID: {}", uuid);

        ReviewDto reviewDto=reviewService.getReviewById(uuid);

        if (reviewDto == null) {
            log.warn("Review not found with UUID: {}", uuid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Review not found", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Review fetched successfully", reviewDto));
    }



    @DeleteMapping("/{uuid}")
    public ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable String uuid) {

        log.info("Deleting review with UUID: {}", uuid);

        String result=reviewService.deleteReview(uuid);
        return ResponseEntity.ok(new ApiResponse<>(true, result, result));
    }

    @GetMapping("/user/{userUuid}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByUser(@PathVariable String userUuid,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching reviews by user UUID: {}, page {}, size {}", userUuid, page, size);

        List<ReviewDto> userReviews=reviewService.getReviewsByUser(userUuid,page,size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User reviews fetched successfully",
                        userReviews,
                        userReviews.size()
                )
        );
    }

    @GetMapping("/business/{businessUuid}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByBusiness(@PathVariable String businessUuid,
                                                                             @RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching reviews by business UUID: {}, page {}, size {}", businessUuid, page, size);


        List<ReviewDto> businessReviews=reviewService.getReviewsByBusiness(businessUuid,page,size);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Business reviews fetched successfully",
                        businessReviews,
                        businessReviews.size()
                )
        );
    }
}
