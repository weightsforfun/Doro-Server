package com.example.DoroServer.domain.post.service;


import com.example.DoroServer.domain.post.dto.*;
import com.example.DoroServer.domain.post.entity.Post;
import com.example.DoroServer.domain.post.repository.PostRepository;
import com.example.DoroServer.global.exception.BaseException;
import com.example.DoroServer.global.exception.Code;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final PostMapper postmapper;


    public Long createPost(CreatePostReq createPostReq) {
        //mapstruct로 entity 생성

        Post post = postmapper.toPost(createPostReq);

        Post savedPost = postRepository.save(post);

        return savedPost.getId();
    }


    //답변, 게시글 수정에 모두 사용됩니다.(model mapper 에서 null 값은 업데이트를 안치게 적용)
    public Long updatePost(Long postId, UpdatePostReq updatePostReq) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        //작성자의 비밀번호가 일치하지 않습니다.
        if(!post.getPassword().equals(updatePostReq.getCurrentPassword())){
            throw new BaseException(Code.BAD_REQUEST);
        }
        //model mapper 적용
        modelMapper.map(updatePostReq,post);

        return post.getId();
    }

    public Long updateAnswer(Long postId,String answer){

        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        post.enrollAnswer(answer);

        return post.getId();

    }

    public List<FindAllPostRes> findAllPost(Pageable pageable) {

        Page<Post> posts = postRepository.findAll(pageable);
        //post -> postRes 로 전환
        List<FindAllPostRes> findAllPostResList = posts.stream().map(post -> postmapper.toFindAllPostRes(post)).collect(Collectors.toList());

        // return postRes
        return findAllPostResList;
    }

    public FindPostRes findPost(Long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.BAD_REQUEST));

        //post postRes로 전환
        FindPostRes findPostRes = postmapper.toFindPostRes(post);
        //return post
        return findPostRes;
    }

    public Long deletePost(Long id){
        postRepository.deleteById(id);
        return id;
    }

    public boolean checkPassword(Long id, String password){

        Post post = postRepository.findById(id).orElseThrow(() -> new BaseException(Code.USER_NOT_FOUND));

        if(!post.getPassword().equals(password)){
            return false;
        }

        return true;
    }

}
