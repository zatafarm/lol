package com.mooland.User;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mooland.Config.RedisConfig;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private RedisConfig redisConfig;
    private int authNumber;

    /* 이메일 인증에 필요한 정보 */
    @Value("${spring.mail.username}")
    private String serviceName;

    /* 랜덤 인증번호 생성 */
    public void makeRandomNum() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }

    /* 이메일 전송 */
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom); // service name
            helper.setTo(toMail); // customer email
            helper.setSubject(title); // email title
            helper.setText(content,true); // content, html: true
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // 에러 출력
        }
        // redis에 3분 동안 이메일과 인증 코드 저장
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        valOperations.set(toMail, Integer.toString(authNumber), 180, TimeUnit.SECONDS);
    }

    /* 이메일 작성 */
    public String joinEmail(String email) {
        makeRandomNum();
        String customerMail = email;
        String title = "Mooland 인증번호 입니다!";
        String content =
                "이메일 인증 번호 입니다" +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다.";
        mailSend(serviceName, customerMail, title, content);
        return Integer.toString(authNumber);
    }
    
    public Boolean checkAuthNum(String email, String authNum) {
        ValueOperations<String, String> valOperations = redisConfig.redisTemplate().opsForValue();
        String code = valOperations.get(email);
        if (Objects.equals(code, authNum)) {
            return true;
        } else return false;
    }
}