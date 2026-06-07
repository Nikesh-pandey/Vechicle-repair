package Repair.it.Services.OperatorSide;

import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.*;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.RequestStatusHistory;
import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
import Repair.it.Repository.RequestHistory.RequestStatusHistoryRepository;
import Repair.it.Repository.ReviewSide.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class OperatorService {

    private final OperatorRepository operatorRepository;
    private final CustomerRequestRepository customerRequestRepository;
    private final RequestStatusHistoryRepository requestStatusHistoryRepository;
    private final ReviewRepository reviewRepository;

    private static final double ADMIN_COMMISSION_RATE = 0.10;

    public String registerOperator(OperatorRegisterDtos operatorRegisterDtos, MultipartFile shopImageUrl,
                                   HttpServletRequest request) throws IOException {
        if (shopImageUrl == null || shopImageUrl.isEmpty()) {
            throw new RuntimeException("Shop image is required");
        }

        String uploadDir = "uploads/shop-images/";
        String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID()
                + "_" + shopImageUrl.getOriginalFilename();

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        Files.write(Paths.get(uploadDir + uniqueFilename), shopImageUrl.getBytes());

        String imageUrl = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + "/uploads/shop-images/" + uniqueFilename;

        User operator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        OperatorGarageRegisterSide garage = new OperatorGarageRegisterSide();
        garage.setOperator(operator);
        garage.setShopName(operatorRegisterDtos.getShopName());
        garage.setAddress(operatorRegisterDtos.getAddress());
        garage.setLatitude(operatorRegisterDtos.getLatitude());
        garage.setLongitude(operatorRegisterDtos.getLongitude());
        garage.setType(operatorRegisterDtos.getType());
        garage.setPhNumber(operatorRegisterDtos.getPhNumber());
        garage.setShopImageUrl(imageUrl);
        garage.setStatus(RegisterStatus.PENDING);
        operatorRepository.save(garage);

        return "Your request is submitted. You will be notified within 2 hours.";
    }

    public AdminResponseDto getUpdate(Long id) {
        OperatorGarageRegisterSide garage = operatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + id));
        AdminResponseDto dto = new AdminResponseDto();
        dto.setStatus(garage.getStatus());
        dto.setMessage(garage.getMessage());
        return dto;
    }

    public OperatorStatus getownData() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatorGarageRegisterSide garage = operatorRepository.findByOperator_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("No garage found for this operator"));
        OperatorStatus status = new OperatorStatus();
        status.setStatus(garage.getStatus().toString());
        return status;
    }

    public GarageDetailDto getMyGarage() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatorGarageRegisterSide garage = operatorRepository.findByOperator_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("No garage found. Please register your garage first."));
        Double avgRating = reviewRepository.findAverageRatingByGarageId(garage.getId());
        long completedCount = customerRequestRepository.findBystaus(user.getId()).size();
        return mapToGarageDetailDto(garage, avgRating, completedCount);
    }

    public GarageDetailDto updateGarage(UpdateGarageDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatorGarageRegisterSide garage = operatorRepository.findByOperator_Id(user.getId())
                .orElseThrow(() -> new RuntimeException("No garage found for this operator"));

        garage.setShopName(dto.getShopName());
        garage.setAddress(dto.getAddress());
        garage.setLatitude(dto.getLatitude());
        garage.setLongitude(dto.getLongitude());
        garage.setPhNumber(dto.getPhNumber());
        operatorRepository.save(garage);

        Double avgRating = reviewRepository.findAverageRatingByGarageId(garage.getId());
        long completedCount = customerRequestRepository.findBystaus(user.getId()).size();
        return mapToGarageDetailDto(garage, avgRating, completedCount);
    }



    private GarageDetailDto mapToGarageDetailDto(OperatorGarageRegisterSide garage, Double avgRating, long completedCount) {
        GarageDetailDto dto = new GarageDetailDto();
        dto.setId(garage.getId());
        dto.setShopName(garage.getShopName());
        dto.setAddress(garage.getAddress());
        dto.setLatitude(garage.getLatitude());
        dto.setLongitude(garage.getLongitude());
        dto.setType(garage.getType());
        dto.setPhNumber(garage.getPhNumber());
        dto.setShopImageUrl(garage.getShopImageUrl());
        dto.setStatus(garage.getStatus());
        dto.setMessage(garage.getMessage());
        dto.setCreatedAt(garage.getCreatedAt());
        dto.setAverageRating(avgRating);
        dto.setTotalCompletedRequests(completedCount);
        return dto;
    }

    public ResponseEntity<?> responseToCustomer() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CustomerRequestEntity> requests = customerRequestRepository.findAllByLoggedInUserId(user.getId());
        List<GetcustomerRequest> result = new ArrayList<>();

        for (CustomerRequestEntity cu : requests) {
            GetcustomerRequest dto = new GetcustomerRequest();
            dto.setId(cu.getId());
            dto.setName(cu.getCustomer().getName());
            dto.setPhoneNumber(cu.getCustomer().getPhoneNumber());
            dto.setType(cu.getVechicleType());
            dto.setLatitude(cu.getLatitude());
            dto.setLongitude(cu.getLongitude());
            dto.setDescription(cu.getDescription());
            dto.setImage(cu.getImage());
            dto.setCustomerid(cu.getCustomer().getId().toString());
            dto.setStatus(cu.getStatus().toString());
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    public List<ActiveRequestDto> getActiveRequests() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CustomerRequestEntity> requests = customerRequestRepository.findActiveByOperatorId(user.getId());
        List<ActiveRequestDto> result = new ArrayList<>();

        for (CustomerRequestEntity req : requests) {
            ActiveRequestDto dto = new ActiveRequestDto();
            dto.setId(req.getId());
            dto.setCustomerName(req.getCustomer().getName());
            dto.setCustomerPhone(req.getCustomer().getPhoneNumber());
            dto.setCustomerLat(req.getLatitude());
            dto.setCustomerLng(req.getLongitude());
            dto.setVehicleType(req.getVechicleType());
            dto.setDescription(req.getDescription());
            dto.setImage(req.getImage());
            dto.setStatus(req.getStatus());
            dto.setPrice(req.getPrice());
            dto.setAcceptedAt(req.getAcceptedAt());
            dto.setCreatedAt(req.getCreatedAt());
            result.add(dto);
        }
        return result;
    }

    public OperatusStatusResponse responseGiven(OperatorAcceptRequest operatorAcceptRequest, Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CustomerRequestEntity request = customerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));

        validateStatusTransition(request.getStatus(), operatorAcceptRequest.getStatus());

        request.setStatus(operatorAcceptRequest.getStatus());
        request.setMessage(operatorAcceptRequest.getMessage());
        request.setPrice(operatorAcceptRequest.getPrice());

        if (request.getStatus() == RegisterStatus.ACCEPTED) {
            request.setAcceptedAt(LocalDateTime.now());
        } else if (request.getStatus() == RegisterStatus.ON_THE_WAY) {
            request.setOnTheWayAt(LocalDateTime.now());
        } else if (request.getStatus() == RegisterStatus.ARRIVED) {
            request.setArrivedAt(LocalDateTime.now());
        } else if (request.getStatus() == RegisterStatus.IN_PROGRESS) {
            request.setInProgressAt(LocalDateTime.now());
        } else if (request.getStatus() == RegisterStatus.COMPLETED) {
            request.setCompletedAt(LocalDateTime.now());
            if (request.getPrice() != null) {
                request.setAdminCommission(request.getPrice() * ADMIN_COMMISSION_RATE);
            }
        }

        customerRequestRepository.save(request);

        RequestStatusHistory history = new RequestStatusHistory();
        history.setOperator(user);
        history.setCustomer(request.getCustomer());
        history.setRequest(request);
        history.setAction(request.getStatus());
        requestStatusHistoryRepository.save(history);

        OperatusStatusResponse response = new OperatusStatusResponse();
        response.setName(user.getName());
        response.setPhnumber(user.getPhoneNumber());
        response.setMessage(request.getMessage());
        response.setCustomerLat(request.getLatitude());
        response.setCustomerLng(request.getLongitude());
        response.setGarageLat(request.getOperator().getLatitude());
        response.setGarageLng(request.getOperator().getLongitude());
        response.setPrice(request.getPrice());
        response.setAdminCommission(request.getAdminCommission());
        return response;
    }

    public List<OperatorCompletedRequest> getAllCOmpletedData(Long id) {
        List<CustomerRequestEntity> requests = customerRequestRepository.findBystaus(id);
        List<OperatorCompletedRequest> result = new ArrayList<>();
        for (CustomerRequestEntity cu : requests) {
            OperatorCompletedRequest dto = new OperatorCompletedRequest();
            dto.setCustomerName(cu.getCustomer().getName());
            dto.setCustomerPhone(cu.getCustomer().getPhoneNumber());
            dto.setVehicleType(cu.getVechicleType().toString());
            dto.setDescription(cu.getDescription());
            dto.setMessage(cu.getMessage());
            dto.setCreatedAt(cu.getCreatedAt());
            result.add(dto);
        }
        return result;
    }

    public EarningsDto getEarnings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Double totalTransaction = customerRequestRepository.findTotalPrice(user.getId());
        if (totalTransaction == null) totalTransaction = 0.0;
        Double adminCommission = totalTransaction * ADMIN_COMMISSION_RATE;
        Double totalIncome = totalTransaction - adminCommission;
        EarningsDto dto = new EarningsDto();
        dto.setTotalTransaction(totalTransaction);
        dto.setAdminCommission(adminCommission);
        dto.setTotalIncome(totalIncome);
        return dto;
    }

    public String deleteRequest(Long id) {
        CustomerRequestEntity request = customerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        request.setStatus(RegisterStatus.CANCELLED);
        customerRequestRepository.save(request);
        return "Request has been cancelled";
    }

    private void validateStatusTransition(RegisterStatus current, RegisterStatus next) {
        if (current == RegisterStatus.PENDING && next != RegisterStatus.ACCEPTED && next != RegisterStatus.CANCELLED) {
            throw new RuntimeException("Invalid status transition: PENDING can only move to ACCEPTED or CANCELLED");
        }
        if (current == RegisterStatus.ACCEPTED && next != RegisterStatus.ON_THE_WAY && next != RegisterStatus.CANCELLED) {
            throw new RuntimeException("Invalid status transition: ACCEPTED can only move to ON_THE_WAY or CANCELLED");
        }
        if (current == RegisterStatus.ON_THE_WAY && next != RegisterStatus.ARRIVED) {
            throw new RuntimeException("Invalid status transition: ON_THE_WAY can only move to ARRIVED");
        }
        if (current == RegisterStatus.ARRIVED && next != RegisterStatus.IN_PROGRESS) {
            throw new RuntimeException("Invalid status transition: ARRIVED can only move to IN_PROGRESS");
        }
        if (current == RegisterStatus.IN_PROGRESS && next != RegisterStatus.COMPLETED) {
            throw new RuntimeException("Invalid status transition: IN_PROGRESS can only move to COMPLETED");
        }
    }
}
