package com.krenai.reviewandrating.business;

import com.krenai.reviewandrating.entities.Business;
import com.krenai.reviewandrating.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business,Long> {

   // Optional<Business> findByEmailAndIsFlagTrue(String email);
    Optional<Business> findByUuid(String uuid);
    Optional<Business> findByUuidAndIsFlagTrue(String uuid);
    List<Business> findAllByIsFlagTrue();
}
