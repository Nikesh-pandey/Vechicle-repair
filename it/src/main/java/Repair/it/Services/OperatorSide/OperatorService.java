package Repair.it.Services.OperatorSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.*;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.RequestStatusHistory;
import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import Repair.it.Repository.CustomerSide.CustomerRepository;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
import Repair.it.Repository.RequestHistory.RequestStatusHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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


    private static  final String  Uploads="static/uploads/";
    public String registerOperator(
            OperatorRegisterDtos operatorRegisterDtos,
            MultipartFile shopImageUrl,
            HttpServletRequest request
    ) throws IOException {

        if (shopImageUrl == null || shopImageUrl.isEmpty()) {
            return "File is Empty";
        }

        String uploadDir = "uploads/shop-images/";

        String originalName = shopImageUrl.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis()
                + "_"
                + UUID.randomUUID()
                + "_"
                + originalName;

        File uploads = new File(uploadDir);

        if (!uploads.exists()) {
            uploads.mkdirs();
        }

        String filePath = uploadDir + uniqueFilename;

        Files.write(Paths.get(filePath), shopImageUrl.getBytes());

        String imageUrl = request.getScheme() + "://"
                + request.getServerName() + ":"
                + request.getServerPort()
                + "/uploads/shop-images/"
                + uniqueFilename;

        User operator = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        OperatorGarageRegisterSide operatorGarageRegisterSide = new OperatorGarageRegisterSide();

        operatorGarageRegisterSide.setOperator(operator);
        operatorGarageRegisterSide.setShopName(operatorRegisterDtos.getShopName());
        operatorGarageRegisterSide.setAddress(operatorRegisterDtos.getAddress());
        operatorGarageRegisterSide.setLatitude(operatorRegisterDtos.getLatitude());
        operatorGarageRegisterSide.setLongitude(operatorRegisterDtos.getLongitude());
        operatorGarageRegisterSide.setType(operatorRegisterDtos.getType());
        operatorGarageRegisterSide.setPhNumber(operatorRegisterDtos.getPhNumber());
        operatorGarageRegisterSide.setShopImageUrl(imageUrl);
        operatorGarageRegisterSide.setStatus(RegisterStatus.PENDING);
        operatorRepository.save(operatorGarageRegisterSide);

        return "Your request is submitted, You will be notified within 2 hrs";
    }



    public AdminResponseDto getUpdate(Long id){
      OperatorGarageRegisterSide operator=  operatorRepository.findById(id).orElseThrow(()-> new RuntimeException("Didnot find user with this id"));
AdminResponseDto adminResponseDto = new AdminResponseDto();
adminResponseDto.setStatus(operator.getStatus());
adminResponseDto.setMessage(operator.getMessage());
return adminResponseDto;
    }



    //New Api added

    public OperatorStatus getownData() {

        User op = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!op.getRole().equals(Role.OPERATOR)) {

            System.out.println("only Operator can get access of this ");
        }
            OperatorGarageRegisterSide operatorGarageRegisterSide = operatorRepository.findByOperator_Id(op.getId()).orElseThrow(() -> new RuntimeException("no id found"));
        OperatorStatus operatorStatus = new OperatorStatus();
operatorStatus.setStatus(operatorGarageRegisterSide.getStatus().toString());
return operatorStatus;
    }




    public ResponseEntity<?> responseToCustomer(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      List<  GetcustomerRequest> getcustomerRequest= new ArrayList<>();
       List<CustomerRequestEntity> customerRequestEntity =customerRequestRepository.findAllByLoggedInUserId(user.getId());
               System.out.println("checking this"+user.getId());

        for(CustomerRequestEntity cu:customerRequestEntity) {

                GetcustomerRequest getcustomerRequest1 = new GetcustomerRequest();
getcustomerRequest1.setId(cu.getId());
                            getcustomerRequest1.setName(cu.getCustomer().getName());
                getcustomerRequest1.setPhoneNumber(cu.getCustomer().getPhoneNumber());
                getcustomerRequest1.setType(cu.getVechicleType());
                getcustomerRequest1.setLatitude(cu.getLatitude());
                getcustomerRequest1.setLongitude(cu.getLongitude());
                getcustomerRequest1.setDescription(cu.getDescription());
                getcustomerRequest1.setImage(cu.getImage());
                getcustomerRequest1.setCustomerid(cu.getCustomer().getId().toString());
                getcustomerRequest1.setStatus(cu.getStatus().toString());
                getcustomerRequest.add(getcustomerRequest1);
            }
            return ResponseEntity.ok(getcustomerRequest);

    }



    public OperatusStatusResponse responseGiven(OperatorAcceptRequest operatorAcceptRequest, Long id) {
        System.out.println("Status received: " + operatorAcceptRequest.getStatus());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OperatusStatusResponse operatusStatusResponse = new OperatusStatusResponse();

        CustomerRequestEntity customerRequestEntity = customerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Didnot find with this id"));

        validateStatusTransition(customerRequestEntity.getStatus(), operatorAcceptRequest.getStatus());

        customerRequestEntity.setStatus(operatorAcceptRequest.getStatus());
        customerRequestEntity.setMessage(operatorAcceptRequest.getMessage());
        customerRequestEntity.setPrice(operatorAcceptRequest.getPrice());
        System.out.println("Price received: " + operatorAcceptRequest.getPrice());
        if(customerRequestEntity.getStatus().equals(RegisterStatus.ACCEPTED)){
            customerRequestEntity.setAcceptedAt(LocalDateTime.now());
        } else if(customerRequestEntity.getStatus().equals(RegisterStatus.ON_THE_WAY)){
            customerRequestEntity.setOnTheWayAt(LocalDateTime.now());
        } else if(customerRequestEntity.getStatus().equals(RegisterStatus.ARRIVED)){
            customerRequestEntity.setArrivedAt(LocalDateTime.now());
        } else if(customerRequestEntity.getStatus().equals(RegisterStatus.IN_PROGRESS)){
            customerRequestEntity.setInProgressAt(LocalDateTime.now());
        } else if(customerRequestEntity.getStatus().equals(RegisterStatus.COMPLETED)){
            customerRequestEntity.setCompletedAt(LocalDateTime.now());
            if(customerRequestEntity.getPrice() != null){
                customerRequestEntity.setAdminCommission(customerRequestEntity.getPrice() * 0.10);
            }
        }

        customerRequestRepository.save(customerRequestEntity);

        RequestStatusHistory history = new RequestStatusHistory();
        history.setOperator(user);
        history.setCustomer(customerRequestEntity.getCustomer());
        history.setRequest(customerRequestEntity);
        history.setAction(customerRequestEntity.getStatus());
        requestStatusHistoryRepository.save(history);

        operatusStatusResponse.setName(user.getName());
        operatusStatusResponse.setPhnumber(user.getPhoneNumber());
        operatusStatusResponse.setMessage(customerRequestEntity.getMessage());
        operatusStatusResponse.setCustomerLat(customerRequestEntity.getLatitude());
        operatusStatusResponse.setCustomerLng(customerRequestEntity.getLongitude());
        operatusStatusResponse.setGarageLat(customerRequestEntity.getOperator().getLatitude());
        operatusStatusResponse.setGarageLng(customerRequestEntity.getOperator().getLongitude());
        operatusStatusResponse.setPrice(customerRequestEntity.getPrice());
        operatusStatusResponse.setAdminCommission(customerRequestEntity.getAdminCommission());

        return operatusStatusResponse;
    }





    private void validateStatusTransition(RegisterStatus current, RegisterStatus next) {
        if (current == RegisterStatus.PENDING && next != RegisterStatus.ACCEPTED && next != RegisterStatus.CANCELLED) {
            throw new RuntimeException("Invalid transition");
        }
        if (current == RegisterStatus.ACCEPTED && next != RegisterStatus.ON_THE_WAY && next != RegisterStatus.CANCELLED) {
            throw new RuntimeException("Invalid transition");
        }
        if (current == RegisterStatus.ON_THE_WAY && next != RegisterStatus.ARRIVED) {
            throw new RuntimeException("Invalid transition");
        }
        if (current == RegisterStatus.ARRIVED && next != RegisterStatus.IN_PROGRESS) {
            throw new RuntimeException("Invalid transition");
        }
        if (current == RegisterStatus.IN_PROGRESS && next != RegisterStatus.COMPLETED) {
            throw new RuntimeException("Invalid transition");
        }
    }

//Complete vako list dincha new add gareko


    public  List<OperatorCompletedRequest> getAllCOmpletedData(Long id){

List<CustomerRequestEntity> customerRequestEntity= customerRequestRepository.findBystaus(id);
List<OperatorCompletedRequest> operatorCompletedRequests= new ArrayList<>();

for(CustomerRequestEntity cu:customerRequestEntity){

    OperatorCompletedRequest dto = new OperatorCompletedRequest();
    dto.setCustomerName(cu.getCustomer().getName());
    dto.setCustomerPhone(cu.getCustomer().getPhoneNumber());
    dto.setVehicleType(cu.getVechicleType().toString());
    dto.setDescription(cu.getDescription());
    dto.setMessage(cu.getMessage());
    dto.setCreatedAt(cu.getCreatedAt());
    operatorCompletedRequests.add(dto);

}
return  operatorCompletedRequests;
    }



    //earning ko dincha like income

    public EarningsDto getEarnings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Double totalTransaction = customerRequestRepository.findTotalPrice(user.getId());
        if(totalTransaction == null) totalTransaction = 0.0;

        Double adminCommission = totalTransaction * 0.10;
        Double totalIncome = totalTransaction - adminCommission;

        EarningsDto earningsDto = new EarningsDto();
        earningsDto.setTotalTransaction(totalTransaction);
        earningsDto.setAdminCommission(adminCommission);
        earningsDto.setTotalIncome(totalIncome);

        return earningsDto;
    }


}



