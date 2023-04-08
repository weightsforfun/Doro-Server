package com.example.DoroServer.domain.user.api;

import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "사용자 👤")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserApi {

    private final UserRepository userRepository;

    @GetMapping()
    //매니저만
    //
    public SuccessResponse findAllUsers(){
        return SuccessResponse.successResponse("all users");
    }
    @GetMapping("/{id}")
    public SuccessResponse findUser(@PathVariable("id") String id){
        return SuccessResponse.successResponse(id + "th user");
    }
    @PatchMapping("/{id}")
    public SuccessResponse updateUser(
            @PathVariable("id") String id,
            @RequestBody UpdateUserReq updateUserReq){
        return SuccessResponse.successResponse(id+"th user patched"+updateUserReq.getDegree().getSchool());
    }
}
