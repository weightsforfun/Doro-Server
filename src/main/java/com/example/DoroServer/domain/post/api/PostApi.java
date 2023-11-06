package com.example.DoroServer.domain.post.api;


import com.example.DoroServer.domain.post.service.PostService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "문의글📋")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostApi {

    private final PostService postService;

    //문의글 생성
    //문의글 수정
    //문의글에 답변 작성(update answer)
    //문의글 전부 조회(get all paging 사용)
    //문의글 조회
    //문의글 삭제

    //문의글 비밀번호 조회 (매니저는 어떤 비번 치든 접근 허용)



}
