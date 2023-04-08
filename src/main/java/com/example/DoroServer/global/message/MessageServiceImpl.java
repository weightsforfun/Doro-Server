package com.example.DoroServer.global.message;

import com.example.DoroServer.global.auth.dto.AuthRequestDto.SendAuthNumDto;
import com.example.DoroServer.global.auth.dto.AuthRequestDto.VerifyAuthNumDto;
import com.example.DoroServer.global.exception.Code;
import com.example.DoroServer.global.exception.MessageException;
import java.util.HashMap;
import java.util.Random;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.KakaoOption;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

public class MessageServiceImpl implements MessageService{

    private final DefaultMessageService defaultMessageService;
    private final String fromNumber;
    private final String pfId;
    private final String templateId;

    public MessageServiceImpl(@Value("${solapi.api-key}") String apiKey,
                            @Value("${solapi.api-secret}") String apiSecret,
                            @Value("${solapi.from-number}") String fromNumber,
                            @Value("${solapi.domain}") String domain,
                            @Value("${solapi.pfid}") String pfid,
                            @Value("${solapi.templateId}") String templateId) {
        this.defaultMessageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, domain);
        this.fromNumber = fromNumber;
        this.pfId = pfid;
        this.templateId = templateId;
    }

    @Transactional
    @Override
    public void sendAuthNum(SendAuthNumDto sendAuthNumDto) {
        try{
            String authNum = numberGen(6);
            KakaoOption kakaoOption = new KakaoOption();
            kakaoOption.setPfId(pfId);
            kakaoOption.setTemplateId(templateId);
            // 알림톡 템플릿 내 #{변수} 관리
            HashMap<String, String> variables = new HashMap<>();
            variables.put("#{인증번호}", authNum);
            kakaoOption.setVariables(variables);

            Message message = new Message();
            message.setFrom(fromNumber);
            message.setTo(sendAuthNumDto.getPhone());
            message.setKakaoOptions(kakaoOption);

            defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
        }catch (Exception e){
            throw new MessageException(Code.MESSAGE_SEND_FAILED);
        }


    }

    @Override
    public void verifyAuthNum(VerifyAuthNumDto verifyAuthNumDto) {

    }

    /**
     * @param len : 생성할 난수의 길이
     */
    private String numberGen(int len) {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();

        for(int i = 0; i < len; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr.append(ran);
        }
        return numStr.toString();
    }
}
