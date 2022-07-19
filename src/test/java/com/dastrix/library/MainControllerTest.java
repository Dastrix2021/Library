package com.dastrix.library;

import com.dastrix.library.controllers.MainController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/books-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/books-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "Librarian")
class MainControllerTest {

    @Autowired
    MainController mainController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void BooksForAllUsers() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='home-books']").nodeCount(1));
    }

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='user']").string("Вітаю: Librarian"));
    }

    @Test
    public void UserBooks() throws Exception {
        this.mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='user-books']").nodeCount(2));
    }

    @Test
    public void UsersAndBooks() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='users-and']").string("Librarian"))
                .andExpect(xpath("//*[@id='and-books']").string("first-book"));

    }

}