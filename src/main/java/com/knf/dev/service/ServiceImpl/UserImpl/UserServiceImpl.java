package com.knf.dev.service.ServiceImpl.UserImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.knf.dev.model.Role.UserRole;
import com.knf.dev.service.Service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.knf.dev.dto.UserRegistrationDto;
import com.knf.dev.model.User.User;
import com.knf.dev.repository.UserRepo.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto, UserRole role) {
        User user = new User(registrationDto.getFirstName(), registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),UserRole.USER);
        return userRepository.save(user);
    }

    @Override
    public void saveUser(UserRegistrationDto userDto) {
        System.out.println(">>>>> inside");
        Optional<User> optionalUser = userRepository.findById(userDto.getUserId());
        if(optionalUser.isPresent()){
            User existUser   =optionalUser.get();
            existUser.setFirstName(userDto.getFirstName());
            existUser.setLastName(userDto.getLastName());
            existUser.setEmail(userDto.getEmail());
            this.userRepository.save(existUser);
        }else {
            // new user
            User savedUser = new User(userDto.getFirstName(), userDto.getLastName(),userDto.getEmail(),userDto.getPassword(),UserRole.USER);
            this.userRepository.save(savedUser);

        }
    }

    @Override
    public void deleteUserById(long userId) {
         this.userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = null;

        if (userOptional.isPresent()){
            user = userOptional.get();
        }else {
            throw new RuntimeException("User not found for id ::" +userId);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRoleToAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> mapRoleToAuthorities(UserRole role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getByKeyword(String search) {
        System.out.println("input........." + search);
        return userRepository.findByKeyword(search);
    }


}
