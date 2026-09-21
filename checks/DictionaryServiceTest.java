package com.example;

import com.example.preview.Level;
import com.example.preview.Levels;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DictionaryServiceTest {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        AtomicInteger hits = new AtomicInteger();
        server.createContext("/", exchange -> {
            hits.incrementAndGet();
            String path = exchange.getRequestURI().getPath();
            int status = path.equals("/apple") ? 200 : path.equals("/zzzzz") ? 404 : 503;
            String body = status == 200 ? "[{\"word\":\"apple\",\"meanings\":[]}]" : "{}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(status, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.close();
        });
        server.start();
        try {
            DictionaryService service = new DictionaryService("http://127.0.0.1:" + server.getAddress().getPort() + "/");
            assert service.check("ZZZZZ", false).join() == DictionaryService.Result.VALID;
            assert service.check("12A45", false).join() == DictionaryService.Result.INVALID;
            assert service.check("", false).join() == DictionaryService.Result.INVALID;
            assert hits.get() == 0 : "Offline practice must make zero HTTP requests";
            assert service.check("12345").join() == DictionaryService.Result.INVALID;
            assert service.check("APPLE").get(5, TimeUnit.SECONDS) == DictionaryService.Result.VALID;
            assert service.check("apple").join() == DictionaryService.Result.VALID;
            assert hits.get() == 1 : "Valid words should be cached";
            assert service.check("ZZZZZ").get(5, TimeUnit.SECONDS) == DictionaryService.Result.INVALID;
            assert service.check("ZZZZZ").join() == DictionaryService.Result.INVALID;
            assert hits.get() == 2 : "Unlisted words should be cached";
            assert service.check("ZZZZZ", false).join() == DictionaryService.Result.VALID;
            assert service.check("ZZZZZ", true).join() == DictionaryService.Result.INVALID;
            assert hits.get() == 2 : "Mode switches must not contaminate dictionary cache";
            assert service.check("QUAYS").get(5, TimeUnit.SECONDS) == DictionaryService.Result.UNAVAILABLE;
            assert service.check("QUAYS").get(5, TimeUnit.SECONDS) == DictionaryService.Result.UNAVAILABLE;
            assert hits.get() == 4 : "Outages must remain retryable";
            for (Level l : Levels.ALL) {
                assert service.check(l.across()).join() == DictionaryService.Result.VALID;
                assert service.check(l.down()).join() == DictionaryService.Result.VALID;
                assert !PirateHints.forWord(l.across()).contains("let the tile colors");
                assert !PirateHints.forWord(l.down()).contains("let the tile colors");
            }
            assert hits.get() == 4 : "Curated answers should work offline";
            assert DictionaryService.classify(429, "{}") == DictionaryService.Result.UNAVAILABLE;
            assert DictionaryService.classify(200, "<html>error</html>") == DictionaryService.Result.UNAVAILABLE;
            System.out.println("PASS: async HTTP lookups, approval/rejection, caching, retryable errors, offline level answers and all pirate hints.");
        } finally { server.stop(0); }
    }
}
