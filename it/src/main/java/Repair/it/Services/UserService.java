package Repair.it.Services;


import Repair.it.Config.JwtService;
import Repair.it.Dtos.User.UserLoginDto;
import Repair.it.Dtos.UserRegisterDto;
import Repair.it.Entity.User;
import Repair.it.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserService {
private  final UserRepository userRepository;
private  final PasswordEncoder passwordEncoder;
private final AuthenticationManager authenticationManager;
private final JwtService jwtService;
public String register(UserRegisterDto userRegisterDto){

String encode= passwordEncoder.encode(userRegisterDto.getPassword());
userRegisterDto.setPassword(encode);

    User user= new User();
    user.setName(userRegisterDto.getName());
    user.setEmail(userRegisterDto.getEmail());
    user.setPassword(userRegisterDto.getPassword());
    user.setPhoneNumber(userRegisterDto.getPhoneNumber());
    user.setRole(userRegisterDto.getRole());
userRepository.save(user);
return user.getName() + "Data Saved Successfully";
}


public String login(UserLoginDto userLoginDto){
    System.out.println("check in Service");
    try{
 Authentication auth=   authenticationManager.authenticate(
         new   UsernamePasswordAuthenticationToken(
                    userLoginDto.getEmail(),
                    userLoginDto.getPassword()
                    )

    );
    System.out.println("catch2");
UserDetails userDetails= (UserDetails) auth.getPrincipal();
 String jwt= jwtService.generateToken(userDetails);

 return jwt;
    }
    catch (Exception e){
        System.out.println("Message"+ e.getMessage());
        e.printStackTrace();
        throw e;

    }


}




}
