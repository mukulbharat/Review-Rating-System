package com.krenai.reviewandrating.user;

import com.krenai.reviewandrating.entities.User;
import com.krenai.reviewandrating.responsedto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndIsFlagTrue(String email);
    Optional<User> findByEmailOrPhonenumberAndIsFlagTrue(String email,String phonenumber);
    Optional<User> findByUuid(String uuid);
    Optional<User> findByUuidAndIsFlagTrue(String uuid);
    List<User> findAllByIsFlagTrue();
    Optional<User> findByPhonenumberAndIsFlagTrue(String phonenumber);

}
