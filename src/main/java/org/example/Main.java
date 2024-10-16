package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Data
class Visitor {
    private String name; // firstName
    private String surname; // lastName
    private String phone; // phoneNumber
    private List<Book> favoriteBooks; // favorites
    private boolean subscribed;
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<Book> getFavoriteBooks() {
        return favoriteBooks;
    }
}


@Data
class Book {
    private String name; // title
    private String author;
    private int publishingYear; // year
    private String isbn;
    private String publisher;
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublishingYear() {
        return publishingYear;
    }
}


public class Main {
    public static void main(String[] args) {
        List<Visitor> visitors = parseJson("books.json");

        // Задание 1: Вывести список посетителей и их количество.
        System.out.println("Visitors and their count:");
        visitors.forEach(visitor ->
                System.out.println(visitor.getName() + " " + visitor.getSurname()));
        System.out.println("Total visitors: " + visitors.size());

        // Задание 2: Вывести список и количество книг, добавленных посетителями в избранное, без повторений.
        System.out.println("\nUnique favorite books and their count:");
        Set<Book> uniqueBooks = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .collect(Collectors.toSet());
        uniqueBooks.forEach(book -> System.out.println(book.getName() + " by " + book.getAuthor()));
        System.out.println("Total unique books: " + uniqueBooks.size());

        // Задание 3: Отсортировать по году издания и вывести список книг.
        System.out.println("\nBooks sorted by year of publication:");
        uniqueBooks.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .forEach(book -> System.out.println(book.getName() + " (" + book.getPublishingYear() + ")"));

        // Задание 4: Проверить, есть ли у кого-то в избранном книга автора “Jane Austen”.
        boolean hasJaneAusten = visitors.stream()
                .flatMap(visitor -> visitor.getFavoriteBooks().stream())
                .anyMatch(book -> "Jane Austen".equals(book.getAuthor()));
        System.out.println("\nIs there any book by Jane Austen in favorites? " + hasJaneAusten);

        // Задание 5: Вывести максимальное число добавленных в избранное книг.
        int maxFavorites = visitors.stream()
                .mapToInt(visitor -> visitor.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("\nMaximum number of favorite books by a visitor: " + maxFavorites);
    }

    // Метод для парсинга JSON файла в список посетителей.
    private static List<Visitor> parseJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Type visitorListType = new TypeToken<List<Visitor>>() {}.getType();
            return new Gson().fromJson(reader, visitorListType);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
