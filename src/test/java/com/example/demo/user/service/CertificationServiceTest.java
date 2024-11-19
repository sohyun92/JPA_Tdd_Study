package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.service.port.MailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다(){
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);

        //when
        certificationService.send("0830thgus@naver.com",1,"aaaaaaaaaaa");

        //then
        assertThat(fakeMailSender.email).isEqualTo("0830thgus@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");

    }

}
