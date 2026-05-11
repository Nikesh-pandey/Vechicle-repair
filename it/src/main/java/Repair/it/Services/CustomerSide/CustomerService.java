package Repair.it.Services.CustomerSide;

import Repair.it.Dtos.CustomerSide.CustomerRequestDto;
import Repair.it.Dtos.CustomerSide.CustomerResponseDto;
import Repair.it.Dtos.CustomerSide.RequestServiceDto;
import Repair.it.Entity.OperatorSide.OperatorRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final OperatorRepository operatorRepository;
    private static  final String  Uploads="static/uploads/";

    public ResponseEntity<?> SearchGarage(CustomerRequestDto customerRequestDto) {


        try {
            List<OperatorRegisterSide> operatorRegisterSides = operatorRepository.findAll();


            double customerLat = customerRequestDto.getLatitude();
            double customerLong = customerRequestDto.getLongitude();
            List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
            double range = customerRequestDto.getRange();


            for (OperatorRegisterSide opsides : operatorRegisterSides) {
                if (RegisterStatus.APPROVED.equals(opsides.getStatus())) {
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
                    CustomerResponseDto responseObj = new CustomerResponseDto();
                    responseObj.setOperatorName(opsides.getOperator().getName());
                    responseObj.setDistance(distance);

                    customerResponseDtos.add(responseObj);
                }
            }    return ResponseEntity.ok(customerResponseDtos);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }









}
