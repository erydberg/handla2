package se.rydberg.handla.security;

import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getNormalUserRole() {
        Role role = getByName("ROLE_USER");
        if (role == null) {
            role = new Role("ROLE_USER");
            save(role);
        }
        return role;
    }

    public Role save(Role role){
        return roleRepository.save(role);
    }

    public Role getByName(String name){
        return roleRepository.getRoleByName(name);
    }
}
