package br.com.brainweb.interview.core.features.hero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
            return new ResponseEntity<>("Herói não encontrado", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(hero, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<HeroDTO>> getAll(@RequestParam(required = false) String name) {
        logger.info("get all by filter: " + name);
        List<HeroDTO> all = heroService.getAll(name);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody HeroDTO hero) {
        logger.info("Update: " + hero);
        HeroDTO heroSaved = heroService.update(id, hero);
        if (heroSaved == null)
            return new ResponseEntity("Herói não encontrado", HttpStatus.NOT_FOUND);
        return new ResponseEntity(hero, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        logger.info("Delete: " + id);
        HeroDTO heroDeleted = heroService.delete(id);
        if (heroDeleted == null)
            return new ResponseEntity("Herói não encontrado", HttpStatus.NOT_FOUND);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/compare")
    public ResponseEntity<HeroComparisonResponseDTO> compareHeroes(@RequestBody HeroComparisonRequestDTO heroComparisonRequestDTO) {
        HeroComparisonResponseDTO compare = heroService.compare(heroComparisonRequestDTO);
        if (compare == null)
            return new ResponseEntity("Um dos heróis não foi encontrado", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(compare, HttpStatus.OK);
    }

}
