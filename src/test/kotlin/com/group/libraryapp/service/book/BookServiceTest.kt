package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BookServiceTest @Autowired constructor(
    private val bookService: BookService,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
) {

    @AfterEach
    fun tearDown() {
        bookRepository.deleteAllInBatch()
        userLoanHistoryRepository.deleteAllInBatch()
        userRepository.deleteAllInBatch()
    }

    @DisplayName("책 정보를 등록할 수 있습니다.")
    @Test
    fun saveBook() {
        // given
        val bookRequest = BookRequest("이상한 나라의 엘리스")

        // when
        bookService.saveBook(bookRequest)

        // then
        assertThat(bookRepository.findAll()[0].name).isEqualTo("이상한 나라의 엘리스")
    }

    @DisplayName("도서 정보를 대출할 수 있습니다.")
    @Test
    fun loanBook() {
        // given
        bookRepository.save(Book("이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User("이재민", null))
        val bookLoanRequest = BookLoanRequest("이재민", "이상한 나라의 엘리스")

        // when
        bookService.loanBook(bookLoanRequest)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].bookName).isEqualTo("이상한 나라의 엘리스")
        assertThat(result[0].isReturn).isFalse()
    }

    @DisplayName("책이 대출중이라면, 대출에 실패합니다.")
    @Test
    fun loanBookException() {
        // given
        bookRepository.save(Book("이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User("이재민", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 엘리스", false))
        val bookLoanRequest = BookLoanRequest("이재민", "이상한 나라의 엘리스")

        // when && then
        assertThrows<IllegalArgumentException> {
            bookService.loanBook(bookLoanRequest)
        }.apply {
            assertThat(message).isEqualTo("진작 대출되어 있는 책입니다")
        }
    }

    @DisplayName("책 반납을 할 수 있습니다.")
    @Test
    fun returnBook() {
        // given
        bookRepository.save(Book("이상한 나라의 엘리스"))
        val savedUser = userRepository.save(User("이재민", null))
        userLoanHistoryRepository.save(UserLoanHistory(savedUser, "이상한 나라의 엘리스", false))
        val bookReturnRequest = BookReturnRequest("이재민", "이상한 나라의 엘리스")

        // when
        bookService.returnBook(bookReturnRequest)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].isReturn).isTrue()
    }
}