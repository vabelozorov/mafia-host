package ua.belozorov.mafia.host.gameengine;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class GameControllerTest {

    @MockBean
    GameService gameService;

    @Autowired
    MockMvc mvc;

    @Test
    void getGameById() throws Exception {

        when(gameService.getById(1L)).thenReturn(new ClassicalGame(1L, 12));

        mvc.perform(get("/games/1"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(APPLICATION_JSON),
                        jsonPath("$.id", equalTo(1)),
                        jsonPath("$.state", equalTo("assigning_roles")),
                        jsonPath("$.gameConfig.roles[?(@.name == \"sheriff\")].count", hasItem(1)),
                        jsonPath("$.gameConfig.roles[?(@.name == \"citizen\")].count", hasItem(7)),
                        jsonPath("$.gameConfig.roles[?(@.name == \"don\")].count", hasItem(1)),
                        jsonPath("$.gameConfig.roles[?(@.name == \"mafia\")].count", hasItem(3)),
                        jsonPath("$.gameConfig.roles[?(@.name == \"sheriff\")].team", hasItem("red"))
                );
    }
}