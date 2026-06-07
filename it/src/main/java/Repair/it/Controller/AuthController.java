package Repair.it.Controller;


import Repair.it.Dtos.User.UserLoginDto;
import Repair.it.Dtos.UserRegisterDto;
import Repair.it.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto){
        return new ResponseEntity<>(userService.register(userRegisterDto), HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserLoginDto userLoginDto){
        String jwt= userService.login(userLoginDto);
        Map<String,String > response= new HashMap<>();
        response.put("Jwt",jwt);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
