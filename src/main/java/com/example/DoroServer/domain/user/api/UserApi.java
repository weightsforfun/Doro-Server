package com.example.DoroServer.domain.user.api;

import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.domain.user.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "사용자 👤")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @ApiOperation(value = "리스트 👤", notes = "모든 사용자 조회 API")
    @GetMapping("/users")
    public List<User> Users(){
        return userRepository.findAll();
    }
}
