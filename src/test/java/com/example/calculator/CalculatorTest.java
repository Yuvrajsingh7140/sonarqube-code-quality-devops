package com.example.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the Calculator class.
 * These tests ensure high code coverage and demonstrate various testing patterns.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Calculator Tests")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Nested
    @DisplayName("Basic Arithmetic Operations")
    class BasicArithmeticTests {

        @Test
        @DisplayName("Should add two positive numbers correctly")
        void testAddPositiveNumbers() {
            // Given
            double a = 5.0;
            double b = 3.0;
            double expected = 8.0;

            // When
            double result = calculator.add(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should add negative numbers correctly")
        void testAddNegativeNumbers() {
            // Given
            double a = -5.0;
            double b = -3.0;
            double expected = -8.0;

            // When
            double result = calculator.add(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should add zero correctly")
        void testAddWithZero() {
            // Given
            double a = 5.0;
            double b = 0.0;
            double expected = 5.0;

            // When
            double result = calculator.add(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should subtract numbers correctly")
        void testSubtract() {
            // Given
            double a = 10.0;
            double b = 3.0;
            double expected = 7.0;

            // When
            double result = calculator.subtract(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle negative subtraction result")
        void testSubtractNegativeResult() {
            // Given
            double a = 3.0;
            double b = 10.0;
            double expected = -7.0;

            // When
            double result = calculator.subtract(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should multiply numbers correctly")
        void testMultiply() {
            // Given
            double a = 4.0;
            double b = 3.0;
            double expected = 12.0;

            // When
            double result = calculator.multiply(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle multiplication by zero")
        void testMultiplyByZero() {
            // Given
            double a = 5.0;
            double b = 0.0;
            double expected = 0.0;

            // When
            double result = calculator.multiply(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should divide numbers correctly")
        void testDivide() {
            // Given
            double a = 12.0;
            double b = 3.0;
            double expected = 4.0;

            // When
            double result = calculator.divide(a, b);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should throw exception when dividing by zero")
        void testDivideByZero() {
            // Given
            double a = 5.0;
            double b = 0.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.divide(a, b)
            );

            assertThat(exception.getMessage()).isEqualTo("Division by zero is not allowed");
        }
    }

    @Nested
    @DisplayName("Advanced Mathematical Operations")
    class AdvancedMathTests {

        @Test
        @DisplayName("Should calculate power correctly")
        void testPower() {
            // Given
            double base = 2.0;
            double exponent = 3.0;
            double expected = 8.0;

            // When
            double result = calculator.power(base, exponent);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle power of zero")
        void testPowerOfZero() {
            // Given
            double base = 5.0;
            double exponent = 0.0;
            double expected = 1.0;

            // When
            double result = calculator.power(base, exponent);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should calculate square root correctly")
        void testSquareRoot() {
            // Given
            double number = 9.0;
            double expected = 3.0;

            // When
            double result = calculator.squareRoot(number);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle square root of zero")
        void testSquareRootOfZero() {
            // Given
            double number = 0.0;
            double expected = 0.0;

            // When
            double result = calculator.squareRoot(number);

            // Then
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should throw exception for negative square root")
        void testSquareRootOfNegative() {
            // Given
            double number = -4.0;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.squareRoot(number)
            );

            assertThat(exception.getMessage())
                .isEqualTo("Cannot calculate square root of negative number");
        }

        @Test
        @DisplayName("Should calculate percentage correctly")
        void testPercentage() {
            // Given
            double number = 200.0;
            double percentage = 15.0;
            double expected = 30.0;

            // When
            double result = calculator.percentage(number, percentage);

            // Then
            assertThat(result).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Number Analysis Operations")
    class NumberAnalysisTests {

        @Test
        @DisplayName("Should identify even numbers correctly")
        void testIsEven() {
            assertThat(calculator.isEven(2)).isTrue();
            assertThat(calculator.isEven(4)).isTrue();
            assertThat(calculator.isEven(0)).isTrue();
            assertThat(calculator.isEven(-2)).isTrue();
        }

        @Test
        @DisplayName("Should identify odd numbers correctly")
        void testIsOdd() {
            assertThat(calculator.isEven(1)).isFalse();
            assertThat(calculator.isEven(3)).isFalse();
            assertThat(calculator.isEven(-1)).isFalse();
            assertThat(calculator.isEven(7)).isFalse();
        }

        @Test
        @DisplayName("Should identify prime numbers correctly")
        void testIsPrime() {
            // Prime numbers
            assertThat(calculator.isPrime(2)).isTrue();
            assertThat(calculator.isPrime(3)).isTrue();
            assertThat(calculator.isPrime(5)).isTrue();
            assertThat(calculator.isPrime(7)).isTrue();
            assertThat(calculator.isPrime(11)).isTrue();
            assertThat(calculator.isPrime(13)).isTrue();
        }

        @Test
        @DisplayName("Should identify non-prime numbers correctly")
        void testIsNotPrime() {
            // Non-prime numbers
            assertThat(calculator.isPrime(0)).isFalse();
            assertThat(calculator.isPrime(1)).isFalse();
            assertThat(calculator.isPrime(4)).isFalse();
            assertThat(calculator.isPrime(6)).isFalse();
            assertThat(calculator.isPrime(8)).isFalse();
            assertThat(calculator.isPrime(9)).isFalse();
            assertThat(calculator.isPrime(10)).isFalse();
        }

        @Test
        @DisplayName("Should handle larger prime numbers")
        void testLargePrime() {
            assertThat(calculator.isPrime(97)).isTrue();
            assertThat(calculator.isPrime(100)).isFalse();
        }
    }

    @Nested
    @DisplayName("Factorial Operations")
    class FactorialTests {

        @Test
        @DisplayName("Should calculate factorial correctly")
        void testFactorial() {
            assertThat(calculator.factorial(0)).isEqualTo(1);
            assertThat(calculator.factorial(1)).isEqualTo(1);
            assertThat(calculator.factorial(2)).isEqualTo(2);
            assertThat(calculator.factorial(3)).isEqualTo(6);
            assertThat(calculator.factorial(4)).isEqualTo(24);
            assertThat(calculator.factorial(5)).isEqualTo(120);
        }

        @Test
        @DisplayName("Should throw exception for negative factorial")
        void testNegativeFactorial() {
            // Given
            int number = -5;

            // When & Then
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculator.factorial(number)
            );

            assertThat(exception.getMessage())
                .isEqualTo("Factorial is not defined for negative numbers");
        }
    }

    @Nested
    @DisplayName("GCD and LCM Operations")
    class GcdLcmTests {

        @Test
        @DisplayName("Should calculate GCD correctly")
        void testGcd() {
            assertThat(calculator.gcd(12, 8)).isEqualTo(4);
            assertThat(calculator.gcd(15, 25)).isEqualTo(5);
            assertThat(calculator.gcd(7, 13)).isEqualTo(1);
            assertThat(calculator.gcd(0, 5)).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle negative numbers in GCD")
        void testGcdWithNegatives() {
            assertThat(calculator.gcd(-12, 8)).isEqualTo(4);
            assertThat(calculator.gcd(12, -8)).isEqualTo(4);
            assertThat(calculator.gcd(-12, -8)).isEqualTo(4);
        }

        @Test
        @DisplayName("Should calculate LCM correctly")
        void testLcm() {
            assertThat(calculator.lcm(4, 6)).isEqualTo(12);
            assertThat(calculator.lcm(15, 25)).isEqualTo(75);
            assertThat(calculator.lcm(7, 13)).isEqualTo(91);
        }

        @Test
        @DisplayName("Should handle zero in LCM")
        void testLcmWithZero() {
            assertThat(calculator.lcm(0, 5)).isEqualTo(0);
            assertThat(calculator.lcm(5, 0)).isEqualTo(0);
            assertThat(calculator.lcm(0, 0)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very large numbers")
        void testLargeNumbers() {
            double largeA = Double.MAX_VALUE / 2;
            double largeB = 2.0;

            assertThat(calculator.add(largeA, largeB))
                .isCloseTo(largeA + largeB, within(0.001));
        }

        @Test
        @DisplayName("Should handle very small numbers")
        void testSmallNumbers() {
            double smallA = 0.000001;
            double smallB = 0.000002;
            double expected = 0.000003;

            assertThat(calculator.add(smallA, smallB))
                .isCloseTo(expected, within(0.0000001));
        }

        @Test
        @DisplayName("Should handle decimal precision")
        void testDecimalPrecision() {
            double a = 0.1;
            double b = 0.2;
            double result = calculator.add(a, b);

            // Due to floating point precision, we use closeTo assertion
            assertThat(result).isCloseTo(0.3, within(0.0001));
        }
    }
}