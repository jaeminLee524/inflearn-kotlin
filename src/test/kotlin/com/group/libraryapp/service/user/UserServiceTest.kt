package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.dto.user.request.UserCreateRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
) {

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
}