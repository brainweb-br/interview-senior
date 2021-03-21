package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public HeroDTO getById(UUID id) {
        Optional<Hero> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        return transformEntityToDto(byId.get());
    }

    public List<HeroDTO> getAll(String name) {
        Hero hero = Hero.builder().name(name).build();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();

        List<Hero> all = repository.findAll(Example.of(hero, matcher));
        logger.info("Quantity found: " + all.size());
        return all.stream().map(this::transformEntityToDto).collect(Collectors.toList());
    }

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

    public HeroDTO delete(UUID id) {
        Optional<Hero> byId = repository.findById(id);
        if (byId.isEmpty()) {
            return null;
        }
        repository.deleteById(id);
        return transformEntityToDto(byId.get());
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
