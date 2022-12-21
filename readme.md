# java 이메일 인증

1. API lib를 추가해준다.

     - [https://mvnrepository.com/](https://mvnrepository.com/) 에서 spring-boot-starter-mail을 받는다.


```implementation 'org.springframework.boot:spring-boot-starter-mail'```



1. config 설정

    ssl과 tls 둘 다 설정할 경우 이메일이 보내지지 않는다.

    ```
    spring.mail.host= smtp.gmail.com // 보내려고 하는 이메일 url
    spring.mail.port= 587            // 포트 번호 입력(ssl : 465, tls: 587)
    spring.mail.username=이메일       // 메일을 보낼 이메일 주소
    spring.mail.password=비밀번호     // 이메일 비밀번호

    spring.mail.properties.mail.smtp.auth=true             //인증 여부
    spring.mail.properties.mail.smtp.starttls.enable=true  //tls 사용 여부
    ```



2.  service 로직 구현

    필요 정보를 세팅해서 메일을 보내준다.

    ```
    package com.example.auth.service;

    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.mail.javamail.JavaMailSenderImpl;
    import org.springframework.mail.javamail.MimeMessageHelper;
    import org.springframework.stereotype.Service;

    import javax.mail.MessagingException;
    import javax.mail.internet.MimeMessage;

    import java.security.SecureRandom;

    @Service
    @RequiredArgsConstructor
    public class AuthService {

        @Value("${spring.mail.username}")
        private String sender;

        private final JavaMailSenderImpl mailSender;

        public String mailAuth(String email) {
            String authCode = makeAuthCode();
            MimeMessage message = mailSender.createMimeMessage();
            String from = sender;
            String to = email;
            String title = "회원 가입 인증 이메일";
            String context = "인증 번호는 " + authCode + "입니다." + "<br/><br/>"
                    + "인증번호를 입력해 확인란에 기입하여 주세요";
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
                helper.setFrom(from);           // 메일을 보낼 이메일 정보
                helper.setTo(to);               // 메일을 받을 이메일 정보
                helper.setSubject(title);       // 메일의 제목
                helper.setText(context, true);  // 메일 내용 , html 여부
                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return authCode;
        }

        // 인증 번호 생성
        private String makeAuthCode() {
            SecureRandom random = new SecureRandom();
            String code = "";
            for(int i=0; i< 6; i++) {
                code += random.nextInt(10);
            }
            return code;
        }
    }

    ```



※ 참고 사항

- 535-5.7.1 Username and Password not accepted 해당 에러의 경우

    구글의 경우는 앱 비밀번호를 만들어서 비밀번호로 사용

    링크 참고 [https://myaccount.google.com/data-and-personalization](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-SMTP-Gmail-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%A0%84%EC%86%A1-%EA%B5%AC%ED%98%84-%EC%9E%84%EC%8B%9C-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EB%B0%9C%EC%86%A1-%EA%B8%B0%EB%8A%A5)