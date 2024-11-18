package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;


@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

})
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_Active_상태인_유저를_찾아올_수_있다(){
        String email = "0830thgus@naver.com";
        UserEntity result = userService.getByEmail(email);
        assertThat(result.getNickname()).isEqualTo("so");
    }

    @Test
    void getByEmail은_Pending_상태인_유저를_찾아올_수_있다(){
        String email = "08302thgus@naver.com";
        assertThatThrownBy(()->{
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_Active_상태인_유저를_찾아올_수_있다(){

        UserEntity result = userService.getById(1);
        assertThat(result.getNickname()).isEqualTo("so");

    }

    @Test
    void getById은_Pending_상태인_유저를_찾아올_수_있다(){
        String email = "08302thgus@naver.com";
        assertThatThrownBy(()->{
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDTO_를_이용하여_유저_생성_할수있다(){

        UserCreate userCreateDto= UserCreate.builder()
                .email("0830thgus@naver.com")
                .address("addr")
                .nickname("so")
                .build();

        //email 인증때문에... 추가한부분..
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        UserEntity result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }



    @Test
    void userUpdateDTO_를_이용하여_유저_수정_할수있다(){

        UserUpdate userUpdateDto= UserUpdate.builder()
                .address("seoul")
                .nickname("so12")
                .build();

        //email 인증때문에... 추가한부분..
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        UserEntity result = userService.update(1,userUpdateDto);

        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("seoul");
        assertThat(userEntity.getNickname()).isEqualTo("so12");

    }

    @Test
    void 유저_login_시키면_마지막_로그인_시간이_변경된다(){
        //given
        //when
        userService.login(1);
        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);

    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        //given
        //when
        userService.verifyEmail(2,"aaaaaaaaaaab");
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        //given
        //when
        assertThatThrownBy(()-> {
            userService.verifyEmail(2,"aaaaaaaaaaab-aa");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}
