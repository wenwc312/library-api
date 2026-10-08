package org.example.library_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.admin.username=testadmin",
        "app.admin.password=testpass"
})
@AutoConfigureMockMvc
public class AdminAuthTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String Book_JSON = """
            {"name":"測試用書","author":"測試作者","ISBN":"t0001"}
            """;

    private static final String LOGIN_JSON = """
            {"username":"testadmin","password":"testpass"}
            """;

    @Test
    void 未登入新增書籍回傳401() throws Exception {
        mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Book_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 未登入進入管理頁面導向登入頁() throws Exception {
        mockMvc.perform(get("/admin.html"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login.html"));
    }

    @Test
    void 帳密錯誤登入失敗()  throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"testadmin","password":"wrong"}
                                """))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 登入後可以新增書籍() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/auth/login")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(LOGIN_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post("/books")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Book_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void 不需要登入的API可以正常使用() throws Exception {
        mockMvc.perform(get("/books/available"))
                .andExpect(status().isOk());
    }
}
