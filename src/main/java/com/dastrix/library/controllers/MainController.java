package com.dastrix.library.controllers;

import com.dastrix.library.models.Book;
import com.dastrix.library.models.Role;
import com.dastrix.library.models.User;
import com.dastrix.library.repo.BookRepository;
import com.dastrix.library.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    
    @GetMapping("/")
    public String home(Model model) {
        Iterable<Book> books = bookRepository.findAll();

        model.addAttribute("book_name", "Бібліотека");
        model.addAttribute("books", books);
        return "home";
    }

    @PostMapping("/home/{id}")
    public String take(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(ClassNotFoundException::new);
        book.setReader(user);
        book.setFree(false);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/books")
    public String books(@AuthenticationPrincipal User user, Model model) {
        var userName = user.getUsername();
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("userName", userName);
        model.addAttribute("book_name", "Мої книги:");
        model.addAttribute("books", books);
        return "books";
    }

    @PostMapping("/books/{id}")
    public String returnBook(@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Book book = bookRepository.findById(id)
                .orElseThrow(ClassNotFoundException::new);
        var userLibrarian = userRepository.findByUsername("Librarian");
        book.setReader(userLibrarian);
        book.setFree(true);
        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/users")
    public String users(Model model) throws ClassNotFoundException {
        Iterable<Book> books = bookRepository.findAll();
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("readers", "Всі Читачі");
        model.addAttribute("users", users);
        model.addAttribute("books", books);
        return "users";
    }

    @GetMapping("/reg")
    public String reg() {
        return "reg";
    }

    @PostMapping("/reg")
    public String addUser(User user, Model model) {
        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
