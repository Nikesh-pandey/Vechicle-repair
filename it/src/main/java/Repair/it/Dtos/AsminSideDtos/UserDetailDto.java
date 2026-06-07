package Repair.it.Dtos.AsminSideDtos;

import Repair.it.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Role role;
    private boolean active;
}
