package com.example.DoroServer.global.exception;


import com.example.DoroServer.domain.user.entity.Gender;
import com.example.DoroServer.domain.user.entity.User;
import com.example.DoroServer.global.common.SuccessResponse;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.DoroServer.global.exception.Code.*;

@RestController
public class ExceptionController {
    //Exception 어떤식으로 사용하는지 예시 만약 spring security 설정되있으면 로그인 해서 token 받기 전까지 자동 redirect 라서 token 갖고 하거나 spring security 끄고 해야함
    @GetMapping("/error/{id}")
    public SuccessResponse exception(@PathVariable("id") String id){
        if(id.equals("ex")){
            throw new BaseException(BAD_REQUEST);
        } else if (id.equals("ex2")) {
            throw new BaseException(BAD_REQUEST);
        } else {
            User user= User.builder()
                    .birth(LocalDate.of(1999,8,10))
                    .phone("123123")
                    .name("youn")
                    .gender(Gender.FEMALE).build();
            User user2= User.builder()
                    .birth(LocalDate.of(1999,8,10))
                    .phone("1231313123")
                    .name("oh")
                    .gender(Gender.MALE).build();
            User[] arr=new User[2];
            arr[0]=user;
            arr[1]=user2;
            return SuccessResponse.successResponse(arr);
        }
    }


}
