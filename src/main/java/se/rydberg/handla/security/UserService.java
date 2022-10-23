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

    public HandlaUser savenew(UserDTO userDto) {
        HandlaUser handlaUser = toEntity(userDto);
        if(handlaUser !=null) {
            Role role = roleService.getNormalUserRole();
            handlaUser.addRole(role);
            handlaUser.setEnabled(true);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String losen = "{bcrypt}" + bCryptPasswordEncoder.encode(handlaUser.getPassword());
            handlaUser.setPassword(losen);

            return userRepository.save(handlaUser);
        }else{
            return null;
        }
    }

    public UserDTO getUserBy(Long id) {
        HandlaUser user = userRepository.getReferenceById(id);
        return toDto(user);
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public List<HandlaUser> getAllUsers() {
        return userRepository.findAll();
    }

    private HandlaUser toEntity(UserDTO user) {
        if (user != null) {
            return modelMapper.map(user, HandlaUser.class);
        } else {
            return null;
        }
    }

    private UserDTO toDto(HandlaUser handlaUser){
        if(handlaUser != null){
            return modelMapper.map(handlaUser, UserDTO.class);
        }else{
            return null;
        }
    }
}
