package Repair.it.Services.AdminSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Entity.OperatorSide.OperatorRegisterSide;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Services.OperatorSide.OperatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final OperatorRepository operatorRepository;

    public ArrayList<OperatorRegisterSide> getOperatorData() {
        ArrayList<OperatorRegisterSide> OperatorDetails = new ArrayList<>();
        List<OperatorRegisterSide> OperatorSide = operatorRepository.findAll();


        for (OperatorRegisterSide opside : OperatorSide) {
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
        OperatorRegisterSide operatorRegisterSide = operatorRepository.findById(id).orElseThrow(() -> new RuntimeException("Didnot find the User with this id"));

        operatorRegisterSide.setStatus(adminResponseDto.getStatus());
        operatorRegisterSide.setMessage(adminResponseDto.getMessage());

        operatorRepository.save(operatorRegisterSide);
return adminResponseDto;
    }

}
