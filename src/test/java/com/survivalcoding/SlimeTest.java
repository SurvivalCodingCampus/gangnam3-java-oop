package com.survivalcoding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SlimeTest {
    //가드라는 것이  있다 범위로(입구컷)
    //이름위에서 alt+enter=바로 test 파일 생성
    //@Test @BeforeEach @assertEquals assetTrue() assertEquals()
    //sout enter_>system 출력 물론 메서드안이니 생성자 안에서만 된다.
    //main 바로 요약본이 나옴
    //변수 위에다가 커서 넣으면 그 커서에 대응되는 놈에 불빛이 나온다.
    //static final=대문자
    //오버라이드 덮어씌기 오버로드  생성자들이 변수 다를떄 구별 가능한거.
    Hero hero;
    PoisonSlime poisonSlime;
    Wizard wizard;
    GreatWizard GWizard;

    /*
    poison slime 체크
    wizard heal 체크 case 1 mp가 부족할떄 2 성공했을떄
    greatwizard heal 체크 superheal 체크 성공 실패 채크
    assertTrue(조건)
     */
    @BeforeEach
    void init() {
        hero = new Hero();//포인터 null 이 띈다 객체는 객체 자체가 아니라
        //this(a,b,c,d)가 된다.
        poisonSlime = new PoisonSlime();
        wizard = new Wizard();
        GWizard = new GreatWizard();
    }

    @Test
    @DisplayName("독 슬라임 공격 ")
    void poisontest() {
        int Bhero = hero.getHp();
        poisonSlime.attack(hero);
        assertTrue(hero.getHp() <= Bhero - 10);
        //실패할떄만 띄워줌
    }

    @Test
    @DisplayName("마법사 마나 테스트 마나 부족 마나 충분")
    void wizardtest() {
        wizard.setMp(4);
        wizard.heal(hero);
        System.out.println(hero.getHp());
        wizard.setMp(12);
        wizard.heal(hero);
        System.out.println(hero.getHp());

    }

    //각각 부모 부자마다 get set 변수 2개 설정하면 상위에서 get하면 위에껄 꺼낸다.
    @Test
    @DisplayName("대마법사 마나 테스트 힐 마나 X 힐 마나 O 대힐 마나 X 대힐 마나 O )")
    void Gwizardtest() {
        GWizard.setMp(4);
        GWizard.heal(hero);
        System.out.println("1번");
        GWizard.setMp(12);
        GWizard.heal(hero);
        System.out.println("1번");
        GWizard.setMp(4);
        GWizard.superHeal(hero);
        System.out.println("1번");
        GWizard.setMp(66);
        GWizard.superHeal(hero);
        System.out.println("1번");
        //모든 메서드를 테스트 하면 된다.(생성자 메서드)...와 그떄마다 각 값 확인...
        //메서드는 덮어씌워지지만 변수는 안덮어씌워짐 부무와 자식 둘다에게 존재한다.
        //this
        //this.field
        //this.method

    }


}
