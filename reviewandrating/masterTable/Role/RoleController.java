package com.krenai.reviewandrating.masterTable.Role;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private static final Logger log = LoggerFactory.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @GetMapping("/{roleId}")
    public Role getByID(@PathVariable Long roleId)
    {
        try {
            log.info("Role id: {}",roleId);
            return roleService.getById(roleId);
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
