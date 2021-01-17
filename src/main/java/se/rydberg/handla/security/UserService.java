package se.rydberg.handla.security;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    public User savenew(UserDTO userDto) {
        User user = toEntity(userDto);
        if(user!=null) {
            Role role = roleService.getNormalUserRole();
            user.addRole(role);
            user.setEnabled(true);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String losen = "{bcrypt}" + bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(losen);

            return userRepository.save(user);
        }else{
            return null;
        }
    }

    public User getUserBy(Long id) {
        return userRepository.getOne(id);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private User toEntity(UserDTO user) {
        if (user != null) {
            return modelMapper.map(user, User.class);
        } else {
            return null;
        }
    }

    private UserDTO toDto(User user){
        if(user != null){
            return modelMapper.map(user, UserDTO.class);
        }else{
            return null;
        }
    }
}
