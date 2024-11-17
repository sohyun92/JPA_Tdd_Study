package com.example.demo.respository;

import com.example.demo.model.UserStatus;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailAndStatus로_유저데이터를_찾아올수있다(){
        //given

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("0830thgus@naver.com", UserStatus.ACTIVE);
        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_데이터가없으면_Optional_empty를_내려준다(){
        //given

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("0830thgus@naver.com", UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }


}
