package Repair.it.Controller.CustomerSide;


import Repair.it.Dtos.CustomerSide.CreateRequestResponseDto;
import Repair.it.Dtos.CustomerSide.FinalResponseDto;
import Repair.it.Dtos.CustomerSide.PaymentDto;
import Repair.it.Dtos.Request.CustomerConfirmDto;
import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Services.CustomerSide.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cu")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/find")
    public ResponseEntity<?> returnDistance(@RequestBody CustomerRequestDto customerRequestDto){
        System.out.println("haha");
        return customerService.SearchGarage(customerRequestDto);
    }



    @PostMapping("/confirmRequest/{garageId}")
    public ResponseEntity<CreateRequestResponseDto> confirmrequestbyCustomer(
            @RequestBody CustomerConfirmDto customerConfirmDto,
            @PathVariable Long garageId
    ) {
        CreateRequestResponseDto response = customerService.customerRequest(customerConfirmDto, garageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/responsefromoperator")
    public ResponseEntity<List<FinalResponseDto>> getOperatorResponse(){
        List<FinalResponseDto> response = customerService.getOperatorResponse();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/payment/{id}")
    public ResponseEntity<?> payment(@RequestBody PaymentDto paymentDto, @PathVariable Long id) {
        return ResponseEntity.ok(customerService.seePrice(paymentDto, id));
    }

}
