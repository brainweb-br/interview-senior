package br.com.brainweb.interview.core.features.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/hero")
public class HeroController {
    private final static Logger logger = LoggerFactory.getLogger(HeroController.class);

    @Autowired
    private HeroService heroService;

    @PostMapping
    public ResponseEntity<HeroDTO> save(@RequestBody HeroDTO hero) {
        logger.info("Savind hero: " + hero.toString());
        HeroDTO saved = heroService.save(hero);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        logger.info("Fetch by id: " + id);
        HeroDTO hero = heroService.getById(id);
        if (hero == null)
            return new ResponseEntity("Herói não encontrado", HttpStatus.NOT_FOUND);
        return new ResponseEntity(hero, HttpStatus.OK);
    }
}
