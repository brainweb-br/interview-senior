package br.com.brainweb.interview.core.features.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/hero")
public class HeroController {
    private final static Logger logger = LoggerFactory.getLogger(HeroController.class);

    @Autowired
    private HeroService heroService;

    @PostMapping
    private ResponseEntity<HeroDTO> save(@RequestBody HeroDTO hero) {
        logger.info("Savind hero: " + hero.toString());
        HeroDTO saved = heroService.save(hero);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
