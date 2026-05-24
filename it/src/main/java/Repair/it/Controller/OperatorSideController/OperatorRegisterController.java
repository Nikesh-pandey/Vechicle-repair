package Repair.it.Controller.OperatorSideController;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.OperatorAcceptRequest;
import Repair.it.Dtos.OperatorSideDtos.OperatorRegisterDtos;
import Repair.it.Dtos.OperatorSideDtos.OperatusStatusResponse;
import Repair.it.Entity.User;
import Repair.it.Services.OperatorSide.OperatorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/op")
@Validated
@AllArgsConstructor
public class OperatorRegisterController {
    private final OperatorService operatorService;
@PostMapping("/registerOperator")
    public ResponseEntity<String> OperatorRegister(
            @RequestPart("body")OperatorRegisterDtos operatorRegisterDtos,
            @RequestPart("file")MultipartFile shopImageUrl,
            HttpServletRequest request
            ) throws IOException {
    try {
        return new ResponseEntity<>(operatorService.registerOperator(operatorRegisterDtos, shopImageUrl, request), HttpStatus.OK);
    }
    catch (Exception e){
        System.out.println(e);
        e.printStackTrace();
        throw e;
    }
}
    @GetMapping("/getUpdate/{id}")
    public ResponseEntity<AdminResponseDto> getResponse(@PathVariable Long id){
    return new ResponseEntity<>(operatorService.getUpdate(id),HttpStatus.OK);
    }

    @GetMapping("/getOwnstatus")
    public ResponseEntity<?> getOwndata(){
    return ResponseEntity.ok(operatorService.getownData());
    }

    @GetMapping("/getCustomerrequest")
    public  ResponseEntity<?> getCustomerRequest(){
    return operatorService.responseToCustomer();
    }

    @PatchMapping("/responseStatus/{id}")
    public ResponseEntity<?> updateToCustomer( @RequestBody OperatorAcceptRequest operatorAcceptRequest,@PathVariable  Long id){
    return new ResponseEntity<>(operatorService.responseGiven(operatorAcceptRequest,id),HttpStatus.OK);

    }
//new
    @GetMapping("/completed/{id}")
    public ResponseEntity<?> getCompletedRequests(@PathVariable Long id) {
        return ResponseEntity.ok(operatorService.getAllCOmpletedData(id));
    }


    @GetMapping("/earnings")
    public ResponseEntity<?> getEarnings() {
        return ResponseEntity.ok(operatorService.getEarnings());
    }
}
