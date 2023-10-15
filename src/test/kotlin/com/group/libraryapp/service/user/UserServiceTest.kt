package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
) {

    @AfterEach
    fun tearDown() {
        userRepository.deleteAllInBatch()
    }

    @DisplayName("유저 정보를 저장할 수 있다.")
    @Test
    fun saveUserTest() {
        // given
        val name = "이재민"
        val userCreateRequest = UserCreateRequest(name, null)

        // when
        userService.saveUser(userCreateRequest)

        // then
        val users = userRepository.findAll()
        assertThat(users[0].name).isEqualTo(name)
        assertThat(users[0].age).isNull()
    }

    @DisplayName("전체 유저 정보를 조회할 수 있습니다.")
    @Test
    fun getUsers() {
        // given
        userRepository.saveAll(
            listOf(
                User("이재민", 28),
                User("홍길동", null),
            )
        )

        // when
        val users = userService.getUsers()

        // then
        assertThat(users).hasSize(2).extracting("name", "age")
            .containsExactlyInAnyOrder(
                tuple("이재민", 28),
                tuple("홍길동", null),
            )
    }

    @DisplayName("회원의 이름 정보를 수정할 수 있습니다.")
    @Test
    fun updateUserName() {
        // given
        val savedUser = userRepository.save(User("이재민", 28))
        val userUpdateRequest = UserUpdateRequest(savedUser.id!!, "홍길동")

        // when
        userService.updateUserName(userUpdateRequest)

        // then
        val findUser = userRepository.findAll()[0]
        assertThat(findUser.name).isEqualTo("홍길동")
    }

    @DisplayName("회원정보를 삭제할 수 있습니다.")
    @Test
    fun deleteUser() {
        // given
        userRepository.save(User("이재민", null))

        // when
        userService.deleteUser("이재민")

        // then
        assertThat( userRepository.findAll()).isEmpty()
    }
}