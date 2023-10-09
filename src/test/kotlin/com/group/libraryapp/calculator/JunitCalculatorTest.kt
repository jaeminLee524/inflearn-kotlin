package com.group.libraryapp.calculator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class JunitCalculatorTest {

    @DisplayName("addTest")
    @Test
    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        assertThat(calculator.number).isEqualTo(8)
    }

    @DisplayName("minusTest")
    @Test
    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        assertThat(calculator.number).isEqualTo(2)
    }

    @DisplayName("multiplyTest")
    @Test
    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        assertThat(calculator.number).isEqualTo(15)
    }

    @DisplayName("divideTest")
    @Test
    fun divideTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(2)

        // then
        assertThat(calculator.number).isEqualTo(2)
    }

    @DisplayName("divideExceptionTest")
    @Test
    fun divideExceptionTest() {
        // given
        val calculator = Calculator(5)

        // when
        val message = assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.message

        // then
        assertThat(message).isEqualTo("0으로 나눌 수 없습니다.")
    }

    @DisplayName("divideExceptionTestWithScopeFunction")
    @Test
    fun divideExceptionTestWithScopeFunction() {
        // given
        val calculator = Calculator(5)

        // when
        val message = assertThrows<IllegalArgumentException> {
            calculator.divide(0)
        }.apply {
            assertThat(message).isEqualTo("0으로 나눌 수 없습니다.")
        }
    }
}