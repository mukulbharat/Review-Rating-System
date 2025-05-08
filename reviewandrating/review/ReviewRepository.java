package com.krenai.reviewandrating.review;

import com.krenai.reviewandrating.entities.Review;
import com.krenai.reviewandrating.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByUserId(Long userId);
    List<Review> findByBusinessId(Long businessId);
    Optional<Review> findByUuid(String uuid);


    Page<Review> findAllByIsFlagTrue(Pageable pageable);
    Page<Review> findByUserIdAndIsFlagTrue(Long userId, Pageable pageable);
    Page<Review> findByBusinessIdAndIsFlagTrue(Long businessId, Pageable pageable);
}
