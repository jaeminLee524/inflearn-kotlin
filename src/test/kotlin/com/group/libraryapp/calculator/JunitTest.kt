package com.group.libraryapp.calculator

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JunitTest {

    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            println("모든 테스트 시작 전")
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            println("모든 테스트 종료 후")
        }
    }

    @BeforeEach
    fun setUp() {
        println("테스트 실행 전")
    }

    @AfterEach
    fun tearDown() {
        println("테스트 종료 후")
    }

    @DisplayName("")
    @Test
    fun test1() {
        println("test1")
    }

    @DisplayName("")
    @Test
    fun test2() {
        println("test2")
    }
}