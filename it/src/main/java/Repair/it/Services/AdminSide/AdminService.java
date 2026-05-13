package Repair.it.Services.AdminSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final OperatorRepository operatorRepository;

    public ArrayList<OperatorGarageRegisterSide> getOperatorData() {
        ArrayList<OperatorGarageRegisterSide> OperatorDetails = new ArrayList<>();
        List<OperatorGarageRegisterSide> OperatorSide = operatorRepository.findAll();


        for (OperatorGarageRegisterSide opside : OperatorSide) {
            if (opside.getStatus().toString() == "PENDING") {

                opside.getOperator();
                opside.getShopName();
                opside.getAddress();
                opside.getLatitude();
                opside.getLongitude();
                opside.getType();
                opside.getPhNumber();
                opside.getShopImageUrl();
                opside.getStatus();
                opside.getCreatedAt();
                OperatorDetails.add(opside);
            }
        }
        return OperatorDetails;

    }

    public AdminResponseDto statusUpdate(Long id, AdminResponseDto adminResponseDto) {

        OperatorGarageRegisterSide operatorGarageRegisterSide = operatorRepository.findById(id).orElseThrow(() -> new RuntimeException("Didnot find the User with this id"));
        operatorGarageRegisterSide.setStatus(adminResponseDto.getStatus());
        operatorGarageRegisterSide.setMessage(adminResponseDto.getMessage());
        operatorRepository.save(operatorGarageRegisterSide);
return adminResponseDto;
    }





}
