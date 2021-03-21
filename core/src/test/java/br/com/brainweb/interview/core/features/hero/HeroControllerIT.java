package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.Application;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsDTO;
import br.com.brainweb.interview.enums.Race;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("it")
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HeroControllerIT {
    @Autowired
    private MockMvc mvc;

    @Test
    public void createHero() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("Spider-man");
        String json = getObjectJson(fullHeroDto);
        mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    private String getObjectJson(HeroDTO heroDTO) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(heroDTO);
    }

    private HeroDTO getFullHeroDto() {
        PowerStatsDTO powerStatsDTO = PowerStatsDTO.builder()
                .intelligence(Short.parseShort("100"))
                .agility(Short.parseShort("100"))
                .dexterity(Short.parseShort("100"))
                .strength(Short.parseShort("100"))
                .build();
        return HeroDTO.builder()
                .name("Super-man")
                .race(Race.ALIEN)
                .enabled(true)
                .powerStats(powerStatsDTO)
                .build();
    }

    @Test
    public void testGetById() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        String json = getObjectJson(fullHeroDto);
        MvcResult mvcResult = mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        HeroDTO heroDTO = mapper.readValue(contentAsString, HeroDTO.class);

        mvc.perform(get("/api/v1/hero/" + heroDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(fullHeroDto.getName())))
                .andExpect(jsonPath("$.race", is(fullHeroDto.getRace().toString())))
                .andExpect(jsonPath("$.id", is(heroDTO.getId().toString())));
    }

    @Test
    public void testShouldReturn404IfNotFound() throws Exception {

        mvc.perform(get("/api/v1/hero/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(jsonPath("$", is("Herói não encontrado")));

    }

    @Test
    public void testShouldListAllHeroes() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("Wonder-woman");
        String json = getObjectJson(fullHeroDto);
        mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
        mvc.perform(get("/api/v1/hero"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    public void testShouldFilterByNameHeroes() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("Birdman");
        String json = getObjectJson(fullHeroDto);
        mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
        mvc.perform(get("/api/v1/hero?name=bird"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    public void testShouldBringEmptyHeroList() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("Birdwoman");
        String json = getObjectJson(fullHeroDto);
        mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()));
        mvc.perform(get("/api/v1/hero"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)));
        mvc.perform(get("/api/v1/hero?name=crazyname"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    public void testShouldUpdate() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("manbat");
        String json = getObjectJson(fullHeroDto);
        MvcResult mvcResult = mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        HeroDTO heroDTO = mapper.readValue(contentAsString, HeroDTO.class);
        heroDTO.setName("Batman");
        json = getObjectJson(heroDTO);

        mvc.perform(put("/api/v1/hero/" + heroDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Batman")))
                .andExpect(jsonPath("$.race", is(fullHeroDto.getRace().toString())))
                .andExpect(jsonPath("$.id", is(heroDTO.getId().toString())));
    }

    @Test
    public void testShouldReturn404WhenNotFindingInUpdate() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        fullHeroDto.setName("manbat2");
        String json = getObjectJson(fullHeroDto);
        MvcResult mvcResult = mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        HeroDTO heroDTO = mapper.readValue(contentAsString, HeroDTO.class);
        heroDTO.setName("Batman");
        json = getObjectJson(heroDTO);

        mvc.perform(put("/api/v1/hero/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(jsonPath("$", is("Herói não encontrado")));
    }

    @Test
    public void testShouldDelete() throws Exception {
        HeroDTO fullHeroDto = getFullHeroDto();
        String json = getObjectJson(fullHeroDto);
        MvcResult mvcResult = mvc.perform(post("/api/v1/hero")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue())).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        HeroDTO heroDTO = mapper.readValue(contentAsString, HeroDTO.class);

        mvc.perform(get("/api/v1/hero/" + heroDTO.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(fullHeroDto.getName())))
                .andExpect(jsonPath("$.race", is(fullHeroDto.getRace().toString())))
                .andExpect(jsonPath("$.id", is(heroDTO.getId().toString())));

        mvc.perform(delete("/api/v1/hero/" + heroDTO.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/api/v1/hero/" + heroDTO.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(jsonPath("$", is("Herói não encontrado")));
    }

    @Test
    public void testShouldReturn404IfNotFoundWhenDeleting() throws Exception {
        mvc.perform(delete("/api/v1/hero/" + UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(jsonPath("$", is("Herói não encontrado")));
    }

}

