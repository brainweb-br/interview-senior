package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.Application;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsDTO;
import br.com.brainweb.interview.enums.Race;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("it")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void createHero() throws Exception {
        PowerStatsDTO powerStatsDTO = PowerStatsDTO.builder()
                .intelligence(Short.parseShort("100"))
                .agility(Short.parseShort("100"))
                .dexterity(Short.parseShort("100"))
                .strength(Short.parseShort("100"))
                .build();
        HeroDTO heroDTO = HeroDTO.builder()
                .name("Super-man")
                .race(Race.ALIEN)
                .enabled(true)
                .powerStats(powerStatsDTO)
                .build();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(heroDTO);

        mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

}

