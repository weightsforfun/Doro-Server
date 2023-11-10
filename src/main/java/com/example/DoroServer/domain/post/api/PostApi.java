package com.example.DoroServer.domain.post.api;


import com.example.DoroServer.domain.post.dto.CreatePostReq;
import com.example.DoroServer.domain.post.dto.FindAllPostRes;
import com.example.DoroServer.domain.post.dto.FindPostRes;
import com.example.DoroServer.domain.post.dto.UpdatePostReq;
import com.example.DoroServer.domain.post.service.PostService;
import com.example.DoroServer.global.common.SuccessResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "문의글📋")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostApi {

    private final PostService postService;

    //문의글 생성
    @PostMapping()
    @ApiOperation(value = "문의글 생성",notes = "문의글 생성")
    public SuccessResponse createPost(
            @RequestBody @Valid CreatePostReq createPostReq
    ) {
        Long postId = postService.createPost(createPostReq);
        return SuccessResponse.successResponse(postId);
    }

    //문의글 수정
    @PatchMapping("/{id}")
    @ApiOperation(value = "문의글 수정",notes = "바꾸고 싶은 필드만 넣어서 update해도 되고 값 다 넣어서 해도 됩니다. 게시물의 주인인지 확인하기 위해 currentpassowrd 에 유저가 입력한 비밀번호를 넣어줘야 합니다.")
    public SuccessResponse updatePost(
            @PathVariable("id") Long postId,
            @RequestBody @Valid UpdatePostReq updatePostReq
    ) {
        Long updatedPostId = postService.updatePost(postId, updatePostReq);

        return SuccessResponse.successResponse(updatedPostId);
    }

    //답변 작성
    @PatchMapping("/{id}/answer")
    @ApiOperation(value = "문의글 답변 작성",notes = "body 에 json 으로 key:answer , value:xxx 이런식으로 넣어주면됩니다")
    public SuccessResponse updateAnswer(
            @PathVariable("id") Long postId,
            @RequestBody Map<String,String> answerMap) {
        Long updatedPostId = postService.updateAnswer(postId, answerMap.get("answer"));

        return SuccessResponse.successResponse(updatedPostId);
    }

    //문의글 전부 조회(get all paging 사용)
    @GetMapping()
    @ApiOperation(value = "문의글 전체 조회",notes = "문의글 전체 조회 page 기본 사이즈 10,기본 page 0입니다. page,size 파라미터로 조절 가능")
    public SuccessResponse findAllPosts(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size
            ) {

        PageRequest pageRequest = PageRequest.of(page,size);

        List<FindAllPostRes> allPost = postService.findAllPost(pageRequest);

        return SuccessResponse.successResponse(allPost);
    }

    //문의글 조회
    @GetMapping("/{id}")
    @ApiOperation(value = "문의글 조회",notes = "문의글 조회")
    public SuccessResponse findPost(
            @PathVariable("id") Long postId
    ) {

        FindPostRes findPostRes = postService.findPost(postId);

        return SuccessResponse.successResponse(findPostRes);
    }

    //문의글 삭제
    @DeleteMapping("/{id}")
    @ApiOperation(value = "문의글 삭제",notes = "문의글 삭제")
    public SuccessResponse deletePost(
            @PathVariable("id") Long postId
    ) {
        Long deletedPostId = postService.deletePost(postId);

        return SuccessResponse.successResponse(deletedPostId);
    }

    //문의글 비밀번호 조회 (매니저는 어떤 비번 치든 접근 허용)
    @GetMapping("/{id}/password")
    @ApiOperation(value = "비밀번호 체크",notes = "해당 문의글에 해당하는 비밀번호를 체크합니다. 일치하면 true, 틀리면 false를 반환합니다")
    public SuccessResponse checkPassword(
            @PathVariable("id") Long postId,
            @RequestBody Map<String,String> passwordMap
    ) {

        boolean result = postService.checkPassword(postId, passwordMap.get("password"));

        return SuccessResponse.successResponse(result);
    }


}
