package br.com.brainweb.interview.core.features.powerstats;


import br.com.brainweb.interview.model.PowerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PowerStatsService {
    private final static Logger logger = LoggerFactory.getLogger(PowerStatsService.class);

    @Autowired
    private PowerStatsRepository repository;

    public PowerStats save(PowerStatsDTO powerStatsDTO) {
        logger.info("Saving powerStats: " + powerStatsDTO.toString());
        PowerStats powerStats = transformDtoToEntity(powerStatsDTO);
        LocalDateTime now = LocalDateTime.now();
        powerStats.setUpdatedAt(now);
        if (powerStats.getId() == null) {
            powerStats.setCreatedAt(now);
        }
        return repository.save(powerStats);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public PowerStats transformDtoToEntity(PowerStatsDTO powerStatsDTO) {
        logger.info("Transforming powerStats to entity: " + powerStatsDTO.toString());
        return PowerStats.builder()
                .id(powerStatsDTO.getId())
                .agility(powerStatsDTO.getAgility())
                .dexterity(powerStatsDTO.getDexterity())
                .strength(powerStatsDTO.getStrength())
                .intelligence(powerStatsDTO.getIntelligence())
                .createdAt(powerStatsDTO.getCreatedAt())
                .updatedAt(powerStatsDTO.getUpdatedAt())
                .build();
    }

    public PowerStatsDTO transformEntityToDto(PowerStats powerStats) {
        logger.info("Transforming powerStats to DTO: " + powerStats.toString());
        return PowerStatsDTO.builder()
                .id(powerStats.getId())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .strength(powerStats.getStrength())
                .intelligence(powerStats.getIntelligence())
                .createdAt(powerStats.getCreatedAt())
                .updatedAt(powerStats.getUpdatedAt())
                .build();
    }
}
