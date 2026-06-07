package Repair.it.Controller.AdminSideController;

import Repair.it.Dtos.AsminSideDtos.*;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Services.AdminSide.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboard());
    }

    @GetMapping("/operatorData")
    public ResponseEntity<List<OperatorGarageRegisterSide>> getPendingOperators() {
        return ResponseEntity.ok(adminService.getOperatorData());
    }

    @PatchMapping("/responsetoOperator/{id}")
    public ResponseEntity<AdminResponseDto> respondToOperator(
            @PathVariable Long id,
            @Valid @RequestBody AdminResponseDto adminResponseDto) {
        return ResponseEntity.ok(adminService.statusUpdate(id, adminResponseDto));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAllHistory() {
        return ResponseEntity.ok(adminService.getAllHistory());
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(adminService.getUsersDetails(role, page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetailDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<UserStatusDto> toggleUserStatus(
            @PathVariable Long id,
            @RequestBody UserStatusDto statusDto) {
        return ResponseEntity.ok(adminService.toggleUserStatus(id, statusDto));
    }

    @GetMapping("/garages")
    public ResponseEntity<?> getGarages(@RequestParam(required = false) String type) {
        return ResponseEntity.ok(adminService.getAllGarageList(type));
    }

    @GetMapping("/requests")
    public ResponseEntity<?> getAllRequests(
            HttpServletRequest httpRequest,
            @RequestParam(required = false) String status,
            @RequestParam(value = "Vechicle_Type", required = false) String vehicleType,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long garageId,
            @RequestParam(required = false) Integer adminCommission,
            @RequestParam(required = false) Integer price,
            @RequestParam(required = false) Integer paid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllRequest(
                status, vehicleType, customerId, garageId, adminCommission, price, paid, page, size));
    }
}
