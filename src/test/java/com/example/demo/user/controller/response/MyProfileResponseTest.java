package com.example.demo.user.controller.response;

import com.example.demo.user.domain.MyProfileResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyProfileResponseTest {

    @Test
    public void User으로_응답을_생성할_수_있다(){
        User user = User.builder().
                 id(1L)
                .email("0830thgus@naver.com")
                .nickname("so")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaaaaa")
                .build();

        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);
        assertThat(myProfileResponse.getId()).isEqualTo(1);
        assertThat(myProfileResponse.getEmail()).isEqualTo("0830thgus@naver.com");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }
}
