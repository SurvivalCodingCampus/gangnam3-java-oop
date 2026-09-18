package com.survivalcoding;//파일 java 기준 선언을 해야함 필수임.
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
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

    void checker(){
        if(HP>=Max_HP){
            HP=Max_HP;
        }
        if(MP>=Max_MP){
            MP=Max_MP;
        }
    };
        Cleric(String name, int hp, int mp){
            this.Name=name;
            this.HP=hp;
            this.MP=mp;
            checker();
}
        Cleric(String name,int hp){
            this.Name=name;
            this.HP=hp;
            this.MP=Max_MP;
            checker();
}
        Cleric(String name){
        this.Name=name;
        this.HP=Max_HP;
        this.MP=Max_MP;
        checker();
}


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
                    int X=MP;
                    MP=Max_MP;
                         {return Max_MP-X;}}
             else if(prayTimeM<0){System.out.println("유효하지 않은 기도시간입니다.");
             return 0;}
            else{
                System.out.println(add_MP+"만큼 MP가 회복되었습니다");
                MP+=add_MP;
                 return add_MP;}
    }
    public static void main(String[] args) throws IOException {
        System.out.println("Hello and welcome!");
        JFrame X = new JFrame("name");
        X.setSize(1600,1800);
        ///연습용 후반///서브 프로젝트
        X.setLayout(new FlowLayout());
        BufferedImage P1= ImageIO.read(new File("ja.png"));
        JLabel IMGLABEL= new JLabel(new ImageIcon(P1));
        JButton addBtn= new JButton("넣기22222");
        JButton removeBtn= new JButton("삭제22222222222222");
        X.add(addBtn);
        X.add(removeBtn);
        addBtn.addActionListener(e->
        {X.add(IMGLABEL);
            X.revalidate();
            X.repaint();
        });
        removeBtn.addActionListener(e->
                {
                    X.remove(IMGLABEL);
                    X.revalidate();
                    X.repaint();

                }
        );
        X.setVisible(true);
    }
}
/*
BufferedImage B1=ImageIO.read(new File("ja.png"));
BufferedImage B1=ImageIO.read(new File("ja.png"));
BufferedImage B1=ImageIO.read(new File("ja.png"));
BufferedImage B1=ImageIO.read(new File("pa.png"));
BufferedImage B1=ImageIO.read(new File("ja.pmg"));
BufferedImage B1=ImageIO.read(new File("pa.png"));
BufferedImage B1=ImageIO.read(new File("ja.png"));
BufferedImage B1=ImageIO.read(new File("ja.png"));
BufferedImage B1=ImageIO.read(new File("ja.png"));
JLabel IL=new JLabel(new ImageIcon(B1));
JLabel IL=new JLabel(new ImageIcon(B1));
JLabel IL=new JLabel (new ImageIcon(B1));
JLabel IL=new JLabel(new ImageIcon(B1));
JLabel IL=new JLabel(new ImageIcon(B1));
JLabel IL=new JLabel(new ImageIcon(B1));
JLabel IL=new JLabel(new ImageIcon(B1));
JButton attchbth=new JButton("넣기");
JButton removebtn=new JButton("뺼까");
attchbth.addActionListener(e->
        {
         X.add(img label)
         X.revaliate();
         X.repaint();



        }
        )
        */