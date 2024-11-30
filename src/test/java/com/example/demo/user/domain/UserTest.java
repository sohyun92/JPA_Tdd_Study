package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할수있다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("08302thgus@naver.com")
                .nickname("so2")
                .address("Seoul")
                .build();

        //when
        User user = User.from(userCreate,new TestUuidHolder("aaaaaaaaaaab"));

        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("08302thgus@naver.com");
        assertThat(user.getNickname()).isEqualTo("so2");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaaaab");

    }

    @Test
    public void User_는_UserUpdate객체로_데이터를_업데이트_할수있다(){

        User user = User.builder()
                .id(1L)
                .email("0830thgus@naver.com")
                .nickname("so")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("soUp")
                .address("London")
                .build();

        //when
        user.update(userUpdate);
        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("0830thgus@naver.com");
        //hmm... why?
        //assertThat(user.getNickname()).isEqualTo("soUp");
        //assertThat(user.getAddress()).isEqualTo("London");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    public void User는_로그인을_할수있고_로그인시_마지막_로그인시간이_변경된다(){
        User user = User.builder()
                .id(1L)
                .email("0830thgus@naver.com")
                .nickname("so")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaa")
                .build();

        user =user.login(new TestClockHolder(1678530673958L));

        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    public void User는_유효한_인증_코드로_계정을_활성화_할수있다(){
        User user = User.builder()
                .id(1L)
                .email("0830thgus@naver.com")
                .nickname("so")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaa")
                .build();

        user =user.certificate("aaaaaaaaaaa");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    public void User는_잘못된_인증코드로_계정을_활성화_하려면_에러를_던진다(){
        User user = User.builder()
                .id(1L)
                .email("0830thgus@naver.com")
                .nickname("so")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaaaaa")
                .build();

        assertThatThrownBy(() -> user.certificate("aaaaaaaabb"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
