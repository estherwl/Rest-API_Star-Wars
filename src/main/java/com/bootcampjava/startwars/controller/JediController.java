package com.bootcampjava.startwars.controller;


import com.bootcampjava.startwars.model.Jedi;
import com.bootcampjava.startwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class JediController {

    private static final Logger logger = LogManager.getLogger(JediController.class);

    private final JediService jediService;

    public JediController(JediService jediService) {
        this.jediService = jediService;
    }

    @GetMapping("/jedi/{id}")
    public ResponseEntity<?> getJedi(@PathVariable Integer id) {

        return jediService.findById(id)
                .map(jedi -> {
                    try {
                        return ResponseEntity
                                .ok()
                                .eTag(Integer.toString(jedi.getVersion()))
                                .location(new URI("/jedi/" + jedi.getId()))
                                .body(jedi);
                    } catch (URISyntaxException e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/jedi")
    public ResponseEntity<Jedi> saveJedi(@RequestBody Jedi jedi) {

        Jedi newJedi = jediService.save(jedi);

        try {
            return ResponseEntity
                    .created(new URI("/jedi/" + newJedi.getId()))
                    .eTag(Integer.toString(newJedi.getVersion()))
                    .body(newJedi);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/jedi/{id}")
    public ResponseEntity<Jedi> updateJedi(@RequestBody Jedi jedi, @PathVariable Integer id) {

//        Optional<Jedi> jediDeclarated = jediService.findById(id);
//
//        jediDeclarated.map(j -> {
//            j.setName(jedi.getName());
//            j.setStrength(jedi.getStrength());
//            j = jediService.save(j);
//            return jediDeclarated;
//        });

        try {
            jediService.findById(id);
            return ResponseEntity
                    .ok()
                    .location(new URI("/jedi/" + jedi.getId()))
                    .eTag(Integer.toString(jedi.getVersion()))
                    .body(jedi);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/jedi/{id}")
    public ResponseEntity<?> deleteJedi(@PathVariable Integer id) {
        return jediService.findById(id).map(jedi -> {
            if (jediService.delete(jedi.getId())) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }).orElse(ResponseEntity.notFound().build());
    }

}
