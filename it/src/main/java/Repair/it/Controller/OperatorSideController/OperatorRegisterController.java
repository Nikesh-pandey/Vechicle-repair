package Repair.it.Controller.OperatorSideController;

import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.*;
import Repair.it.Services.OperatorSide.OperatorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/op")
@AllArgsConstructor
public class OperatorRegisterController {

    private final OperatorService operatorService;

    @PostMapping("/registerOperator")
    public ResponseEntity<String> registerOperator(
            @RequestPart("body") OperatorRegisterDtos operatorRegisterDtos,
            @RequestPart("file") MultipartFile shopImageUrl,
            HttpServletRequest request) throws IOException {
        return new ResponseEntity<>(operatorService.registerOperator(operatorRegisterDtos, shopImageUrl, request), HttpStatus.OK);
    }

    @GetMapping("/getUpdate/{id}")
    public ResponseEntity<AdminResponseDto> getResponse(@PathVariable Long id) {
        return ResponseEntity.ok(operatorService.getUpdate(id));
    }

    @GetMapping("/getOwnstatus")
    public ResponseEntity<?> getOwnStatus() {
        return ResponseEntity.ok(operatorService.getownData());
    }

    @GetMapping("/myGarage")
    public ResponseEntity<GarageDetailDto> getMyGarage() {
        return ResponseEntity.ok(operatorService.getMyGarage());
    }

    @PutMapping("/updateGarage")
    public ResponseEntity<GarageDetailDto> updateGarage(@Valid @RequestBody UpdateGarageDto dto) {
        return ResponseEntity.ok(operatorService.updateGarage(dto));
    }

    @GetMapping("/getCustomerrequest")
    public ResponseEntity<?> getCustomerRequest() {
        return operatorService.responseToCustomer();
    }

    @GetMapping("/activeRequests")
    public ResponseEntity<List<ActiveRequestDto>> getActiveRequests() {
        return ResponseEntity.ok(operatorService.getActiveRequests());
    }

    @PatchMapping("/responseStatus/{id}")
    public ResponseEntity<?> updateToCustomer(
            @RequestBody OperatorAcceptRequest operatorAcceptRequest,
            @PathVariable Long id) {
        return ResponseEntity.ok(operatorService.responseGiven(operatorAcceptRequest, id));
    }

    @GetMapping("/completed/{id}")
    public ResponseEntity<?> getCompletedRequests(@PathVariable Long id) {
        return ResponseEntity.ok(operatorService.getAllCOmpletedData(id));
    }

    @GetMapping("/earnings")
    public ResponseEntity<?> getEarnings() {
        return ResponseEntity.ok(operatorService.getEarnings());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRequest(@RequestParam Long id) {
        return ResponseEntity.ok(operatorService.deleteRequest(id));
    }
}
