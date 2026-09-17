package com.survivalcoding;//파일 java 기준 선언을 해야함 필수임.
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;//추가적인 method을 사용하기 위해서 필수적인것
import java.io.IOException;//회색은 import가 됬으나 사용하지 않았다는 뜻
public class Cleric {
    String Name="Cleric";
    int HP=10;
    int Init_HP=10;
    int Max_HP=100;
    int MP=6;
    int Init_MP=6;
    int Max_MP=67;
    int prayMp;
    int prayTime;

    void selfAid(){
        if(MP >=5){
        MP -=5;
        HP=Max_HP;
        System.out.println("HP가 최대로 회복됬습니다");}
        else{System.out.println("MP가 부족합니다");}
    }
    int pray(int prayTimeM){
      int REUSE=  (int)(Math.random()*2);
        int add_MP=prayTimeM+REUSE;
             if(add_MP+MP>=Max_MP){
                    System.out.println(Max_MP-MP+"만큼 MP가 회복되었씁니다");
                    MP=Max_MP;
                         {return Max_MP-MP;}}
             else if(prayTimeM<0){System.out.println("유효하지 않은 기도시간입니다.");
             return 67;}
            else{
                System.out.println(add_MP+"만큼 MP가 회복되었습니다");
                MP+=add_MP;
                 return add_MP;}
    }
    public static void main(String[] args) throws IOException {

        System.out.println("Hello and welcome!");
        BufferedImage p1=ImageIO.read(new File("ja.png"));
        BufferedImage p2=ImageIO.read(new File ("jb.png"));
    }
}