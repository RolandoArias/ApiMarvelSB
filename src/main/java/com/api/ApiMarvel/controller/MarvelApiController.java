package com.api.ApiMarvel.controller;

import com.api.ApiMarvel.entity.Character;
import com.api.ApiMarvel.entity.Comic;
import com.api.ApiMarvel.entity.SearchHistory;


import com.api.ApiMarvel.entity.User;
import com.api.ApiMarvel.dtos.LoginUserDto;
import com.api.ApiMarvel.dtos.RegisterUserDto;
import com.api.ApiMarvel.response.LoginResponse;
import com.api.ApiMarvel.service.AuthenticationService;
import com.api.ApiMarvel.service.JwtService;


import com.api.ApiMarvel.service.MarvelApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/marvel")
public class MarvelApiController {

    private final MarvelApiService marvelApiService;

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;




    @Autowired
    public MarvelApiController(MarvelApiService marvelApiService,JwtService jwtService, AuthenticationService authenticationService) {
        this.marvelApiService = marvelApiService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }


    @GetMapping("/searches")
    public ResponseEntity<List<SearchHistory>> getAllSearches() {
        List<SearchHistory> searches = marvelApiService.findAllSearchHistories();
        if (searches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searches);
    }

    @GetMapping("/users/{userId}/searches")
    public ResponseEntity<List<SearchHistory>> getUserSearches(@PathVariable Long userId) {
        List<SearchHistory> searches = marvelApiService.findSearchHistoryByUserId(userId);
        if (searches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searches);
    }


    @PostMapping("/public/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/public/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/public/characters-api")
    public String getMarvelCharacters(
            @RequestParam(required = false) String id
    ) {
        System.out.println("id: " + id);
        try {

            return marvelApiService.getMarvelCharacters(id);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/public/comics-api")
    public String getMarvelComics(
            @RequestParam(required = false) String nameStartsWith,
            @RequestParam(required = false) String id
    ) {
        System.out.println("id: " + id);
        try {

            return marvelApiService.getMarvelComics(nameStartsWith,id);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @GetMapping("/public/events-api")
    public String getMarvelEvents(
            @RequestParam(required = false) String nameStartsWith,
            @RequestParam(required = false) String id
    ) {
        System.out.println("id: " + id);
        try {

            return marvelApiService.getMarvelEvents(nameStartsWith,id);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



}
