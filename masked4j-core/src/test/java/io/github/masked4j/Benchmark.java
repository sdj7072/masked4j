package io.github.masked4j;

import java.util.regex.Pattern;

public class Benchmark {
  private static final String EMAIL = "test.user@example.com";
  private static final int ITERATIONS = 1_000_000;
  private static final Pattern EMAIL_PATTERN = Pattern.compile("(?<=.).(?=[^@]*?@)");

  public static void main(String[] args) {
    System.out.println("Benchmarking " + ITERATIONS + " iterations...");

    // Warmup
    for (int i = 0; i < 10000; i++) {
      maskJava(EMAIL);
      maskRegex(EMAIL);
    }

    long startJava = System.nanoTime();
    for (int i = 0; i < ITERATIONS; i++) {
      maskJava(EMAIL);
    }
    long endJava = System.nanoTime();

    long startRegex = System.nanoTime();
    for (int i = 0; i < ITERATIONS; i++) {
      maskRegex(EMAIL);
    }
    long endRegex = System.nanoTime();

    double javaTimeMs = (endJava - startJava) / 1_000_000.0;
    double regexTimeMs = (endRegex - startRegex) / 1_000_000.0;

    System.out.printf("Java Logic: %.2f ms%n", javaTimeMs);
    System.out.printf("Regex:      %.2f ms%n", regexTimeMs);
    System.out.printf("Ratio:      Regex is %.2fx slower%n", regexTimeMs / javaTimeMs);
  }

  private static String maskJava(String input) {
    if (input == null || !input.contains("@")) {
      return "***";
    }
    int atIndex = input.indexOf("@");
    if (atIndex <= 1) {
      return "***" + input.substring(atIndex);
    }
    return input.charAt(0) + "***" + input.substring(atIndex);
  }

  private static String maskRegex(String input) {
    // Simple regex to mask characters before @, keeping first char
    // Note: This is a simplified regex for comparison
    return input.replaceAll("(?<=.).(?=[^@]*?@)", "*");
  }

  // TODO: Add benchmarks for other maskers (PhoneNumber, CreditCard, etc.)
}
