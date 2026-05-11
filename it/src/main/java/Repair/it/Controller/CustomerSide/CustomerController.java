package Repair.it.Controller.CustomerSide;


import Repair.it.Dtos.CustomerSide.CustomerRequestDto;
import Repair.it.Services.CustomerSide.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cu")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/find")
    public ResponseEntity<?> returnDistance(@RequestBody  CustomerRequestDto customerRequestDto){
        System.out.println("haha");
        return customerService.SearchGarage(customerRequestDto);
    }






}
