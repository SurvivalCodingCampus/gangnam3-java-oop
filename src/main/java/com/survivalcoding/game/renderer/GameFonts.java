package com.survivalcoding.game.renderer;

import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 한글 글리프를 가진 폰트를 애플리케이션에 등록한다.
 * <p>
 * JVM 기본(System) 폰트에는 한글이 포함되어 있지 않아, 이 게임의 모든 한글 문자열이
 * 네모(□)로 렌더링된다. 이 클래스는 리소스로 번들된 Noto Sans KR 을 로드해
 * {@link #font(double, boolean)} 로 접근할 수 있게 한다.
 * <p>
 * 번들 폰트를 쓸 수 없는 환경(리소스 누락)에서는 시스템 폰트로 자동 대체하므로
 * 게임이 깨지지 않는다.
 */
public final class GameFonts {

    private static final String FONT_RESOURCE = "/fonts/NotoSansKR-%s.ttf";

    /** 한글 글리프 지원 여부를 확인할 표본 문자. */
    private static final String HANGUL_SAMPLE = "가나다라마바사아자차카타파하";

    private static final String REGULAR = "Regular";
    private static final String BOLD = "Bold";

    /** 굵기별 TTF 바이트. 폰트를 매번 디스크에서 다시 읽지 않기 위해 보관한다. */
    private static final Map<String, byte[]> FONT_BYTES = new HashMap<>();

    private static final Map<String, Font> LOADED = new HashMap<>();

    /** 로드에 실패했을 때 true. 시스템 폰트로 대체된 상태다. */
    private static boolean fallbackToSystem = false;

    private GameFonts() {
    }

    /**
     * 게임에서 쓰는 모든 크기의 폰트를 미리 로드한다.
     * 씬을 만들기 전에 호출해야 한다.
     */
    public static void load() {
        font(48, true);
        font(24, false);
        font(16, true);
        font(14, false);
        font(11, false);
        font(10, true);
    }

    /**
     * 요청한 크기·굵기의 한글 폰트를 반환한다. 같은 조합은 한 번만 로드하고 재사용한다.
     *
     * @param size 글자 크기(pt)
     * @param bold 굵은 글꼴 여부
     */
    public static synchronized Font font(double size, boolean bold) {
        String weight = bold ? BOLD : REGULAR;
        String key = weight + "@" + size;
        Font cached = LOADED.get(key);
        if (cached != null) {
            return cached;
        }

        Font loaded = loadFromResource(weight, size);
        if (loaded == null) {
            if (!fallbackToSystem) {
                fallbackToSystem = true;
                System.err.println("[GameFonts] 번들 한글 폰트를 찾지 못해 시스템 폰트를 사용합니다. "
                        + "화면의 한글이 깨질 수 있습니다.");
            }
            loaded = Font.font("System", bold ? javafx.scene.text.FontWeight.BOLD
                    : javafx.scene.text.FontWeight.NORMAL, size);
        }

        LOADED.put(key, loaded);
        return loaded;
    }

    /** 번들 폰트를 로드하고 캐시에 보관한다. 실패 시 null. */
    private static Font loadFromResource(String weight, double size) {
        byte[] bytes = fontBytes(weight);
        if (bytes == null || !supportsHangul(bytes)) {
            return null;
        }
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            return Font.loadFont(in, size);
        } catch (Exception e) {
            System.err.println("[GameFonts] 폰트 로드 실패: " + weight + " (" + e.getMessage() + ")");
            return null;
        }
    }

    /** 굵기별 TTF 바이트를 읽어 캐시한다. 리소스가 없으면 null. */
    private static byte[] fontBytes(String weight) {
        byte[] cached = FONT_BYTES.get(weight);
        if (cached != null) {
            return cached;
        }
        String resource = String.format(FONT_RESOURCE, weight);
        try (InputStream in = GameFonts.class.getResourceAsStream(resource)) {
            if (in == null) {
                System.err.println("[GameFonts] 폰트 리소스를 찾을 수 없습니다: " + resource);
                return null;
            }
            byte[] bytes = in.readAllBytes();
            FONT_BYTES.put(weight, bytes);
            return bytes;
        } catch (Exception e) {
            System.err.println("[GameFonts] 폰트 읽기 실패: " + resource + " (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * 실제 TTF 바이트가 한글 글리프를 포함하는지 확인한다.
     * JavaFX Font 에는 글리프 조회 API 가 없어 AWT 로 검증한다.
     */
    private static boolean supportsHangul(byte[] fontBytes) {
        try (InputStream in = new ByteArrayInputStream(fontBytes)) {
            java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, in);
            if (awtFont.canDisplayUpTo(HANGUL_SAMPLE) != -1) {
                System.err.println("[GameFonts] '" + awtFont.getFontName() + "' 에 한글이 없습니다.");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.err.println("[GameFonts] 폰트 검증 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * 번들 폰트 사용 중인지 확인한다. 테스트와 진단용이다.
     */
    public static boolean isUsingBundledFont() {
        return !LOADED.isEmpty() && !fallbackToSystem;
    }
}