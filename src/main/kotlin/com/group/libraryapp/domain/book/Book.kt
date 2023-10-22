package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book(
    val name: String,

    @Enumerated(EnumType.STRING)
    val type: BookType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다");
        }
    }

    /**
     * 테스트 코드만을 위한 정적팩토리 메소드
     * Book 객체에 특정 타입이 추가되어도 테스트 코드에 영향도가 미치지 않는다
     * Obejct mother pattern
     */
    companion object {
        fun fixture(
            name: String = "이상한 나라의 엘리스",
            type: BookType = BookType.COMPUTER,
            id: Long? = null,
        ): Book {
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }
}