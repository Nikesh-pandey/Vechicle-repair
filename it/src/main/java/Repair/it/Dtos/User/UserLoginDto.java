package Repair.it.Dtos.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginDto {
    private String email;
    @NotBlank(message = "Please enter password")
    @Size(min = 7)
    private String password;
}
