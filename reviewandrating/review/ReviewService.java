package com.krenai.reviewandrating.review;

import com.fasterxml.uuid.Generators;
import com.krenai.reviewandrating.entities.Business;
import com.krenai.reviewandrating.responsedto.BusinessDto;
import com.krenai.reviewandrating.business.BusinessRepository;
import com.krenai.reviewandrating.entities.Review;
import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.requestdto.ReviewRequestDto;
import com.krenai.reviewandrating.responsedto.ReviewDto;
import com.krenai.reviewandrating.responsedto.UserDto;
import com.krenai.reviewandrating.user.UserRepository;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

//    public List<Review> getAllReviews()
//    {
//        return reviewRepository.findAll();
//    }


    public List<ReviewDto> getAllReviews(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending().and(Sort.by("rating").descending()));
        List<Review> reviews = reviewRepository.findAllByIsFlagTrue(pageable)
                .stream()
                .filter(Review::getIsFlag)
                .collect(Collectors.toList());

        return reviews.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public ReviewDto postReview(ReviewRequestDto reviewRequestDto, String userUuid, String businessUuid)
    {
        try {
            User user = userRepository.findByUuid(userUuid)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userUuid));

            Business business = businessRepository.findByUuid(businessUuid)
                    .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessUuid));

            Review review = new Review();
            review.setReviewText(reviewRequestDto.getReviewText());
            review.setRating(reviewRequestDto.getRating());
            review.setUser(user);
            review.setBusiness(business);
            //review.setUuid(Generators.timeBasedGenerator().generate().toString());
            review.setUuid(UuidUtil.getTimeBasedUuid().toString());
            //review.setCreatedAt(LocalDateTime.now());
            review.setIsFlag(true);
            return convertToDto(reviewRepository.save(review));

        } catch (Exception e) {
            throw new RuntimeException("Failed to post review: " + e.getMessage());
        }
    }
    public ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setUuid(review.getUuid());
        dto.setReviewText(review.getReviewText());
        dto.setRating(review.getRating());
        //dto.setIsFlag(review.getIsFlag());
        //dto.setCreatedAt(review.getCreatedAt());

        // Convert user to UserDto
        UserDto userDto = new UserDto();
        userDto.setUuid(review.getUser().getUuid());
        userDto.setUserName(review.getUser().getUsername());
        userDto.setPhoneNumber(review.getUser().getPhonenumber());
        dto.setUser(userDto);

        // Convert business to BusinessDto
        BusinessDto businessDto = new BusinessDto();
        businessDto.setUuid(review.getBusiness().getUuid());
        businessDto.setBusinessName(review.getBusiness().getBusinessname());
        businessDto.setLatitude(review.getBusiness().getLatitude());
        businessDto.setLongitude(review.getBusiness().getLongitude());

        // Set business userDto
        UserDto businessUserDto = new UserDto();
        businessUserDto.setUuid(review.getBusiness().getUser().getUuid());
        businessUserDto.setUserName(review.getBusiness().getUser().getUsername());
        businessUserDto.setPhoneNumber(review.getBusiness().getUser().getPhonenumber());

        businessDto.setUserDto(businessUserDto);
        dto.setBusiness(businessDto);

        return dto;
    }

    public ReviewDto getReviewById(String uuid) {
        Review review= reviewRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + uuid));

        if (!review.getIsFlag()) {
            throw new RuntimeException("Review does not exist");
        }

        return convertToDto(review);
    }

    public String deleteReview(String uuid) {
        Review review = reviewRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + uuid));

        if (!review.getIsFlag()) {
            return ("Review does not exist");
        }

        review.setIsFlag(false);
        reviewRepository.save(review);

        return "Review deleted";
    }

    public List<ReviewDto> getReviewsByUser(String userUuid, int page, int size) {
        // Ensure the user exists
        User user=  userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userUuid));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending().and(Sort.by("rating").descending()));
        List<ReviewDto> reviews= reviewRepository.findByUserIdAndIsFlagTrue(user.getId(),pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return reviews;
    }

    public List<ReviewDto> getReviewsByBusiness(String businessUuid,int page, int size) {
        // Ensure the business exists
        Business business= businessRepository.findByUuid(businessUuid)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + businessUuid));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending().and(Sort.by("rating").descending()));
        List<ReviewDto> reviews= reviewRepository.findByBusinessIdAndIsFlagTrue(business.getId(),pageable).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return reviews;


    }


}
