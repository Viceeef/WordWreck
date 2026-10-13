package com.example;

import com.example.preview.Levels;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * Checks a complete guess against the optional online dictionary. The result
 * controls Enter in PrimaryController; this class does not consume attempts.
 */
public final class DictionaryService {
    public enum Result { VALID, INVALID, VALID_BUILT_IN, INVALID_BUILT_IN, UNAVAILABLE }
    private final String dictionaryEndpoint;
    private final boolean useDatamuseFirst;
    private final Set<String> builtInWords = loadBuiltInWords();
    private final Map<String, Result> cache = new ConcurrentHashMap<>();
    private final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5)).build();

    public DictionaryService() {
        this("https://api.dictionaryapi.dev/api/v2/entries/en/", true);
    }
    DictionaryService(String endpoint) {
        this(endpoint, false);
    }
    private DictionaryService(String endpoint, boolean useDatamuseFirst) {
        this.dictionaryEndpoint = endpoint;
        this.useDatamuseFirst = useDatamuseFirst;
        // Curated level answers remain playable even if the service omits a plural.
        Levels.ALL.forEach(level -> {
            cache.put(level.across(), Result.VALID);
            cache.put(level.down(), Result.VALID);
        });
    }

    public CompletableFuture<Result> validateWord(String input) {
        return validateWord(input, true);
    }

    /** online=false accepts a complete A-Z word without making an HTTP request. */
    public CompletableFuture<Result> validateWord(String input, boolean online) {
        String word;
        if (input == null) {
            word = "";
        } else {
            word = input.toUpperCase(Locale.ROOT);
        }
        if (!word.matches("[A-Z]{5,9}")) {
            return CompletableFuture.completedFuture(Result.INVALID);
        }
        if (!online) return CompletableFuture.completedFuture(Result.VALID);
        Result saved = cache.get(word);
        if (saved != null) return CompletableFuture.completedFuture(saved);
        CompletableFuture<Result> onlineResult;
        if (useDatamuseFirst) {
            onlineResult = checkDatamuse(word).thenCompose(result -> {
                if (result == Result.UNAVAILABLE) {
                    return checkFreeDictionary(word);
                } else {
                    return CompletableFuture.completedFuture(result);
                }
            });
        } else {
            onlineResult = checkFreeDictionary(word);
        }
        return onlineResult.thenApply(result -> {
            if (result == Result.UNAVAILABLE) {
                if (builtInWords.isEmpty()) return Result.UNAVAILABLE;
                if (builtInWords.contains(word)) {
                    return Result.VALID_BUILT_IN;
                } else {
                    return Result.INVALID_BUILT_IN;
                }
            }
            cache.put(word, result);
            return result;
        });
    }

    private CompletableFuture<Result> checkDatamuse(String word) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(
            "https://api.datamuse.com/words?sp=" + word.toLowerCase(Locale.ROOT) + "&max=1"))
            .timeout(Duration.ofSeconds(5))
            .header("Accept", "application/json").GET().build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .handle((response, error) -> {
                if (error == null) {
                    return classifyDatamuseResponse(response.statusCode(), response.body(), word);
                } else {
                    return Result.UNAVAILABLE;
                }
            });
    }

    private CompletableFuture<Result> checkFreeDictionary(String word) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(
            dictionaryEndpoint + word.toLowerCase(Locale.ROOT)))
            .timeout(Duration.ofSeconds(7))
            .header("Accept", "application/json").GET().build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .handle((response, error) -> {
                if (error != null) return Result.UNAVAILABLE;
                return classifyHttpResponse(response.statusCode(), response.body());
            });
    }

    /** Datamuse returns an empty list for an unlisted word; require an exact match. */
    static Result classifyDatamuseResponse(int status, String body, String word) {
        if (status != 200 || body == null) return Result.UNAVAILABLE;
        String response = body.trim();
        if (response.equals("[]")) return Result.INVALID;
        if (!response.startsWith("[") || !response.endsWith("]")) return Result.UNAVAILABLE;
        String exactWord = "\"word\"\\s*:\\s*\"" + word.toLowerCase(Locale.ROOT) + "\"";
        boolean exactMatchFound = java.util.regex.Pattern.compile(exactWord, java.util.regex.Pattern.CASE_INSENSITIVE)
            .matcher(response).find();
        if (exactMatchFound) {
            return Result.VALID;
        } else {
            return Result.INVALID;
        }
    }

    private static Set<String> loadBuiltInWords() {
        InputStream stream = DictionaryService.class.getResourceAsStream("/words.txt");
        if (stream == null) return Set.of();
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase(Locale.ROOT);
                if (word.matches("[A-Z]{5,9}")) words.add(word);
            }
        } catch (IOException error) {
            return Set.of();
        }
        return Set.copyOf(words);
    }

    /** A server failure is retryable; it must never count as a wrong guess. */
    static Result classifyHttpResponse(int status, String body) {
        if (status == 404) return Result.INVALID;
        if (status == 200 && body != null && body.trim().startsWith("[")
                && body.contains("\"word\"") && body.contains("\"meanings\"")) {
            return Result.VALID;
        }
        // Timeouts, throttling and server errors are NOT proof a word is invalid.
        return Result.UNAVAILABLE;
    }
}
