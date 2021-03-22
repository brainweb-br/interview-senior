package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class HeroService {
    private final static Logger logger = LoggerFactory.getLogger(HeroService.class);
    @Autowired
    private HeroRepository repository;
    @Autowired
    private PowerStatsService powerStatsService;

    @CacheEvict(cacheNames = {"Hero", "Heroes"}, allEntries = true)
    public HeroDTO save(HeroDTO heroDTO) {
        LocalDateTime now = LocalDateTime.now();
        if (heroDTO.getId() == null) {
            heroDTO.setCreatedAt(now);
        }
        heroDTO.setUpdatedAt(now);
        Hero hero = transformDtoToEntity(heroDTO);
        Hero saved = repository.save(hero);
        logger.info("Saved: " + saved.toString());
        return transformEntityToDto(saved);
    }

    @Cacheable(cacheNames = "Hero", key = "#id")
    public HeroDTO getById(UUID id) {
        Optional<Hero> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return transformEntityToDto(byId.get());
    }

    @Cacheable(cacheNames = "Heroes", key = "#name + 'list'", unless = "#result.size() == 0")
    public List<HeroDTO> getAll(String name) {
        Hero hero = Hero.builder().name(name).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        List<Hero> all = repository.findAll(Example.of(hero, matcher));
        logger.info("Quantity found: " + all.size());
        return all.stream().map(this::transformEntityToDto).collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "Heroes", allEntries = true, condition = "#result != null")
    }, put = {
            @CachePut(cacheNames = "Hero", key = "#id", condition = "#result != null")
    })
    public HeroDTO update(UUID id, HeroDTO heroDTO) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Hero> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        heroDTO.setUpdatedAt(now);
        Hero hero = transformDtoToEntity(heroDTO);
        Hero saved = repository.save(hero);
        logger.info("Updated: " + saved.toString());
        return transformEntityToDto(saved);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "Heroes", allEntries = true, condition = "#result != null"),
            @CacheEvict(cacheNames = "Hero", key = "#id", condition = "#result != null")
    })
    public HeroDTO delete(UUID id) {
        Optional<Hero> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        repository.deleteById(id);
        powerStatsService.delete(byId.get().getPowerStats().getId());
        return transformEntityToDto(byId.get());
    }

    public HeroComparisonResponseDTO compare(HeroComparisonRequestDTO heroComparisonRequestDTO) {
        HeroDTO hero1 = getById(heroComparisonRequestDTO.getHero1().getId());
        HeroDTO hero2 = getById(heroComparisonRequestDTO.getHero2().getId());
        if (hero1 == null || hero2 == null)
            return null;
        short agilityDiference = (short) (hero1.getPowerStats().getAgility() - hero2.getPowerStats().getAgility());
        short strengthDiference = (short) (hero1.getPowerStats().getStrength() - hero2.getPowerStats().getStrength());
        short dexterityDiference = (short) (hero1.getPowerStats().getDexterity() - hero2.getPowerStats().getDexterity());
        short intelligenceDiference = (short) (hero1.getPowerStats().getIntelligence() - hero2.getPowerStats().getIntelligence());
        List<Short> statsDiference = List.of(agilityDiference, strengthDiference, dexterityDiference, intelligenceDiference);
        List<Short> positive = new ArrayList<>();
        List<Short> negative = new ArrayList<>();
        statsDiference.forEach(i -> {
            if (i < 0) {
                negative.add(i);
            } else if (i > 0) {
                positive.add(i);
            }
        });
        HeroDTO stronger = null;
        if (positive.size() > negative.size()) {
            stronger = hero1;
        } else if (negative.size() > positive.size()) {
            stronger = hero2;
        } else {
            stronger = HeroDTO.builder().name("Heróis com status balanceados.").build();
        }
        return HeroComparisonResponseDTO.builder()
                .strongestHero(stronger)
                .agility(agilityDiference)
                .strength(strengthDiference)
                .dexterity(dexterityDiference)
                .intelligence(intelligenceDiference)
                .build();
    }

    public HeroDTO transformEntityToDto(Hero saved) {
        logger.info("Transforming hero to Entity - " + saved.toString());
        return HeroDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .race(saved.getRace())
                .enabled(saved.getEnabled())
                .powerStats(powerStatsService.transformEntityToDto(saved.getPowerStats()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public Hero transformDtoToEntity(HeroDTO heroDTO) {
        logger.info("Transforming hero to DTO - " + heroDTO.toString());
        return Hero.builder()
                .id(heroDTO.getId())
                .name(heroDTO.getName())
                .race(heroDTO.getRace())
                .enabled(heroDTO.isEnabled())
                .powerStats(powerStatsService.save(heroDTO.getPowerStats()))
                .updatedAt(heroDTO.getUpdatedAt())
                .createdAt(heroDTO.getCreatedAt())
                .build();
    }
}
