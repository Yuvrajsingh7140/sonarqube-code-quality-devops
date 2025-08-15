package com.example.calculator;

import org.springframework.stereotype.Service;

/**
 * Calculator service that provides basic mathematical operations.
 * This class demonstrates various code patterns for SonarQube analysis.
 */
@Service
public class Calculator {

    /**
     * Adds two numbers.
     * 
     * @param a first number
     * @param b second number
     * @return sum of a and b
     */
    public double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts second number from first number.
     * 
     * @param a first number
     * @param b second number to subtract
     * @return difference of a and b
     */
    public double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two numbers.
     * 
     * @param a first number
     * @param b second number
     * @return product of a and b
     */
    public double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides first number by second number.
     * 
     * @param a dividend
     * @param b divisor
     * @return quotient of a divided by b
     * @throws IllegalArgumentException if divisor is zero
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        return a / b;
    }

    /**
     * Calculates the power of a number.
     * 
     * @param base the base number
     * @param exponent the exponent
     * @return base raised to the power of exponent
     */
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    /**
     * Calculates the square root of a number.
     * 
     * @param number the number to find square root of
     * @return square root of the number
     * @throws IllegalArgumentException if number is negative
     */
    public double squareRoot(double number) {
        if (number < 0) {
            throw new IllegalArgumentException("Cannot calculate square root of negative number");
        }
        return Math.sqrt(number);
    }

    /**
     * Calculates the percentage of a number.
     * 
     * @param number the base number
     * @param percentage the percentage to calculate
     * @return percentage of the number
     */
    public double percentage(double number, double percentage) {
        return (number * percentage) / 100;
    }

    /**
     * Determines if a number is even.
     * 
     * @param number the number to check
     * @return true if number is even, false otherwise
     */
    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    /**
     * Determines if a number is prime.
     * 
     * @param number the number to check
     * @return true if number is prime, false otherwise
     */
    public boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        if (number <= 3) {
            return true;
        }
        if (number % 2 == 0 || number % 3 == 0) {
            return false;
        }

        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the factorial of a number.
     * 
     * @param number the number to calculate factorial for
     * @return factorial of the number
     * @throws IllegalArgumentException if number is negative
     */
    public long factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        if (number == 0 || number == 1) {
            return 1;
        }

        long result = 1;
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Calculates the greatest common divisor of two numbers.
     * 
     * @param a first number
     * @param b second number
     * @return greatest common divisor of a and b
     */
    public int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    /**
     * Calculates the least common multiple of two numbers.
     * 
     * @param a first number
     * @param b second number
     * @return least common multiple of a and b
     */
    public int lcm(int a, int b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.abs(a * b) / gcd(a, b);
    }
}