package com.krenai.reviewandrating.masterTable.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByUserRole(String UserRole);
}
