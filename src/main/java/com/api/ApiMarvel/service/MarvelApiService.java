package com.api.ApiMarvel.service;

import com.api.ApiMarvel.entity.Character;
import com.api.ApiMarvel.entity.Comic;
import com.api.ApiMarvel.entity.SearchHistory;
import com.api.ApiMarvel.entity.User;
import com.api.ApiMarvel.repository.CharacterRepository;
import com.api.ApiMarvel.repository.ComicRepository;
import com.api.ApiMarvel.repository.SearchHistoryRepository;
import com.api.ApiMarvel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Formatter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class MarvelApiService {

    private final CharacterRepository characterRepository;
    private final ComicRepository comicRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;


    @Value("${marvel.api.publicKey}")
    private String publicKey;

    @Value("${marvel.api.privateKey}")
    private String privateKey;

    @Value("${marvel.api.baseUrl}")
    private String baseUrl;

    private final WebClient webClient;


    @Autowired
    public MarvelApiService(CharacterRepository characterRepository,
                            ComicRepository comicRepository,
                            SearchHistoryRepository searchHistoryRepository,
                            UserRepository userRepository,
                            WebClient.Builder webClientBuilder
    ) {
        this.characterRepository = characterRepository;
        this.comicRepository = comicRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.userRepository = userRepository;
        this.webClient = webClientBuilder.build();
    }

    /**
     * Busca un personaje por su ID.
     * @param id El ID del personaje.
     * @return El personaje si es encontrado, o `Optional.empty()` si no.
     */
    public Optional<Character> findCharacterById(Long id) {
        return characterRepository.findById(id);
    }

    /**
     * Busca un cómic por su ID.
     * @param id El ID del cómic.
     * @return El cómic si es encontrado, o `Optional.empty()` si no.
     */
    public Optional<Comic> findComicById(Long id) {
        return comicRepository.findById(id);
    }

    /**
     * Guarda el historial de búsqueda.
     * @param searchHistory El historial de búsqueda a guardar.
     * @return El historial de búsqueda guardado.
     */
    public SearchHistory saveSearchHistory(SearchHistory searchHistory) {
        return searchHistoryRepository.save(searchHistory);
    }

    /**
     * Obtiene el historial de búsqueda de un usuario.
     * @param userId El ID del usuario.
     * @return La lista de historiales de búsqueda del usuario.
     */
    public List<SearchHistory> findSearchHistoryByUserId(Long userId) {
        return searchHistoryRepository.findByUserId(userId);
    }

    public void setHistoryUser(String entityType,String entity) {
        if (isUserLoggedIn()) {

            var user = getCurrentUser();
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setUser(user);
            searchHistory.setEntityType(entityType);
            searchHistory.setEntity(entity);
            searchHistory.setSearchTime(LocalDateTime.now());

            searchHistoryRepository.save(searchHistory);
        }
    }

    private boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String &&
                        authentication.getPrincipal().equals("anonymousUser"));
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
            return userRepository.findByEmail(email);
        }
        throw new IllegalStateException("User not found or not logged in");
    }


    /**
     * Busca personajes por su nombre.
     * @param name El nombre del personaje.
     * @return La lista de personajes encontrados.
     */
    public List<Character> searchCharactersByName(String name) {
        return characterRepository.findByNameContainingIgnoreCase(name);
    }


    /**
     * Obtiene todos los cómics.
     * @return La lista de todos los cómics.
     */
    public List<Comic> findAllComics() {
        return comicRepository.findAll();
    }

    /**
     * Obtiene todos los historiales de búsqueda.
     * @return La lista de todos los historiales de búsqueda.
     */
    public List<SearchHistory> findAllSearchHistories() {
        return searchHistoryRepository.findAll();
    }

    private String generateHash(String ts) throws NoSuchAlgorithmException {
        String valueToHash = ts + privateKey + publicKey;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(valueToHash.getBytes(StandardCharsets.UTF_8));
        return byteArrayToHex(hashBytes);
    }

    private String byteArrayToHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

    private UriComponentsBuilder buildUri(String endpoint, String ts, String hash) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl + endpoint)
                .queryParam("ts", ts)
                .queryParam("apikey", publicKey)
                .queryParam("hash", hash)
                .queryParam("limit", 5);
    }

    public String getMarvelComics(
            String nameStartsWith,
            String id
    ) throws NoSuchAlgorithmException {
        String ts = String.valueOf(Instant.now().getEpochSecond());
        String hash = generateHash(ts);
        String endpoint = "/comics";

        if (id != null && !id.isEmpty()) {
            endpoint = endpoint + "/" + id;
            setHistoryUser("comics",id);
        }
        System.out.println("Endpoint: " + endpoint);
        UriComponentsBuilder uriBuilder = buildUri(endpoint, ts, hash);

        // Si nameStartsWith no es nulo, agrega el parámetro a la URL
        if (nameStartsWith != null && !nameStartsWith.isEmpty()) {
            uriBuilder.queryParam("nameStartsWith", nameStartsWith);
        }

        String uri = uriBuilder.toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getMarvelCharacters(
            String id
    ) throws NoSuchAlgorithmException {
        String ts = String.valueOf(Instant.now().getEpochSecond());
        String hash = generateHash(ts);
        String endpoint = "/characters";

        if (id != null && !id.isEmpty()) {
            endpoint = endpoint + "/" + id;
            // Guardar el historial de búsqueda solo si el usuario ha iniciado sesión
            setHistoryUser("characters",id);
        }
        System.out.println("Endpoint: " + endpoint);
        UriComponentsBuilder uriBuilder = buildUri(endpoint, ts, hash);

        String uri = uriBuilder.toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public String getMarvelEvents(
            String nameStartsWith,
            String id
    ) throws NoSuchAlgorithmException {
        String ts = String.valueOf(Instant.now().getEpochSecond());
        String hash = generateHash(ts);
        String endpoint = "/events";

        if (id != null && !id.isEmpty()) {
            endpoint = endpoint + "/" + id;
            setHistoryUser("events",id);
        }
        System.out.println("Endpoint: " + endpoint);
        UriComponentsBuilder uriBuilder = buildUri(endpoint, ts, hash);

        // Si nameStartsWith no es nulo, agrega el parámetro a la URL
        if (nameStartsWith != null && !nameStartsWith.isEmpty()) {
            uriBuilder.queryParam("nameStartsWith", nameStartsWith);
        }

        String uri = uriBuilder.toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


}
