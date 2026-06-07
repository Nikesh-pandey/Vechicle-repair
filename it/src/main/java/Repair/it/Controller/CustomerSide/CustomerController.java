package Repair.it.Controller.CustomerSide;

import Repair.it.Dtos.CustomerSide.*;
import Repair.it.Dtos.Request.CustomerConfirmDto;
import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Services.CustomerSide.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cu")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/find")
    public ResponseEntity<?> returnDistance(@Valid @RequestBody CustomerRequestDto customerRequestDto) {
        return customerService.SearchGarage(customerRequestDto);
    }

    @PostMapping("/confirmRequest/{garageId}")
    public ResponseEntity<CreateRequestResponseDto> confirmrequestbyCustomer(
            @Valid @RequestBody CustomerConfirmDto customerConfirmDto,
            @PathVariable Long garageId) {
        return ResponseEntity.ok(customerService.customerRequest(customerConfirmDto, garageId));
    }

    @GetMapping("/myRequests")
    public ResponseEntity<List<MyRequestDto>> getMyRequests() {
        return ResponseEntity.ok(customerService.getMyRequests());
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelRequest(@PathVariable Long id) {
        customerService.cancelRequest(id);
        return ResponseEntity.ok("Request cancelled successfully");
    }

    @PostMapping("/review/{requestId}")
    public ResponseEntity<ReviewResponseDto> submitReview(
            @PathVariable Long requestId,
            @Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        return ResponseEntity.ok(customerService.submitReview(requestId, reviewRequestDto));
    }

    @GetMapping("/reviews/{garageId}")
    public ResponseEntity<List<ReviewResponseDto>> getGarageReviews(@PathVariable Long garageId) {
        return ResponseEntity.ok(customerService.getGarageReviews(garageId));
    }

    @GetMapping("/responsefromoperator")
    public ResponseEntity<List<FinalResponseDto>> getOperatorResponse() {
        return ResponseEntity.ok(customerService.getOperatorResponse());
    }

    @PostMapping("/payment/{id}")
    public ResponseEntity<?> payment(@RequestBody PaymentDto paymentDto, @PathVariable Long id) {
        return ResponseEntity.ok(customerService.seePrice(paymentDto, id));
    }
}
