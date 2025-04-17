package umc.SukBakJi.global.filter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BadWordFilterTest {

    @Autowired
    private BadWordFilter badWordFilter;


    @Test
    void testContainsBadWord_whenInputHasBadWord_shouldReturnTrue() {
        String input = "넌 정말 멍청이 같아";
        boolean result = badWordFilter.containsBadWord(input);
        assertThat(result).isTrue();
    }


    @Test
    void testContainsBadWord_whenInputHasNoBadWord_shouldReturnFalse() {
        String input = "오늘 날씨 너무 좋다!";
        boolean result = badWordFilter.containsBadWord(input);
        assertThat(result).isFalse();
    }

    @Test
    void testContainsBadWord_whenInputIsNull_shouldReturnFalse() {
        boolean result = badWordFilter.containsBadWord(null);
        assertThat(result).isFalse();
    }

    @Test
    void testContainsBadWord_whenSpacedBadWord_shouldReturnTrue() {
        String input = "ㅅ ㅂ 진짜 왜 이래";
        assertThat(badWordFilter.containsBadWord(input)).isTrue();
    }

    @Test
    void testContainsBadWord_whenSpecialCharBadWord_shouldReturnTrue() {
        String input = "시ㅡ발 뭐냐고";
        assertThat(badWordFilter.containsBadWord(input)).isTrue();
    }
}