package com.group.libraryapp.service.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.book.BookRepository
import com.group.libraryapp.domain.book.BookType
import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import com.group.libraryapp.dto.book.request.BookLoanRequest
import com.group.libraryapp.dto.book.request.BookRequest
import com.group.libraryapp.dto.book.request.BookReturnRequest
import com.group.libraryapp.dto.book.response.BookStatResponse
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
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
        val bookRequest = BookRequest("이상한 나라의 엘리스", BookType.COMPUTER)

        // when
        bookService.saveBook(bookRequest)

        // then
        val books = bookRepository.findAll()
        assertThat(books).hasSize(1)
        assertThat(books[0].name).isEqualTo("이상한 나라의 엘리스")
        assertThat(books[0].type).isEqualTo(BookType.COMPUTER)
    }

    @DisplayName("도서 정보를 대출할 수 있습니다.")
    @Test
    fun loanBook() {
        // given
        bookRepository.save(Book.fixture())
        val savedUser = userRepository.save(User("이재민", null))
        val bookLoanRequest = BookLoanRequest("이재민", "이상한 나라의 엘리스")

        // when
        bookService.loanBook(bookLoanRequest)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result[0].user.id).isEqualTo(savedUser.id)
        assertThat(result[0].bookName).isEqualTo("이상한 나라의 엘리스")
        assertThat(result[0].status).isEqualTo(UserLoanStatus.LOANED)
    }

    @DisplayName("책이 대출중이라면, 대출에 실패합니다.")
    @Test
    fun loanBookException() {
        // given
        bookRepository.save(Book.fixture())
        val savedUser = userRepository.save(User("이재민", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "이상한 나라의 엘리스"))
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
        bookRepository.save(Book.fixture())
        val savedUser = userRepository.save(User("이재민", null))
        userLoanHistoryRepository.save(UserLoanHistory.fixture(savedUser, "이상한 나라의 엘리스"))
        val bookReturnRequest = BookReturnRequest("이재민", "이상한 나라의 엘리스")

        // when
        bookService.returnBook(bookReturnRequest)

        // then
        val result = userLoanHistoryRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].status).isEqualTo(UserLoanStatus.RETURNED)
    }

    @DisplayName("책 대여 권수를 정상 확인한다.")
    @Test
    fun countLoanBook() {
        // given
        val savedUser = userRepository.save(User("이재민", null))
        userLoanHistoryRepository.saveAll(
            listOf(
                UserLoanHistory.fixture(savedUser, "A"),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
                UserLoanHistory.fixture(savedUser, "B", UserLoanStatus.RETURNED),
            )
        )

        // when
        val results = bookService.countLoanBook()

        // then
        assertThat(results).isEqualTo(1)
    }

    @DisplayName("분야별 책 권수를 정상 확인한다.")
    @Test
    fun getBookStatistics() {
        // given
        bookRepository.saveAll(
            listOf(
                Book("A", BookType.COMPUTER),
                Book("B", BookType.COMPUTER),
                Book("C", BookType.SCIENCE),
            )
        )

        // when
        val results = bookService.getBookStatistics()

        // then
        assertThat(results).hasSize(2)
        assertCount(results, BookType.COMPUTER, 2L)
        assertCount(results, BookType.SCIENCE, 1L)
    }

    private fun assertCount(results: List<BookStatResponse>, type: BookType, count: Long) {
        assertThat( results.first { dto -> dto.type == type }.count).isEqualTo(count)
    }
}