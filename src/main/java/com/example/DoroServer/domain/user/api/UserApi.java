package com.example.DoroServer.domain.user.api;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "사용자 👤")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserApi {

    private final UserRepository userRepository;

    @GetMapping()
    public SuccessResponse getUsers(){
        return SuccessResponse.successResponse("all users");
    }
    @GetMapping("/{id}")
    public SuccessResponse getUser(@PathVariable("id") String id){
        return SuccessResponse.successResponse(id + "th user");
    }
    @PatchMapping("/{id}")
    public SuccessResponse patchUser(@PathVariable("id") String id){
        return SuccessResponse.successResponse(id+"th user patched");
    }
}
