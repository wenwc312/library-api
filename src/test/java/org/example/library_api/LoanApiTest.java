package org.example.library_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoanApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 借書成功回傳200() throws Exception {
        mockMvc.perform(post("/books/borrow").param("userId","U0001").param("isbn","s0001"))
                .andExpect(status().isOk());
    }

    @Test
    void 還書成功回傳200() throws Exception {
        mockMvc.perform(post("/books/borrow").param("userId","U0001").param("isbn","s0002"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/books/return").param("userId","U0001").param("isbn","s0002"))
                .andExpect(status().isOk());
    }

    @Test
    void 借不存在的書回傳404() throws Exception{
        mockMvc.perform(post("/books/borrow").param("userId","U0001").param("isbn","s9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void 查無會員借書還傳404() throws Exception{
        mockMvc.perform(post("/books/borrow").param("userId","notExist").param("isbn","s9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void 借已被借走的書回傳409() throws Exception{
        mockMvc.perform(post("/books/borrow").param("userId", "U0001").param("isbn", "f0001"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/books/borrow").param("userId", "U0002").param("isbn", "f0001"))
                .andExpect(status().isConflict());
    }

    @Test
    void 還沒借過的書回傳409() throws Exception{
        mockMvc.perform(post("/books/return").param("userId", "U0002").param("isbn", "h0001"))
                .andExpect(status().isConflict());
    }
}
