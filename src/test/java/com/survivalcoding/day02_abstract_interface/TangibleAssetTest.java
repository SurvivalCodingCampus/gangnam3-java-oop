package com.survivalcoding.day02_abstract_interface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TangibleAsset 클래스 테스트")
public class TangibleAssetTest {

    private static final String EXPECTED_ERROR_MESSAGE = "설정할 무게는 0.0 이상 180.0 이하입니다";
    private TangibleAsset tangibleAsset;

    @BeforeEach
    void setUp() {
        tangibleAsset = new TangibleAsset() {
        };
    }

    @Test
    @DisplayName("무게가 0 미만이거나 180 초과인 경우 IllegalArgumentException 예외가 발생해야 한다")
    void setWeight_shouldThrowException_whenWeightIsOutOfRange() {
        // 하한 경계값 바깥: -0.1, -1.0
        assertThrows(IllegalArgumentException.class, () -> tangibleAsset.setWeight(-0.1));
        assertThrows(IllegalArgumentException.class, () -> tangibleAsset.setWeight(-1.0));

        // 상한 경계값 바깥: 180.1, 181.0
        assertThrows(IllegalArgumentException.class, () -> tangibleAsset.setWeight(180.1));
        assertThrows(IllegalArgumentException.class, () -> tangibleAsset.setWeight(181.0));
    }

    @Test
    @DisplayName("예외 발생 시 올바른 에러 메시지를 반환해야 한다")
    void setWeight_shouldReturnCorrectErrorMessage_whenThrowsException() {
        // given
        double invalidWeight = -1.0;

        // when
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> tangibleAsset.setWeight(invalidWeight));

        // then
        assertEquals(EXPECTED_ERROR_MESSAGE, error.getMessage());
    }

    @Test
    @DisplayName("정상 범위(0.0 이상 180.0 이하)의 무게는 정상적으로 설정되어야 한다")
    void setWeight_shouldSetWeightSuccessfully_whenWeightIsInRange() {
        // 하한 경계값 (0.0)
        tangibleAsset.setWeight(0.0);
        assertEquals(0.0, tangibleAsset.getWeight());

        // 중간 정상값 (90.0)
        tangibleAsset.setWeight(90.0);
        assertEquals(90.0, tangibleAsset.getWeight());

        // 상한 경계값 (180.0)
        tangibleAsset.setWeight(180.0);
        assertEquals(180.0, tangibleAsset.getWeight());
    }
}
