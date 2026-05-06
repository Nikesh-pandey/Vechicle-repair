package Repair.it.Services.OperatorSide;


import Repair.it.Dtos.AsminSideDtos.AdminResponseDto;
import Repair.it.Dtos.OperatorSideDtos.OperatorRegisterDtos;
import Repair.it.Entity.OperatorSide.OperatorRegisterSide;
import Repair.it.Entity.OperatorSide.RegisterStatus;
import Repair.it.Entity.User;
import Repair.it.Repository.OperatorSide.OperatorRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;

@Service
@AllArgsConstructor
public class OperatorService {
private final OperatorRepository operatorRepository;
    private static  final String  Uploads="static/uploads/";
    public String registerOperator(OperatorRegisterDtos operatorRegisterDtos, MultipartFile shopImageUrl,HttpServletRequest request) throws IOException {

        if(shopImageUrl.isEmpty()){
            return "File is Empty";
        }

String originalName= shopImageUrl.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + originalName;

File uploads= new File(Uploads);
        if(!uploads.exists()){
            uploads.mkdirs();
        }
        String filePath =Uploads + uniqueFilename;
        Files.write(Paths.get(filePath), shopImageUrl.getBytes());
        String protocol = request.getScheme();           // http
        String serverName = request.getServerName();     // localhost
        int serverPort = request.getServerPort();        // 8080
        String imageUrl = protocol + "://" + serverName + ":" + serverPort + "/uploads/" + uniqueFilename;
User Operator= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
OperatorRegisterSide operatorRegisterSide = new OperatorRegisterSide();
operatorRegisterSide.setOperator(Operator);
operatorRegisterSide.setShopName(operatorRegisterDtos.getShopName());
operatorRegisterSide.setAddress(operatorRegisterDtos.getAddress());
operatorRegisterSide.setLatitude(operatorRegisterDtos.getLatitude());
operatorRegisterSide.setLongitude(operatorRegisterDtos.getLongitude());
operatorRegisterSide.setType(operatorRegisterDtos.getType());
operatorRegisterSide.setPhNumber(operatorRegisterDtos.getPhNumber());
operatorRegisterSide.setShopImageUrl(imageUrl);
operatorRegisterSide.setStatus(RegisterStatus.PENDING);
operatorRepository.save(operatorRegisterSide);
return "Your request is submitted, You will be notified within 2 hrs";
    }


    public AdminResponseDto getUpdate(Long id){

      OperatorRegisterSide operator=  operatorRepository.findById(id).orElseThrow(()-> new RuntimeException("Didnot find user with this id"));
AdminResponseDto adminResponseDto = new AdminResponseDto();
adminResponseDto.setStatus(operator.getStatus());
adminResponseDto.setMessage(operator.getMessage());

return adminResponseDto;
    }

}
