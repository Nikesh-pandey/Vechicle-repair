package Repair.it.Services.OperatorSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.*;
import Repair.it.Entity.OperatorSide.OperatorGarageRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.Request.CustomerRequestEntity;
import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import Repair.it.Repository.CustomerSide.CustomerRepository;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import Repair.it.Repository.Request.CustomerRequestRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class OperatorService {
private final OperatorRepository operatorRepository;
private final CustomerRequestRepository customerRequestRepository;



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



    public OperatusStatusResponse responseGiven(OperatorAcceptRequest operatorAcceptRequest,Long id){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
OperatusStatusResponse operatusStatusResponse= new OperatusStatusResponse();
        System.out.println(id.toString());
        CustomerRequestEntity customerRequestEntity= customerRequestRepository.findById(id).orElseThrow(()-> new RuntimeException("Didnot find with this id"));

        System.out.println(id);
            customerRequestEntity.setStatus(operatorAcceptRequest.getStatus());
            customerRequestEntity.setMessage(operatorAcceptRequest.getMessage());
            customerRequestRepository.save(customerRequestEntity);
            //customer


            operatusStatusResponse.setName(user.getName());
            operatusStatusResponse.setPhnumber(user.getPhoneNumber());
            operatusStatusResponse.setStatus(customerRequestEntity.getStatus());
            operatusStatusResponse.setMessage(customerRequestEntity.getMessage());
operatusStatusResponse.setCustomerLat(customerRequestEntity.getLatitude());
operatusStatusResponse.setCustomerLng(customerRequestEntity.getLongitude());
operatusStatusResponse.setGarageLat(customerRequestEntity.getOperator().getLatitude());
operatusStatusResponse.setGarageLng(customerRequestEntity.getOperator().getLongitude());

            return operatusStatusResponse;

    }





}



