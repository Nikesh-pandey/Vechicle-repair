package Repair.it.Controller;


import Repair.it.Dtos.User.UserLoginDto;
import Repair.it.Dtos.UserRegisterDto;
import Repair.it.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto userRegisterDto){
        return new ResponseEntity<>(userService.register(userRegisterDto), HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody UserLoginDto userLoginDto){
        System.out.println("catched");
       String jwt= userService.login(userLoginDto);
Map<String,String > data= new HashMap<>();
data.put("jwt",jwt);
return new ResponseEntity<>(data, HttpStatus.OK);
    }

}
