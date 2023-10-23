package com.group.libraryapp.service.user

import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.aspectj.lang.annotation.After
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val userLoanRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
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

    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다.")
    @Test
    fun getUserLoanHistoriesTest1() {
        // given
        userRepository.save(User("A", null))

        // when
        val results = userService.getUserLoanHistory()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).isEmpty()
    }

    @DisplayName("대출 기록이 많은 유저의 응답이 정상 동작한다.")
    @Test
    fun getUserLoanHistoriesTest2() {
        // given
        val savedUser = userRepository.save(User("A", null))
        userLoanRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "책1", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "책2", UserLoanStatus.LOANED),
                UserLoanHistory.fixture(savedUser, "책3", UserLoanStatus.RETURNED),
            )
        )

        // when
        val results = userService.getUserLoanHistory()

        // then
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("A")
        assertThat(results[0].books).hasSize(3)
            .extracting("name", "isReturn")
            .containsExactlyInAnyOrder(
                tuple("책1", false),
                tuple("책2", false),
                tuple("책3", true)
            )
    }
}