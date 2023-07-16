package com.example.DoroServer.domain.lectureContent.service;


import com.example.DoroServer.domain.lectureContent.dto.LectureContentDto;
import com.example.DoroServer.domain.lectureContent.dto.LectureContentMapper;
import com.example.DoroServer.domain.lectureContent.dto.UpdateLectureContentReq;
import com.example.DoroServer.domain.lectureContent.entity.LectureContent;
import com.example.DoroServer.domain.lectureContent.repository.LectureContentRepository;
import com.example.DoroServer.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;


import javax.annotation.meta.When;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LectureContentServiceTest {


    @InjectMocks
    private LectureContentService lectureContentService;

    @Mock
    private LectureContentRepository lectureContentRepository;

    @Mock
    private ModelMapper modelMapper;

    @Spy
    private LectureContentMapper lectureContentMapper = Mappers.getMapper(LectureContentMapper.class);

    private LectureContentDto setUpLectureContentDto(){
        return LectureContentDto.builder()
                .kit("kit")
                .detail("detail")
                .remark("remark")
                .requirement("requirement")
                .content("content")
                .build();
    }

    private LectureContent setUpLectureContent(){
        return LectureContent.builder()
                .id(1L)
                .kit("kit")
                .detail("detail")
                .remark("remark")
                .requirement("requirement")
                .content("content")
                .build();
    }
    private UpdateLectureContentReq setUpUpdateLectureContentReq(){
        return UpdateLectureContentReq.builder()
                .kit("updated")
                .content("updated")
                .build();
    }
    private List<LectureContent> setUpLectureContentList(int listSize){
        List<LectureContent> allContents= new ArrayList<LectureContent>();
        for(int i=0;i<listSize;i++){
            allContents.add(setUpLectureContent());
        }
        return allContents;
    }


    @DisplayName("강의자료 생성 Mapper 테스트")
    @Test
    void toLectureContentMapperTest() throws IllegalAccessException {
        //given
        LectureContentDto lectureContentDto = setUpLectureContentDto();
        //when
        LectureContent lectureContent = lectureContentMapper.toLectureContent(lectureContentDto);

        //then
        for(Field field :lectureContent.getClass().getDeclaredFields() ){
            field.setAccessible(true);
            Object value = field.get(lectureContent);
            if(field.getName().equals("id")){
                continue;
            }
            assertThat(value).isNotNull();
        }

    }

    @DisplayName("강의 자료 생성 테스트")
    @Test
    void createLectureContentTest() {

        // given
        LectureContentDto lectureContentDto = setUpLectureContentDto();
        LectureContent lectureContent = setUpLectureContent();

        given(lectureContentMapper.toLectureContent(any(LectureContentDto.class))).willReturn(lectureContent);
        given(lectureContentRepository.save(any(LectureContent.class))).willReturn(lectureContent);
        // when
        Long lectureID = lectureContentService.createLecture(lectureContentDto);
        // then
        assertThat(lectureID).isEqualTo(lectureContent.getId());
        verify(lectureContentMapper,times(1)).toLectureContent(lectureContentDto);
        verify(lectureContentRepository,times(1)).save(lectureContent);

    }

    @DisplayName("강의 자료 조회 Mapper 테스트")
    @Test
    void toLectureContentDtoMapperTest() throws IllegalAccessException {
        // given
        LectureContent lectureContent = setUpLectureContent();

        // when
        LectureContentDto lectureContentDto = lectureContentMapper.toLectureContentDto(lectureContent);
        // then
        for(Field field :lectureContentDto.getClass().getDeclaredFields() ){
            field.setAccessible(true);
            Object value = field.get(lectureContentDto);
            assertThat(value).isNotNull();
        }

    }

    @DisplayName("모든 강의 자료 조회 테스트")
    @Test
    void findAllLectureContents() {
        // given
        int contentCount=5;
        List<LectureContent> lectureContents = setUpLectureContentList(contentCount);
        given(lectureContentRepository.findAll()).willReturn(lectureContents);
        given(lectureContentMapper.toLectureContentDto(any(LectureContent.class))).willReturn(setUpLectureContentDto());

        // when
        List<LectureContentDto> allLectureContents = lectureContentService.findAllLectureContents();

        // then
        verify(lectureContentRepository,times(1)).findAll();
        verify(lectureContentMapper,times(contentCount)).toLectureContentDto(any(LectureContent.class));
        assertThat(allLectureContents.size()).isEqualTo(contentCount);

    }

    @DisplayName("강의 자료 업데이트 예외 테스트")
    @Test
    void updateLectureContentExceptionTest() {
        // given
        LectureContent lectureContent = setUpLectureContent();
        UpdateLectureContentReq updateLectureContentReq = setUpUpdateLectureContentReq();
        given(lectureContentRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // when

        // then
        assertThrows(BaseException.class,()->{
            lectureContentService.updateLectureContent(lectureContent.getId(),updateLectureContentReq);
        });
    }

    @DisplayName("강의 자료 업데이트 테스트")
    @Test
    void updateLectureContentTest() {
        // given
        LectureContent lectureContent = setUpLectureContent();
        UpdateLectureContentReq updateLectureContentReq = setUpUpdateLectureContentReq();
        given(lectureContentRepository.findById(any(Long.class))).willReturn(Optional.of(lectureContent));
        doNothing().when(modelMapper).map(any(UpdateLectureContentReq.class),any(LectureContent.class));

        // when
        lectureContentService.updateLectureContent(lectureContent.getId(),updateLectureContentReq);
        // then
        verify(lectureContentRepository,times(1)).findById(any(Long.class));
        verify(modelMapper,times(1)).map(any(UpdateLectureContentReq.class),any(LectureContent.class));;

    }

}