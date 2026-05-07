package com.example.ci_cd_demo.controller;

import com.example.ci_cd_demo.model.Todo;
import com.example.ci_cd_demo.repository.TodoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void testCreateTodo() throws Exception {
        Todo todo = new Todo(null, "Test Todo", false);
        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Todo"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void testGetAllTodos() throws Exception {
        todoRepository.save(new Todo(null, "Todo 1", false));
        todoRepository.save(new Todo(null, "Todo 2", true));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetTodoById() throws Exception {
        Todo savedTodo = todoRepository.save(new Todo(null, "Todo 1", false));

        mockMvc.perform(get("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Todo 1"));
    }

    @Test
    void testUpdateTodo() throws Exception {
        Todo savedTodo = todoRepository.save(new Todo(null, "Old Title", false));
        Todo updatedTodo = new Todo(null, "New Title", true);

        mockMvc.perform(put("/api/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void testDeleteTodo() throws Exception {
        Todo savedTodo = todoRepository.save(new Todo(null, "To delete", false));

        mockMvc.perform(delete("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isNotFound());
    }
}
