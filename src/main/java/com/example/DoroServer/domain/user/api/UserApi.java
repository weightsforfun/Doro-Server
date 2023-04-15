package com.example.DoroServer.domain.user.api;

import com.example.DoroServer.domain.user.dto.UpdateUserReq;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.entity.UserRole;
import com.example.DoroServer.domain.user.repository.UserRepository;
import com.example.DoroServer.domain.user.service.UserService;
import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import io.swagger.annotations.Api;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "사용자 👤")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserApi {

    private final UserRepository userRepository;
    private final UserService userService;
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
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserReq updateUserReq){
        UserRole role = user.getRole();
        if(role.equals(UserRole.ROLE_USER) && !Long.valueOf(id).equals(user.getId())){
            throw new BaseException(Code.FORBIDDEN);
        }
        userService.updateUser(id, updateUserReq);
        return SuccessResponse.successResponse(id+"th user patched");
    }
    @PatchMapping("/profile")
    public SuccessResponse<String> updateUserProfile(
            @RequestParam("images") MultipartFile multipartFile,
            @AuthenticationPrincipal User user) throws IOException {
        userService.updateUserProfile(user, multipartFile);
        return SuccessResponse.successResponse("프로필 이미지 변경 성공");
    }
}
