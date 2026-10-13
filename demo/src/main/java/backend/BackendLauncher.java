package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;


public class BackendLauncher {
    public static void main (String[] args) throws IOException {
        long num = 0;
        Random rand = new Random();
        Path path = Path.of("demo/src/main/resources/words.txt");

        num = Files.lines(path).count();
        int word1 = rand.nextInt((int)num);

        String randomWord = Files.lines(path)
                .skip(word1)
                .findFirst()
                .orElse("Empty line");

        System.out.println(randomWord);






    }
}