package Repair.it.Controller;

import Repair.it.Dtos.User.ChangePasswordDto;
import Repair.it.Dtos.User.UpdateProfileDto;
import Repair.it.Services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UpdateProfileDto dto) {
        userService.updateProfile(dto);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok("Password changed successfully");
    }
}
