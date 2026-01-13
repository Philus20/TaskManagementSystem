package utils;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Utility class for functional programming operations
 * Provides common functional interfaces and stream operations
 */
public class FunctionalUtils {
    
    /**
     * Higher-order function for retrying operations
     */
    public static <T> T retry(int maxAttempts, Supplier<T> operation, Predicate<Exception> shouldRetry) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < maxAttempts) {
            try {
                return operation.get();
            } catch (Exception e) {
                lastException = e;
                if (!shouldRetry.test(e) || attempts == maxAttempts - 1) {
                    break;
                }
                attempts++;
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxAttempts + " attempts", lastException);
    }
    
    /**
     * Safe function execution with exception handling
     */
    public static <T, R> Optional<R> safeApply(Function<T, R> function, T input) {
        try {
            return Optional.ofNullable(function.apply(input));
        } catch (Exception e) {
            System.err.println("Error applying function: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Safe supplier execution with exception handling
     */
    public static <T> Optional<T> safeGet(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            System.err.println("Error getting value: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Compose multiple predicates with AND logic
     */
    @SafeVarargs
    public static <T> Predicate<T> andAll(Predicate<T>... predicates) {
        return Arrays.stream(predicates).reduce(x -> true, Predicate::and);
    }
    
    /**
     * Compose multiple predicates with OR logic
     */
    @SafeVarargs
    public static <T> Predicate<T> orAny(Predicate<T>... predicates) {
        return Arrays.stream(predicates).reduce(x -> false, Predicate::or);
    }
    
    /**
     * Create a predicate that negates the given predicate
     */
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
    
    /**
     * Create a predicate that always returns true
     */
    public static <T> Predicate<T> alwaysTrue() {
        return x -> true;
    }
    
    /**
     * Create a predicate that always returns false
     */
    public static <T> Predicate<T> alwaysFalse() {
        return x -> false;
    }
    
    /**
     * Create a predicate that tests for null
     */
    public static <T> Predicate<T> isNull() {
        return Objects::isNull;
    }
    
    /**
     * Create a predicate that tests for non-null
     */
    public static <T> Predicate<T> isNotNull() {
        return Objects::nonNull;
    }
    
    /**
     * Create a predicate that tests for equality with a given value
     */
    public static <T> Predicate<T> isEqual(T value) {
        return x -> Objects.equals(x, value);
    }
    
    /**
     * Create a function that always returns a constant value
     */
    public static <T, R> Function<T, R> constant(R value) {
        return x -> value;
    }
    
    /**
     * Create a function that returns the input identity
     */
    public static <T> Function<T, T> identity() {
        return Function.identity();
    }
    
    /**
     * Memoize a function (simple caching)
     */
    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        Map<T, R> cache = new HashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }
    
    /**
     * Create a consumer that does nothing
     */
    public static <T> Consumer<T> doNothing() {
        return x -> {};
    }
    
    /**
     * Create a consumer that prints the input
     */
    public static <T> Consumer<T> print() {
        return System.out::println;
    }
    
    /**
     * Create a consumer that prints with a prefix
     */
    public static <T> Consumer<T> printWithPrefix(String prefix) {
        return x -> System.out.println(prefix + x);
    }
    
    /**
     * Partition a list based on a predicate
     */
    public static <T> Map<Boolean, List<T>> partition(List<T> list, Predicate<T> predicate) {
        return list.stream().collect(Collectors.partitioningBy(predicate));
    }
    
    /**
     * Group elements by a classifier function
     */
    public static <T, K> Map<K, List<T>> groupBy(List<T> list, Function<T, K> classifier) {
        return list.stream().collect(Collectors.groupingBy(classifier));
    }
    
    /**
     * Find first element matching predicate, or return default
     */
    public static <T> T findFirstOrElse(List<T> list, Predicate<T> predicate, T defaultValue) {
        return list.stream().filter(predicate).findFirst().orElse(defaultValue);
    }
    
    /**
     * Find any element matching predicate, or return default
     */
    public static <T> T findAnyOrElse(List<T> list, Predicate<T> predicate, T defaultValue) {
        return list.stream().filter(predicate).findAny().orElse(defaultValue);
    }
    
    /**
     * Check if all elements match predicate
     */
    public static <T> boolean allMatch(List<T> list, Predicate<T> predicate) {
        return list.stream().allMatch(predicate);
    }
    
    /**
     * Check if any element matches predicate
     */
    public static <T> boolean anyMatch(List<T> list, Predicate<T> predicate) {
        return list.stream().anyMatch(predicate);
    }
    
    /**
     * Check if no elements match predicate
     */
    public static <T> boolean noneMatch(List<T> list, Predicate<T> predicate) {
        return list.stream().noneMatch(predicate);
    }
    
    /**
     * Count elements matching predicate
     */
    public static <T> long countMatches(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).count();
    }
    
    /**
     * Get distinct elements
     */
    public static <T> List<T> distinct(List<T> list) {
        return list.stream().distinct().toList();
    }
    
    /**
     * Get first n elements
     */
    public static <T> List<T> take(List<T> list, int n) {
        return list.stream().limit(n).toList();
    }
    
    /**
     * Get elements after skipping first n
     */
    public static <T> List<T> skip(List<T> list, int n) {
        return list.stream().skip(n).toList();
    }
    
    /**
     * Filter list by predicate
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).toList();
    }
    
    /**
     * Map list using function
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).toList();
    }
    
    /**
     * Flat map list using function
     */
    public static <T, R> List<R> flatMap(List<T> list, Function<T, List<R>> mapper) {
        return list.stream().flatMap(mapper.andThen(List::stream)).toList();
    }
    
    /**
     * Reduce list to single value
     */
    public static <T> Optional<T> reduce(List<T> list, BinaryOperator<T> accumulator) {
        return list.stream().reduce(accumulator);
    }
    
    /**
     * Reduce list with identity value
     */
    public static <T> T reduce(List<T> list, T identity, BinaryOperator<T> accumulator) {
        return list.stream().reduce(identity, accumulator);
    }
    
    /**
     * Create a supplier that returns a random element from list
     */
    public static <T> Supplier<T> randomElement(List<T> list) {
        Random random = new Random();
        return () -> list.isEmpty() ? null : list.get(random.nextInt(list.size()));
    }
    
    /**
     * Create a predicate that tests if string contains substring (case-insensitive)
     */
    public static Predicate<String> containsIgnoreCase(String substring) {
        return str -> str != null && str.toLowerCase().contains(substring.toLowerCase());
    }
    
    /**
     * Create a predicate that tests if string starts with prefix (case-insensitive)
     */
    public static Predicate<String> startsWithIgnoreCase(String prefix) {
        return str -> str != null && str.toLowerCase().startsWith(prefix.toLowerCase());
    }
    
    /**
     * Create a predicate that tests if string ends with suffix (case-insensitive)
     */
    public static Predicate<String> endsWithIgnoreCase(String suffix) {
        return str -> str != null && str.toLowerCase().endsWith(suffix.toLowerCase());
    }
    
    /**
     * Create a function to get string length
     */
    public static final Function<String, Integer> stringLength = String::length;
    
    /**
     * Create a predicate to test if string is empty
     */
    public static final Predicate<String> stringIsEmpty = String::isEmpty;
    
    /**
     * Create a predicate to test if string is blank (Java 11+ compatible)
     */
    public static final Predicate<String> stringIsBlank = str -> str == null || str.trim().isEmpty();
    
    /**
     * Async version of filter
     */
    public static <T> CompletableFuture<List<T>> filterAsync(List<T> list, Predicate<T> predicate) {
        return CompletableFuture.supplyAsync(() -> filter(list, predicate));
    }
    
    /**
     * Async version of map
     */
    public static <T, R> CompletableFuture<List<R>> mapAsync(List<T> list, Function<T, R> mapper) {
        return CompletableFuture.supplyAsync(() -> map(list, mapper));
    }
    
    /**
     * Create a curried function for addition
     */
    public static Function<Integer, Function<Integer, Integer>> add = a -> b -> a + b;
    
    /**
     * Create a curried function for multiplication
     */
    public static Function<Integer, Function<Integer, Integer>> multiply = a -> b -> a * b;
    
    /**
     * Function composition helper
     */
    public static <T, U, R> Function<T, R> compose(Function<T, U> before, Function<U, R> after) {
        return after.compose(before);
    }
    
    /**
     * Function andThen helper
     */
    public static <T, U, R> Function<T, R> andThen(Function<T, U> first, Function<U, R> second) {
        return first.andThen(second);
    }
}
