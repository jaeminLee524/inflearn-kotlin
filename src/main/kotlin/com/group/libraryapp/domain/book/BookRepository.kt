package com.group.libraryapp.domain.book

import com.group.libraryapp.domain.book.Book
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.loanhistory.UserLoanStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface BookRepository: JpaRepository<Book, Long> {

    fun findByName(bookName: String): Book?
}