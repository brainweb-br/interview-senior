package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.Application;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsDTO;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsRepository;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.enums.Race;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("it")
public class HeroServiceTest {
    @InjectMocks
    private HeroService service;
    @Mock
    private PowerStatsService powerStatsService;
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransformationDtoToEntity() {
        LocalDateTime now = LocalDateTime.now();
        Short stat = Short.valueOf("100");
        UUID powerStatsId = UUID.randomUUID();
        PowerStatsDTO powerStatsDTO = PowerStatsDTO.builder()
                .id(powerStatsId)
                .strength(stat)
                .intelligence(stat)
                .agility(stat)
                .dexterity(stat)
                .createdAt(now)
                .updatedAt(now)
                .build();
        UUID heroId = UUID.randomUUID();
        String name = "Super-man";
        Race race = Race.ALIEN;
        HeroDTO heroDTO = HeroDTO.builder()
                .id(heroId)
                .name(name)
                .race(race)
                .enabled(true)
                .powerStats(powerStatsDTO)
                .createdAt(now)
                .updatedAt(now)
                .build();

        PowerStats powerStats = PowerStats.builder()
                .id(powerStatsId)
                .strength(stat)
                .intelligence(stat)
                .agility(stat)
                .dexterity(stat)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(powerStatsService.save(any(PowerStatsDTO.class))).thenReturn(powerStats);

        Hero hero = service.transformDtoToEntity(heroDTO);
        assertEquals(heroId, hero.getId());
        assertEquals(name, hero.getName());
        assertEquals(race, hero.getRace());
        assertTrue(hero.getEnabled());
        assertEquals(now, hero.getCreatedAt());
        assertEquals(now, hero.getUpdatedAt());
        PowerStats powerStats1 = hero.getPowerStats();
        assertEquals(stat, powerStats1.getAgility());
        assertEquals(stat, powerStats1.getDexterity());
        assertEquals(stat, powerStats1.getStrength());
        assertEquals(stat, powerStats1.getIntelligence());
        assertEquals(now, powerStats1.getCreatedAt());
        assertEquals(now, powerStats1.getUpdatedAt());
        assertEquals(powerStatsId, powerStats1.getId());
    }
}
