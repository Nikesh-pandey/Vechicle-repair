package Repair.it.Controller.AdminSideController;

import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Services.AdminSide.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/operatorData")
    public ResponseEntity<List<OperatorGarageRegisterSide>> getopData(){
try{
        System.out.println("check from admin2");
        return new ResponseEntity<>(adminService.getOperatorData(), HttpStatus.OK);
}
catch (Exception e) {
    System.out.println(e);
    e.printStackTrace();
    throw e;
}

    }

    @PatchMapping("/responsetoOperator/{id}")
    public ResponseEntity<?> ResponseToOperator(
            @PathVariable Long id,
            @RequestBody AdminResponseDto adminResponseDto){
        return new ResponseEntity<>(adminService.statusUpdate(id,adminResponseDto),HttpStatus.OK);
    }





}
