package com.itheima.reggie.common;

import static java.lang.Math.*;

public class langman {


    public static void main(String[] args) {
        heart2();
    }

    public static  void heart2(){
        double x,y,a;
        char s[]=new char[]{'q',' ','子','金','危','晔',' ','叶','危','金','子'};
        int index=0;
        for(y=1.3f;y>-1.1f;y-=0.06f){
            index=0;
            for(x=-1.1f;x<=1.1f;x+=0.025f){
                double result=x*x+pow((5.0*y/4.0-sqrt(abs(x))),2);
                if(result<=1){
                    System.out.print((s[index]));
                    index=(index+1)%11;
                }
                else{
                    System.out.print(' ');
                }
            }
            System.out.println(" ");
        }
    }
}
