package Repair.it.Services.CustomerSide;

import Repair.it.Dtos.CustomerSide.*;
import Repair.it.Dtos.Request.CustomerConfirmDto;
import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.Review.ReviewEntity;
import Repair.it.Entity.User;
import Repair.it.Repository.CustomerSide.CustomerRepository;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
import Repair.it.Repository.ReviewSide.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {

    private final OperatorRepository operatorRepository;
    private final CustomerRequestRepository customerRequestRepository;
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;

    public ResponseEntity<?> SearchGarage(CustomerRequestDto customerRequestDto) {
        List<OperatorGarageRegisterSide> allGarages = operatorRepository.findAll();
        double customerLat = customerRequestDto.getLatitude();
        double customerLng = customerRequestDto.getLongitude();
        double range = customerRequestDto.getRange();
        List<CustomerResponseDto> result = new ArrayList<>();

        for (OperatorGarageRegisterSide garage : allGarages) {
            if (!RegisterStatus.APPROVED.equals(garage.getStatus())) continue;
            if (!customerRequestDto.getVechicleType().equals(garage.getType())) continue;

            double latDiff = Math.toRadians(garage.getLatitude() - customerLat);
            double lngDiff = Math.toRadians(garage.getLongitude() - customerLng);
            double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                    + Math.cos(Math.toRadians(customerLat))
                    * Math.cos(Math.toRadians(garage.getLatitude()))
                    * Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
            double distance = 6371 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            if (distance > range) continue;

            CustomerResponseDto dto = new CustomerResponseDto();
            dto.setId(garage.getId());
            dto.setShopName(garage.getShopName());
            dto.setDistance(distance);
            dto.setType(garage.getType());
            result.add(dto);
        }
        return ResponseEntity.ok(result);
    }

    public CreateRequestResponseDto customerRequest(CustomerConfirmDto customerConfirmDto, Long garageId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatorGarageRegisterSide operator = operatorRepository.findById(garageId)
                .orElseThrow(() -> new RuntimeException("Garage not found with id: " + garageId));

        CustomerRequestEntity entity = new CustomerRequestEntity();
        entity.setCustomer(user);
        entity.setOperator(operator);
        entity.setVechicleType(customerConfirmDto.getVechicleType());
        entity.setLatitude(customerConfirmDto.getLatitude());
        entity.setLongitude(customerConfirmDto.getLongitude());
        entity.setDescription(customerConfirmDto.getDescription());
        entity.setImage(customerConfirmDto.getImage());
        customerRequestRepository.save(entity);

        CreateRequestResponseDto response = new CreateRequestResponseDto();
        response.setCustomer(entity.getCustomer());
        response.setOperator(entity.getOperator());
        response.setLatitude(entity.getLatitude());
        response.setLongitude(entity.getLongitude());
        response.setDescription(entity.getDescription());
        response.setImage(entity.getImage());
        return response;
    }

    public List<MyRequestDto> getMyRequests() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CustomerRequestEntity> requests = customerRepository.findByCustomerId(user.getId());
        List<MyRequestDto> result = new ArrayList<>();

        for (CustomerRequestEntity req : requests) {
            MyRequestDto dto = new MyRequestDto();
            dto.setId(req.getId());
            dto.setGarageName(req.getOperator().getShopName());
            dto.setGaragePhone(req.getOperator().getPhNumber());
            dto.setGarageAddress(req.getOperator().getAddress());
            dto.setGarageImage(req.getOperator().getShopImageUrl());
            dto.setVehicleType(req.getVechicleType());
            dto.setDescription(req.getDescription());
            dto.setImage(req.getImage());
            dto.setStatus(req.getStatus());
            dto.setMessage(req.getMessage());
            dto.setPrice(req.getPrice());
            dto.setPaid(req.isPaid());
            dto.setCreatedAt(req.getCreatedAt());
            dto.setReviewed(reviewRepository.existsByRequestId(req.getId()));
            result.add(dto);
        }
        return result;
    }

    public void cancelRequest(Long requestId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerRequestEntity request = customerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        if (!request.getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own request");
        }
        if (request.getStatus() != RegisterStatus.PENDING) {
            throw new RuntimeException("Request can only be cancelled when status is PENDING");
        }
        request.setStatus(RegisterStatus.CANCELLED);
        customerRequestRepository.save(request);
    }

    public ReviewResponseDto submitReview(Long requestId, ReviewRequestDto dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomerRequestEntity request = customerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        if (!request.getCustomer().getId().equals(user.getId())) {
            throw new RuntimeException("You can only review your own request");
        }
        if (request.getStatus() != RegisterStatus.COMPLETED) {
            throw new RuntimeException("You can only submit a review after the service is COMPLETED");
        }
        if (reviewRepository.existsByRequestId(requestId)) {
            throw new RuntimeException("You have already reviewed this request");
        }

        ReviewEntity review = new ReviewEntity();
        review.setCustomer(user);
        review.setGarage(request.getOperator());
        review.setRequest(request);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        reviewRepository.save(review);

        ReviewResponseDto response = new ReviewResponseDto();
        response.setId(review.getId());
        response.setCustomerName(user.getName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }

    public List<ReviewResponseDto> getGarageReviews(Long garageId) {
        List<ReviewEntity> reviews = reviewRepository.findByGarageId(garageId);
        List<ReviewResponseDto> result = new ArrayList<>();
        for (ReviewEntity review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto();
            dto.setId(review.getId());
            dto.setCustomerName(review.getCustomer().getName());
            dto.setRating(review.getRating());
            dto.setComment(review.getComment());
            dto.setCreatedAt(review.getCreatedAt());
            result.add(dto);
        }
        return result;
    }

    public List<FinalResponseDto> getOperatorResponse() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CustomerRequestProjection> projections = customerRepository.findOperatorIdByCustomerId(user.getId());
        List<FinalResponseDto> result = new ArrayList<>();
        for (CustomerRequestProjection cur : projections) {
            FinalResponseDto dto = new FinalResponseDto();
            dto.setId(cur.getId());
            dto.setName(cur.getName());
            dto.setPhnumber(cur.getPhoneNumber());
            dto.setMessage(cur.getMessage());
            dto.setStatus(cur.getStatus());
            dto.setPrice(cur.getPrice());
            dto.setPaid(cur.getPaid());
            result.add(dto);
        }
        return result;
    }

    public PaymentDto seePrice(PaymentDto paymentDto, Long id) {
        CustomerRequestEntity request = customerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + id));
        if (paymentDto.getAmount() < request.getPrice()) {
            throw new RuntimeException("Amount is less than the required price of " + request.getPrice());
        }
        request.setPaid(true);
        customerRequestRepository.save(request);
        return paymentDto;
    }
}
