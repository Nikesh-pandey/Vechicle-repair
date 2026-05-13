package Repair.it.Controller.CustomerSide;


import Repair.it.Dtos.Request.CustomerConfirmDto;
import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Services.CustomerSide.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


@PostMapping("/confirmRequest/{operatorId}")
    public  ResponseEntity<?> confirmrequestbyCustomer(@RequestBody CustomerConfirmDto customerConfirmDto, @PathVariable Long operatorId){
return customerService.customerRequest(customerConfirmDto,operatorId);

}




}
