package umc.SukBakJi.global.filter;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Component
public class BadWordFilter {

    private final Set<String> badWords = new HashSet<>();

    @PostConstruct
    public void init() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("badwords.txt");
        if (inputStream == null) {
            throw new FileNotFoundException("badwords.txt not found");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                badWords.add(line.trim().toLowerCase());
            }
        }
    }

    public boolean containsBadWord(String input) {
        if (input == null) return false;

        String normalized = normalize(input);
        for (String badWord : badWords) {
            if (normalized.contains(badWord)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String input) {
        return input
                .toLowerCase()
                .replaceAll("[^ㄱ-ㅎ가-힣a-zA-Z0-9]", "") // 특수문자 제거
                .replaceAll("ㅅ+", "ㅅ")
                .replaceAll("ㅂ+", "ㅂ")
                .replaceAll("시+", "시")
                .replaceAll("발+", "발")
                .replaceAll("\\s+", ""); // 공백 제거
    }

}
