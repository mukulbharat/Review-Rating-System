package com.krenai.reviewandrating.masterTable.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository rolesRepository;

    public Role createRole(Role role) {
        return rolesRepository.save(role);
    }

    public Role getById(Long roleId)
    {
        Role role= rolesRepository.findById(roleId).orElseThrow(
                ()->new RuntimeException("Role with id  not found."+ roleId)
        );
        System.out.println(role.getUserRole());
        return role;
    }
}
