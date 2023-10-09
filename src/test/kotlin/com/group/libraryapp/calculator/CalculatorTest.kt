package com.group.libraryapp.calculator

/**
 * 수동으로 만든 테스트 코드의 경우 여러 단점이 존재한다.
 * 1. main 메소드에 수동으로 테스트 메소드를 추가해야한다.
 * 2. 테스트 메소드를 개별적으로 실행하기 어렵다.
 * 3. 기대값과 실제 결과값에 대한 정보를 제공해주지 않는다.
 * 4. 예외를 직전 던지거나, try catch을 사용해 직접 구현해야한다.
 * 5. 공통적으로 처리해야 할 기능이 있다면 메소드 마다 중복이 생긴다 -> given 절 참고
 */
fun main() {
    val calculatorTest = CalculatorTest()

    calculatorTest.addTest()
    calculatorTest.minusTest()
    calculatorTest.multiplyTest()
    calculatorTest.divideTest()
    calculatorTest.divideExceptionTest()
}

class CalculatorTest {

    fun addTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.add(3)

        // then
        if (calculator.number != 8) {
            throw IllegalArgumentException()
        }
    }

    fun minusTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.minus(3)

        // then
        if (calculator.number != 2) {
            throw IllegalArgumentException()
        }
    }

    fun multiplyTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.multiply(3)

        // then
        if (calculator.number != 15) {
            throw IllegalArgumentException()
        }
    }

    fun divideTest() {
        // given
        val calculator = Calculator(5)

        // when
        calculator.divide(2)

        // then
        if (calculator.number != 2) {
            throw IllegalArgumentException()
        }
    }

    fun divideExceptionTest() {
        // given
        val calculator = Calculator(5)

        // when
        try {
            calculator.divide(0)
        } catch (e: IllegalArgumentException) {
            if (e.message != "0으로 나눌 수 없습니다.") {
                throw IllegalArgumentException("메시지가 다릅니다.")
            }
            return
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }

        throw IllegalArgumentException("기대하는 예외가 발생하지 않았습니다.")
    }
}