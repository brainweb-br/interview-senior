package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsRepository;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public HeroDTO transformEntityToDto(Hero saved) {
        logger.info("Transforming DTO to Entity - " + saved.toString());
        return HeroDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .race(saved.getRace())
                .enabled(saved.isEnabled())
                .powerStats(powerStatsService.transformEntityToDto(saved.getPowerStats()))
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    public Hero transformDtoToEntity(HeroDTO heroDTO) {
        logger.info("Transforming Entity to DTO - " + heroDTO.toString());
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
