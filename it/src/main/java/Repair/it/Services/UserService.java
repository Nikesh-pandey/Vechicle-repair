package Repair.it.Services;

import Repair.it.Config.JwtService;
import Repair.it.Dtos.User.ChangePasswordDto;
import Repair.it.Dtos.User.UpdateProfileDto;
import Repair.it.Dtos.User.UserLoginDto;
import Repair.it.Dtos.UserRegisterDto;
import Repair.it.Entity.User;
import Repair.it.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String register(UserRegisterDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        userRepository.save(user);
        return user.getName() + " registered successfully";
    }

    public String login(UserLoginDto dto) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return jwtService.generateToken(userDetails);
    }

    public void updateProfile(UpdateProfileDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
