package com.survivalcoding;

import java.util.Scanner;

/**
 * 모험 게임의 진행 과정을 직접 실행해 보는 실행용 클래스.
 * <p>
 * 게임이 아니라, "게임을 조종하는 사람"의 역할이다.
 * 객체 생성 → 값 설정 → 행동 지시 순서로 게임이 진행되는 과정을 보여 준다.
 * <p>
 * [고도화] 콘솔 색상, 지연 효과 및 키보드 입력을 통한 실제 플레이 지원.
 */
public class Main {

    // ANSI 색상 코드 (고도화: 가시성 강화)
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";

    // 게임 상태 플래그 (고도화: 키보드 입력을 위한 플래그)
    private static boolean isPlaying = true;

    static void main(String[] args) {
        // 스캐너로 키보드 입력을 받기 위한 초기화 (고도화: 플레이어 컨트롤 지원)
        Scanner scanner = new Scanner(System.in);

        // 가상 세계에 용사를 생성
        Hero hero = new Hero();

        // 생성된 용사에게 최초의 HP와 이름을 설정
        // 이름은 Hero#setName 의 검증(3글자 이상)을 통과해야 하므로 3글자로 지정한다.
        hero.setName("준석이");
        hero.setHp(100);

        // 게임 소개 애니메이션 (고도화: 색상 + 지연)
        System.out.println(
                ANSI_BOLD + "=== 몬스터 아레나 배틀 ===" + ANSI_RESET
        );
        animate(500);
        System.out.println(
                "용사님의 이름은 " + ANSI_BOLD + hero.getName() + ANSI_RESET + "이고, "
                        + ANSI_GREEN + formatHP(hero.getHp(), 100) + ANSI_RESET + "입니다"
        );

        // 메인 게임 루프 (고도화: 키보드 입력 지원)
        while (isPlaying) {
            // 메뉴 출력 (고도화: 색상 적용)
            animate(300);
            System.out.println(ANSI_CYAN + "\n--- 전투 메뉴 ---" + ANSI_RESET);
            animate(200);
            System.out.println(ANSI_GREEN + "1. 공격 (a)" + ANSI_RESET);
            animate(200);
            System.out.println(ANSI_BLUE + "2. 마법 (m)" + ANSI_RESET);
            animate(200);
            System.out.println(ANSI_YELLOW + "3. 아이템 (h)" + ANSI_RESET);
            animate(200);
            System.out.println(ANSI_RED + "4. 종료 (q)" + ANSI_RESET);

            // 키보드 입력 받기 (고도화: 플레이어 컨트롤)
            System.out.print(ANSI_PURPLE + "선택하세요 (a/m/h/q): " + ANSI_RESET);
            if (scanner.hasNext()) {
                String input = scanner.next().trim().toLowerCase();

                // 입력에 따른 행동 처리 (고도화: 키보드 컨트롤)
                switch (input) {
                    case "a", "attack" -> {
                        animate(500);
                        if (hero.getHp() > 0) {
                            int damage = 5;
                            hero.setHp(hero.getHp() - damage);
                            System.out.println(ANSI_RED + "적을 공격합니다! 데미지 " + damage + ANSI_RESET);
                            if (hero.getHp() <= 0) {
                                System.out.println(ANSI_RED + "GAME OVER" + ANSI_RESET);
                                isPlaying = false;
                            }
                        }
                    }
                    case "m", "magic" -> {
                        animate(700);
                        if (hero.getHp() > 0) {
                            System.out.println(ANSI_PURPLE + "마법 시전..." + ANSI_RESET);
                            animate(500);
                            System.out.println("화염구가 몬스터에게!");
                            // 마법은 HP를 직접 감소시키지 않으나, 몬스터 체력 감소 등 게임 로직 확장 가능
                        }
                    }
                    case "h", "item" -> {
                        animate(400);
                        System.out.println(ANSI_YELLOW + "아이템을 습득했습니다!" + ANSI_RESET);
                    }
                    case "q", "quit", "exit" -> {
                        System.out.println(ANSI_RED + "게임을 종료합니다." + ANSI_RESET);
                        isPlaying = false;
                    }
                    default -> {
                        animate(200);
                        System.out.println(ANSI_RED + "잘못된 입력입니다. a/m/h/q 중 하나를 선택하세요." + ANSI_RESET);
                    }
                }
            }

            // 소규모 지연으로 CPU 과부하 방지 및 화면 플리커 방지
            animate(100);
        }

        // 게임 종료 메시지 (고도화: 색상 + 지연)
        animate(500);
        System.out.println(ANSI_BOLD + "오래도록 기억되길 바랍니다, " + hero.getName() + "!" + ANSI_RESET);
        animate(300);
        System.out.println(ANSI_GREEN + "최종 HP: " + hero.getHp() + ANSI_RESET);

        // 스캐너 리소스 해제 (고도화: 리소스 관리)
        scanner.close();
    }

    /**
     * 출력 지연 효과 (고도화: 자연스러운 흐름 연출).
     *
     * @param millis 지연 시간(밀리초)
     */
    private static void animate(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * HP를 색상으로 표시하는 헬퍼 메서드 (고도화: 비율에 따른 색상 자동 결정).
     *
     * @param current 현재 체력
     * @param max 최대 체력
     * @return 색상이 적용된 문자열
     */
    private static String formatHP(int current, int max) {
        double ratio = (double) current / max;
        String color;
        if (ratio > 0.5) {
            color = ANSI_GREEN;
        } else if (ratio > 0.2) {
            color = ANSI_YELLOW;
        } else {
            color = ANSI_RED;
        }
        return color + current + ANSI_RESET + "/" + max;
    }
}