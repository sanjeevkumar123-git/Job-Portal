package JobApplication.FeignClients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import JobApplication.DTO.UserDTO;

@FeignClient(name = "User" , url = "${user-service.url}" )
public interface UserClient {

    @GetMapping("/api/users/{userId}/profile")
    UserDTO getUserDetails(@PathVariable Long userId);

   
}

