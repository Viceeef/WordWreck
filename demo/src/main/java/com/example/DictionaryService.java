package com.example;

import com.example.preview.Levels;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/** Only a confirmed dictionary word can enable Enter. No API key needed. */
public final class DictionaryService {
    public enum Result { VALID, INVALID, UNAVAILABLE }
    private final String endpoint;
    private final Map<String, Result> cache = new ConcurrentHashMap<>();
    private final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5)).build();

    public DictionaryService() {
        this("https://api.dictionaryapi.dev/api/v2/entries/en/");
    }
    DictionaryService(String endpoint) {
        this.endpoint = endpoint;
        // Curated level answers remain playable even if the service omits a plural.
        Levels.ALL.forEach(level -> {
            cache.put(level.across(), Result.VALID);
            cache.put(level.down(), Result.VALID);
        });
    }

    public CompletableFuture<Result> check(String input) {
        return check(input, true);
    }

    public CompletableFuture<Result> check(String input, boolean online) {
        String word = input == null ? "" : input.toUpperCase(Locale.ROOT);
        if (!word.matches("[A-Z]{5,9}")) {
            return CompletableFuture.completedFuture(Result.INVALID);
        }
        if (!online) return CompletableFuture.completedFuture(Result.VALID);
        Result saved = cache.get(word);
        if (saved != null) return CompletableFuture.completedFuture(saved);
        HttpRequest request = HttpRequest.newBuilder(URI.create(
            endpoint + word.toLowerCase(Locale.ROOT)))
            .timeout(Duration.ofSeconds(7))
            .header("Accept", "application/json").GET().build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .handle((response, error) -> {
                if (error != null) return Result.UNAVAILABLE;
                Result result = classify(response.statusCode(), response.body());
                if (result != Result.UNAVAILABLE) cache.put(word, result);
                return result;
            });
    }

    static Result classify(int status, String body) {
        if (status == 404) return Result.INVALID;
        if (status == 200 && body != null && body.trim().startsWith("[")
                && body.contains("\"word\"") && body.contains("\"meanings\"")) {
            return Result.VALID;
        }
        // Timeouts, throttling and server errors are NOT proof a word is invalid.
        return Result.UNAVAILABLE;
    }
}
