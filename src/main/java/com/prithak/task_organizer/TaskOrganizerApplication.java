package com.prithak.task_organizer;

import com.prithak.task_organizer.constants.PermissionConstant;
import com.prithak.task_organizer.constants.RoleConstant;
import com.prithak.task_organizer.model.Permission;
import com.prithak.task_organizer.model.Role;
import com.prithak.task_organizer.model.User;
import com.prithak.task_organizer.repository.PermissionRepository;
import com.prithak.task_organizer.repository.RoleRepository;
import com.prithak.task_organizer.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
public class TaskOrganizerApplication {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  public static void main(String[] args) {
    SpringApplication.run(TaskOrganizerApplication.class, args);
  }

  @PostConstruct
  public void init() {
    Set<Role> roles = new HashSet<>();
    Set<Permission> permissions = new HashSet<>();
    Permission createPermission = new Permission();
    createPermission.setName(PermissionConstant.CREATE);

    Permission readPermission = new Permission();
    readPermission.setName(PermissionConstant.READ);

    Permission updatePermission = new Permission();
    updatePermission.setName(PermissionConstant.UPDATE);

    Permission deletePermission = new Permission();
    deletePermission.setName(PermissionConstant.DELETE);

    permissions.add(createPermission);
    permissions.add(readPermission);
    permissions.add(updatePermission);
    permissions.add(deletePermission);
    permissions.forEach(permission -> {
      if (permissionRepository.findByName(permission.getName()).isEmpty()) {
        permissionRepository.save(permission);
      }
    });

    Role userRole = new Role();
    userRole.setName(RoleConstant.ROLE_USER);
    userRole.setPermissions(Set.of(readPermission, createPermission, updatePermission, deletePermission));

    Role adminRole = new Role();
    adminRole.setName(RoleConstant.ROLE_ADMIN);
    adminRole.setPermissions(Set.of(readPermission, createPermission, updatePermission, deletePermission));

    roles.add(userRole);
    roles.add(adminRole);
    roles.forEach(role -> {
      if (roleRepository.findByName(role.getName()).isEmpty()) {
        roleRepository.save(role);
      }
    });

    User superUser = new User();
    superUser.setUsername("prithak");
    superUser.setPassword(passwordEncoder.encode("admin"));
    superUser.setEmail("prithak@gmail.com");
    superUser.setRoles(Set.of(adminRole));

    if (userRepository.findByUsername(superUser.getUsername()).isEmpty()) {
      userRepository.save(superUser);
    }
  }
}
