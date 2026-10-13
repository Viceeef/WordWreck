package backend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordScraper {
    public static void main() {
        int num; 

        try (BufferedReader reader = new BufferedReader(new FileReader("example.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
