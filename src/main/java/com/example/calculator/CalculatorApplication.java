package com.example.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main application class for the Calculator application.
 * This Spring Boot application provides REST endpoints for calculator operations.
 */
@SpringBootApplication
@RestController
public class CalculatorApplication {

    @Autowired
    private Calculator calculator;

    public static void main(String[] args) {
        SpringApplication.run(CalculatorApplication.class, args);
    }

    /**
     * Health check endpoint.
     * 
     * @return health status message
     */
    @GetMapping("/")
    public String home() {
        return "Calculator Application is running! Use /add?a=5&b=3 to test operations.";
    }

    /**
     * Addition endpoint.
     * 
     * @param a first number
     * @param b second number
     * @return result of addition
     */
    @GetMapping("/add")
    public String add(@RequestParam double a, @RequestParam double b) {
        double result = calculator.add(a, b);
        return String.format("%.2f + %.2f = %.2f", a, b, result);
    }

    /**
     * Subtraction endpoint.
     * 
     * @param a first number
     * @param b second number
     * @return result of subtraction
     */
    @GetMapping("/subtract")
    public String subtract(@RequestParam double a, @RequestParam double b) {
        double result = calculator.subtract(a, b);
        return String.format("%.2f - %.2f = %.2f", a, b, result);
    }

    /**
     * Multiplication endpoint.
     * 
     * @param a first number
     * @param b second number
     * @return result of multiplication
     */
    @GetMapping("/multiply")
    public String multiply(@RequestParam double a, @RequestParam double b) {
        double result = calculator.multiply(a, b);
        return String.format("%.2f * %.2f = %.2f", a, b, result);
    }

    /**
     * Division endpoint.
     * 
     * @param a first number
     * @param b second number
     * @return result of division
     */
    @GetMapping("/divide")
    public String divide(@RequestParam double a, @RequestParam double b) {
        try {
            double result = calculator.divide(a, b);
            return String.format("%.2f / %.2f = %.2f", a, b, result);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Power calculation endpoint.
     * 
     * @param base the base number
     * @param exponent the exponent
     * @return result of power calculation
     */
    @GetMapping("/power")
    public String power(@RequestParam double base, @RequestParam double exponent) {
        double result = calculator.power(base, exponent);
        return String.format("%.2f ^ %.2f = %.2f", base, exponent, result);
    }

    /**
     * Square root calculation endpoint.
     * 
     * @param number the number
     * @return square root of the number
     */
    @GetMapping("/sqrt")
    public String squareRoot(@RequestParam double number) {
        try {
            double result = calculator.squareRoot(number);
            return String.format("sqrt(%.2f) = %.2f", number, result);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Prime number check endpoint.
     * 
     * @param number the number to check
     * @return whether the number is prime
     */
    @GetMapping("/isPrime")
    public String isPrime(@RequestParam int number) {
        boolean result = calculator.isPrime(number);
        return String.format("%d is %s", number, result ? "prime" : "not prime");
    }

    /**
     * Factorial calculation endpoint.
     * 
     * @param number the number
     * @return factorial of the number
     */
    @GetMapping("/factorial")
    public String factorial(@RequestParam int number) {
        try {
            long result = calculator.factorial(number);
            return String.format("%d! = %d", number, result);
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}