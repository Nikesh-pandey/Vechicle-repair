package Repair.it.Services.AdminSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.StatusHistory.StatusHistoryDto;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.RequestStatusHistory;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.RequestHistory.RequestStatusHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final OperatorRepository operatorRepository;
private final RequestStatusHistoryRepository requestStatusHistoryRepository;
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


    public List<StatusHistoryDto> getAllHistory() {
        List<RequestStatusHistory> histories = requestStatusHistoryRepository.findAllByOrderByTimestampDesc();
        List<StatusHistoryDto> dtos = new ArrayList<>();
        for(RequestStatusHistory h : histories) {
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


}
