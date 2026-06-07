package Repair.it.Services.AdminSide;

import Repair.it.Dtos.AsminSideDtos.*;
import Repair.it.Dtos.StatusHistory.StatusHistoryDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.RequestStatusHistory;
import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
import Repair.it.Repository.RequestHistory.RequestStatusHistoryRepository;
import Repair.it.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {

    private final OperatorRepository operatorRepository;
    private final RequestStatusHistoryRepository requestStatusHistoryRepository;
    private final UserRepository userRepository;
    private final CustomerRequestRepository customerRequestRepository;

    public ArrayList<OperatorGarageRegisterSide> getOperatorData() {
        ArrayList<OperatorGarageRegisterSide> result = new ArrayList<>();
        List<OperatorGarageRegisterSide> all = operatorRepository.findAll();
        for (OperatorGarageRegisterSide garage : all) {
            if (garage.getStatus() == RegisterStatus.PENDING) {
                result.add(garage);
            }
        }
        return result;
    }

    public AdminResponseDto statusUpdate(Long id, AdminResponseDto adminResponseDto) {
        OperatorGarageRegisterSide garage = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + id));
        garage.setStatus(adminResponseDto.getStatus());
        garage.setMessage(adminResponseDto.getMessage());
        operatorRepository.save(garage);
        return adminResponseDto;
    }

    public List<StatusHistoryDto> getAllHistory() {
        List<RequestStatusHistory> histories = requestStatusHistoryRepository.findAllByOrderByTimestampDesc();
        List<StatusHistoryDto> dtos = new ArrayList<>();
        for (RequestStatusHistory h : histories) {
            StatusHistoryDto dto = new StatusHistoryDto();
            dto.setRequestId(h.getRequest().getId());
            dto.setOperatorName(h.getOperator().getName());
            dto.setOperatorPhone(h.getOperator().getPhoneNumber());
            dto.setCustomerName(h.getCustomer().getName());
            dto.setCustomerPhone(h.getCustomer().getPhoneNumber());
            dto.setAction(h.getAction().toString());
            dto.setTimestamp(h.getTimestamp());
            dto.setPrice(h.getRequest().getPrice());
            dto.setVehicleType(h.getRequest().getVechicleType().toString());
            dto.setDescription(h.getRequest().getDescription());
            dtos.add(dto);
        }
        return dtos;
    }

    public Page<User> getUsersDetails(String role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (role == null || role.isEmpty()) {
            return userRepository.findAll(pageable);
        }
        Role roleEnum = Role.valueOf(role.toUpperCase());
        return userRepository.findByrole(roleEnum, pageable);
    }

    public UserDetailDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        return dto;
    }

    public UserStatusDto toggleUserStatus(Long id, UserStatusDto statusDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setActive(statusDto.isActive());
        userRepository.save(user);
        return statusDto;
    }

    public List<OperatorGarageRegisterSide> getAllGarageList(String type) {
        if (type == null || type.trim().isEmpty() || type.trim().equalsIgnoreCase("ALL")) {
            return operatorRepository.findAll();
        }
        try {
            VechicleType vehicleType = VechicleType.valueOf(type.trim().toUpperCase());
            return operatorRepository.findAllByType(vehicleType);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid vehicle type: " + type);
        }
    }

    public DashboardDto getDashboard() {
        long totalCustomers = userRepository.countByRole(Role.CUSTOMER);
        long totalOperators = userRepository.countByRole(Role.OPERATOR);
        long totalUsers = totalCustomers + totalOperators;

        long totalGarages = operatorRepository.count();
        long pendingGarages = operatorRepository.countByStatus(RegisterStatus.PENDING);
        long approvedGarages = operatorRepository.countByStatus(RegisterStatus.APPROVED);
        long rejectedGarages = operatorRepository.countByStatus(RegisterStatus.REJECTED);

        long totalRequests = customerRequestRepository.count();
        long pendingRequests = customerRequestRepository.countByStatus(RegisterStatus.PENDING);
        long activeRequests = customerRequestRepository.countActiveRequests();
        long completedRequests = customerRequestRepository.countByStatus(RegisterStatus.COMPLETED);
        long cancelledRequests = customerRequestRepository.countByStatus(RegisterStatus.CANCELLED);

        Double totalRevenue = customerRequestRepository.findTotalRevenue();
        Double totalAdminCommission = customerRequestRepository.findTotalAdminCommission();

        DashboardDto dto = new DashboardDto();
        dto.setTotalUsers(totalUsers);
        dto.setTotalCustomers(totalCustomers);
        dto.setTotalOperators(totalOperators);
        dto.setTotalGarages(totalGarages);
        dto.setPendingGarages(pendingGarages);
        dto.setApprovedGarages(approvedGarages);
        dto.setRejectedGarages(rejectedGarages);
        dto.setTotalRequests(totalRequests);
        dto.setPendingRequests(pendingRequests);
        dto.setActiveRequests(activeRequests);
        dto.setCompletedRequests(completedRequests);
        dto.setCancelledRequests(cancelledRequests);
        dto.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        dto.setTotalAdminCommission(totalAdminCommission != null ? totalAdminCommission : 0.0);
        return dto;
    }

    public List<RequestTableResponse> getAllRequest(String status, String vehicleType, Long customerId,
                                                    Long garageId, Integer adminCommission, Integer price,
                                                    Integer paid, int page, int size) {
        status = (status != null && !status.isEmpty()) ? status.toUpperCase() : null;
        vehicleType = (vehicleType != null && !vehicleType.isEmpty()) ? vehicleType.toUpperCase() : null;
        customerId = (customerId != null && customerId > 0) ? customerId : null;
        garageId = (garageId != null && garageId > 0) ? garageId : null;
        adminCommission = (adminCommission != null && adminCommission > 0) ? adminCommission : null;
        price = (price != null && price > 0) ? price : null;
        paid = (paid != null && paid > 0) ? paid : null;

        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerRequestEntity> pageResult = customerRequestRepository.findByFilters(
                status, vehicleType, customerId, garageId, adminCommission, price, paid, pageable
        );

        List<RequestTableResponse> result = new ArrayList<>();
        pageResult.forEach(cur -> {
            RequestTableResponse response = new RequestTableResponse();
            response.setId(cur.getId());
            response.setCustomer(cur.getCustomer());
            response.setOperator(cur.getOperator());
            response.setVechicleType(cur.getVechicleType());
            response.setLatitude(cur.getLatitude());
            response.setLongitude(cur.getLongitude());
            response.setDescription(cur.getDescription());
            response.setStatus(cur.getStatus());
            response.setMessage(cur.getMessage());
            response.setPrice(cur.getPrice());
            response.setAdminCommission(cur.getAdminCommission());
            response.setCreatedAt(cur.getCreatedAt());
            response.setPaid(cur.isPaid());
            result.add(response);
        });
        return result;
    }
}
