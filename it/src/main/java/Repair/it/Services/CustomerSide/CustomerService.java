package Repair.it.Services.CustomerSide;

import Repair.it.Dtos.CustomerSide.CreateRequestResponseDto;
import Repair.it.Dtos.CustomerSide.CustomerRequestProjection;
import Repair.it.Dtos.CustomerSide.CustomerResponseDto;
import Repair.it.Dtos.CustomerSide.FinalResponseDto;
import Repair.it.Dtos.Request.CustomerConfirmDto;
import Repair.it.Dtos.Request.CustomerRequestDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.OperatorSide.VechicleType;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.User;
import Repair.it.Repository.CustomerSide.CustomerRepository;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
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

    private static final String Uploads = "static/uploads/";

    public ResponseEntity<?> SearchGarage(CustomerRequestDto customerRequestDto) {
        try {
            List<OperatorGarageRegisterSide> operatorGarageRegisterSides = operatorRepository.findAll();
            double customerLat = customerRequestDto.getLatitude();
            double customerLong = customerRequestDto.getLongitude();
            List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
            double range = customerRequestDto.getRange();
            for (OperatorGarageRegisterSide opsides : operatorGarageRegisterSides) {
                if (RegisterStatus.APPROVED.equals(opsides.getStatus())) {
                    CustomerResponseDto responseObj = new CustomerResponseDto();
                    if (customerRequestDto.getVechicleType().equals(opsides.getType())) {
                        double operatorLatitude = opsides.getLatitude();
                        double operatorLongitude = opsides.getLongitude();
                        double earthRadius = 6371;
                        double latDistance = Math.toRadians(operatorLatitude - customerLat);
                        double longDistance = Math.toRadians(operatorLongitude - customerLong);
                        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                                + Math.cos(Math.toRadians(customerLat))
                                * Math.cos(Math.toRadians(operatorLatitude))
                                * Math.sin(longDistance / 2)
                                * Math.sin(longDistance / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = earthRadius * c;
                        if (distance > range) {
                            System.out.println(distance);
                            continue;
                        }
                        responseObj.setId(opsides.getId());
                        responseObj.setShopName(opsides.getShopName());
                        responseObj.setDistance(distance);
                        responseObj.setType(opsides.getType());
                        customerResponseDtos.add(responseObj);

                    }
                }
            }

            return ResponseEntity.ok(customerResponseDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CreateRequestResponseDto customerRequest(
            CustomerConfirmDto customerConfirmDto,
            Long garageId
    ) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        OperatorGarageRegisterSide operator = operatorRepository
                .findById(garageId)
                .orElseThrow(() -> new RuntimeException("Operator not found"));

        CustomerRequestEntity customerRequestEntity = new CustomerRequestEntity();
        customerRequestEntity.setCustomer(user);
        customerRequestEntity.setOperator(operator);
        customerRequestEntity.setVechicleType(customerConfirmDto.getVechicleType());
        customerRequestEntity.setLatitude(customerConfirmDto.getLatitude());
        customerRequestEntity.setLongitude(customerConfirmDto.getLongitude());
        customerRequestEntity.setDescription(customerConfirmDto.getDescription());
        customerRequestEntity.setImage(customerConfirmDto.getImage());

        customerRequestRepository.save(customerRequestEntity);

        CreateRequestResponseDto response = new CreateRequestResponseDto();
        response.setCustomer(customerRequestEntity.getCustomer());
        response.setOperator(customerRequestEntity.getOperator());
        response.setLatitude(customerRequestEntity.getLatitude());
        response.setLongitude(customerRequestEntity.getLongitude());
        response.setDescription(customerRequestEntity.getDescription());
        response.setImage(customerRequestEntity.getImage());

        return response;
    }


    public List<FinalResponseDto> getOperatorResponse(){

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();



        List<CustomerRequestProjection> customerRequestProjection= customerRepository.findOperatorIdByCustomerId(user.getId());


        List<FinalResponseDto> finalResponseDtos= new ArrayList<>();

        for(CustomerRequestProjection cur:customerRequestProjection){
            FinalResponseDto finalResponseDto= new FinalResponseDto();
            finalResponseDto.setName(cur.getName());
            finalResponseDto.setPhnumber(cur.getPhoneNumber());
            finalResponseDto.setMessage(cur.getMessage());
            finalResponseDto.setStatus(cur.getStatus());

            finalResponseDtos.add(finalResponseDto);

        }

return finalResponseDtos;

    }


}
