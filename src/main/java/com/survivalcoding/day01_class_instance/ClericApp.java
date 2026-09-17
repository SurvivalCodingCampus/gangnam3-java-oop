package com.survivalcoding.day01_class_instance;

import java.util.Random;
import java.util.Scanner;

public class ClericApp {
    // ANSI 컬러 코드 상수
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        printHeader("FINAL FANTASY CLERIC ADVENTURE : 슬라임 사냥");
        System.out.print(CYAN + "생성할 성직자의 이름을 입력하세요: " + RESET);
        String name = scanner.nextLine();

        Cleric cleric = new Cleric(name, 50, 10);
        printStatus(cleric);

        boolean isRunning = true;
        while (isRunning) {
            System.out.println(YELLOW + "\n━━━━━━━━━━━━━━━ [ COMMAND MENU ] ━━━━━━━━━━━━━━━" + RESET);
            System.out.println(" 1. 상태 확인 (Status)");
            System.out.println(" 2. 셀프 에이드 [HP 풀회복 / MP -5] (Self-Aid)");
            System.out.println(" 3. 기도하기 [MP 회복] (Pray)");
            System.out.println(" 4. 야생의 슬라임과 전투 시작! (Battle Slime)");
            System.out.println(" 5. 모험 포기 및 종료 (Exit)");
            System.out.print(CYAN + "명령 번호를 선택하세요 ➔ " + RESET);

            if (!scanner.hasNextInt()) {
                System.out.println(RED + "숫자를 입력해주세요!" + RESET);
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    printStatus(cleric);
                    break;

                case 2:
                    System.out.println(PURPLE + "\n성스러운 빛이여, 스스로를 치유하소서..." + RESET);
                    if (cleric.selfAid()) {
                        System.out.println(GREEN + "셀프 에이드 성공! HP가 완전히 회복되었습니다." + RESET);
                    } else {
                        System.out.println(RED + "실패: 마나(MP)가 부족합니다! (필요: 5)" + RESET);
                    }
                    printStatus(cleric);
                    break;

                case 3:
                    System.out.print(CYAN + "기도할 시간(초)을 입력하세요: " + RESET);
                    int sec = scanner.nextInt();
                    int recovered = cleric.pray(sec);
                    System.out.println(YELLOW + sec + "초 동안 기도하여 " + recovered + "의 MP를 회복했습니다!" + RESET);
                    printStatus(cleric);
                    break;

                case 4:
                    // 슬라임 전투 시나리오 진입
                    startSlimeBattle(scanner, cleric, random);
                    if (!cleric.isAlive()) {
                        System.out.println(RED + "\n게임 오버 되었습니다. 새로운 성직자로 다시 도전하세요!" + RESET);
                        isRunning = false;
                    }
                    break;

                case 5:
                    System.out.println(PURPLE + "\n 모험을 종료합니다. 안녕히 가세요!" + RESET);
                    isRunning = false;
                    break;

                default:
                    System.out.println(RED + "존재하지 않는 명령입니다. (1~5 선택)" + RESET);
            }
        }
        scanner.close();
    }

    // 슬라임 전투 시나리오 메서드
    private static void startSlimeBattle(Scanner scanner, Cleric cleric, Random random) {
        int slimeHp = 25;
        int slimeMaxHp = 25;
        System.out.println(RED + "\n둔탁한 소리와 함께 [야생의 푸른 슬라임]이 나타났습니다!" + RESET);

        while (slimeHp > 0 && cleric.isAlive()) {
            System.out.println(YELLOW + "\n---------------- [ 전투 턴 ] ----------------" + RESET);
            System.out.println(" 슬라임 HP: " + slimeHp + "/" + slimeMaxHp);
            System.out.println(" 내 현재 HP: " + cleric.getHp() + "/" + cleric.getMaxHp() + " | MP: " + cleric.getMp() + "/" + cleric.getMaxMp());
            System.out.println("----------------------------------------------");
            System.out.println(" 1. 물리 공격 (메이스 휘두르기)");
            System.out.println(" 2. 셀프 에이드 (HP 회복)");
            System.out.println(" 3. 기도하기 (MP 회복)");
            System.out.println(" 4. 도망치기");
            System.out.print(CYAN + "전투 행동 선택 ➔ " + RESET);

            int battleChoice = scanner.nextInt();

            if (battleChoice == 4) {
                System.out.println(PURPLE + "무사히 슬라임에게서 도망쳤습니다!" + RESET);
                return;
            }

            // 플레이어 행동 처리
            switch (battleChoice) {
                case 1:
                    int damageToSlime = random.nextInt(6) + 5; // 5 ~ 10 데미지
                    slimeHp = Math.max(0, slimeHp - damageToSlime);
                    System.out.println(GREEN + "메이스로 슬라임을 내려쳐 " + damageToSlime + "의 피해를 입혔습니다!" + RESET);
                    break;

                case 2:
                    if (cleric.selfAid()) {
                        System.out.println(GREEN + "셀프 에이드 발동! HP가 가득 찼습니다." + RESET);
                    } else {
                        System.out.println(RED + "MP가 부족해서 셀프 에이드 실패!" + RESET);
                    }
                    break;

                case 3:
                    System.out.print(CYAN + "전투 중 기도할 시간(초) 입력: " + RESET);
                    int sec = scanner.nextInt();
                    int healed = cleric.pray(sec);
                    System.out.println(YELLOW + " 급하게 " + sec + "초간 기도하여 " + healed + " MP 회복!" + RESET);
                    break;

                default:
                    System.out.println(RED + "잘못된 행동으로 턴을 놓쳤습니다!" + RESET);
            }

            // 슬라임이 살아있다면 반격
            if (slimeHp > 0) {
                int slimeDamage = random.nextInt(5) + 3; // 3 ~ 7 데미지
                cleric.takeDamage(slimeDamage);
                System.out.println(RED + "슬라임이 산성 체액을 뱉어 " + slimeDamage + "의 데미지를 입혔습니다!" + RESET);
            }
        }

        // 전투 결과 판정
        if (slimeHp <= 0) {
            System.out.println(GREEN + "\n[승리] 슬라임을 처치했습니다! 경험치를 획득했습니다." + RESET);
        }
    }

    private static void printStatus(Cleric cleric) {
        System.out.println(YELLOW + "\n═════════════ [ " + cleric.getName() + "의 스테이터스 ] ═════════════" + RESET);
        System.out.println(" HP: " + getProgressBar(cleric.getHp(), cleric.getMaxHp(), RED) + " " + cleric.getHp() + "/" + cleric.getMaxHp());
        System.out.println(" MP: " + getProgressBar(cleric.getMp(), cleric.getMaxMp(), CYAN) + " " + cleric.getMp() + "/" + cleric.getMaxMp());
        System.out.println(YELLOW + "═════════════════════════════════════════════════" + RESET);
    }

    private static String getProgressBar(int current, int max, String color) {
        int totalBlocks = 10;
        int filledBlocks = (int) Math.round((double) current / max * totalBlocks);
        StringBuilder bar = new StringBuilder(color + "[");
        for (int i = 0; i < totalBlocks; i++) {
            if (i < filledBlocks) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]").append(RESET);
        return bar.toString();
    }

    private static void printHeader(String title) {
        System.out.println(PURPLE + "================================================");
        System.out.println("  " + title);
        System.out.println("================================================" + RESET);
    }
}