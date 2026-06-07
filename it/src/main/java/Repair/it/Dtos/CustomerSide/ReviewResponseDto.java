package Repair.it.Dtos.CustomerSide;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long id;
    private String customerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
