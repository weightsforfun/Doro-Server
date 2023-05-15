package com.example.DoroServer.global.message;

import com.example.DoroServer.global.common.SuccessResponse;
import com.example.DoroServer.global.message.dto.SendAuthNumReq;
import com.example.DoroServer.global.message.dto.VerifyAuthNumReq;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "메시지 📬")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @Operation(summary = "002_01", description = "인증번호 전송")
    @PostMapping("/message/send")
    public SuccessResponse<String> sendAuthNum(@RequestBody @Valid SendAuthNumReq sendAuthNumReq){
        messageService.sendAuthNum(sendAuthNumReq);
        return SuccessResponse.successResponse("인증번호가 전송되었습니다.");
    }


    @Operation(summary = "002_01", description = "인증번호 확인")
    @PostMapping("/message/verify")
    public SuccessResponse<String> verifyAuthNum(@RequestBody @Valid VerifyAuthNumReq verifyAuthNumReq){
        messageService.verifyAuthNum(verifyAuthNumReq);
        return SuccessResponse.successResponse("인증 성공");
    }
}
