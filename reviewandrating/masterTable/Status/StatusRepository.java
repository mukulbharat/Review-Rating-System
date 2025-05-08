package com.krenai.reviewandrating.masterTable.Status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusCode,Long> {


    Optional<StatusCode> findByStatus(String status);
}
