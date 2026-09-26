package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** Original 8-bit sprites: integer-grid rectangles, limited palette, no image downloads. */
public final class OceanArt {
    private OceanArt() {}
    private static final String[] PALM = {
        ".........gg....gg........", "......gggGGg..gGGgg......", "....ggGGGGGGggGGGGGg....",
        "...gGGGggGGGGGGggGGGGg..", "..gGGg...gGGGGg...gGGGg.", "..gg.....ggttgg......gg.",
        "..........ttt...........", "..........tt............", ".........ttt............",
        ".........tt.............", "........ttt.............", ".....ssssssssss.........",
        "...ssSSSSSSSSSSss.......", ".ssSSSSSSSSSSSSSSss.....", "...ssssssssssssss.......",
        "..wwwwwwwwwwwwwwwww....."};
    private static final String[] SHARK = {
        "........................d.......................",
        "........................ddd.....................",
        ".......................dddd.....................",
        ".......................dddd.....................",
        "d.....................ddddd.....................",
        ".d....................ddddd.....................",
        ".ddd..................ddddd.....................",
        "..ddd................ddddddd....................",
        "..dddd...............dddddddd...................",
        "...ddddd.......ddddddddLddddddd.................",
        "...ddddddddddddddLLLLLLLLLLLLLLLdddddddd........",
        "....dddddLLLLLLLLLLLLLLLLLLLLLLLLLLLdddddd......",
        "....ddddddLLLLLLLLLLLLLLLLLLLLdLdLdLLLkkLddd....",
        "....dddddddLLLLLLLLLLLLLLLLLLLdLdLdLLLkkLLLddd..",
        "...ddddddddLLLLLLLLLLLLLLLLLLdLdLdLLLLLLLLLLLddd",
        "...dddddddddLLLLLLLLLLLdLLLLLdLdLdLLLLLLLLkkkdd.",
        "..ddddd...dddddBBBBBBBBddddddddddBBBBBBBkkdddd..",
        "..dddd.......ddddBBBBBBdddddddddBBBBBBBBddddd...",
        ".ddd...........dddBBBBBddddddddBBBBBBBBddddd....",
        ".dd.................dddddddddddddddddddd........",
        "d......................ddddddddddddd............",
        ".......................dddddd...................",
        ".......................ddddd....................",
        ".......................d........................"};
    private static void block(GraphicsContext g,String color,double x,double y,double w,double h) {
        g.setFill(Color.web(color));g.fillRect(Math.rint(x),Math.rint(y),Math.ceil(w),Math.ceil(h));
    }
    private static void sprite(GraphicsContext g,String[] rows,double x,double y,int pixel) {
        for(int row=0;row<rows.length;row++)for(int col=0;col<rows[row].length();col++) {
            char c=rows[row].charAt(col);String color;
            switch(c) {
                case 'g':color="#166758";break;case 'G':color="#6cbe68";break;
                case 't':color="#a65d3b";break;case 's':color="#c18b49";break;case 'S':color="#f2d48d";break;
                case 'd':color="#263750";break;case 'L':color="#7eafbe";break;case 'B':color="#d8e9df";break;
                case 'k':color="#111b35";break;case 'w':color="#287da0";break;default:continue;
            }
            block(g,color,x+col*pixel,y+row*pixel,pixel,pixel);
        }
    }
    public static void sea(GraphicsContext g,double w,double h) { sea(g,w,h,0); }
    public static void sea(GraphicsContext g,double w,double h,int phase) {
        g.clearRect(0,0,w,h);
        block(g,"#122a51",0,0,w,h);
        block(g,"#173d68",0,0,w,h*.36);
        block(g,"#16486f",0,h*.36,w,h*.32);
        for(int y=12;y<h-8;y+=24)for(int x=8;x<w-22;x+=48) {
            int shift=((y/24+phase)%3)*4;
            block(g,"#245d85",x+shift,y,16,2);block(g,"#3481a0",x+shift+4,y+2,16,2);
            block(g,"#133556",x+shift+10,y+7,8,2);
        }
    }
    public static void island(GraphicsContext g,double x,double y,double scale) {
        sprite(g,PALM,x,y-34,Math.max(1,(int)Math.round(scale*3)));
    }
    public static void shark(GraphicsContext g,double x,double y,double scale) {
        shark(g,x,y,scale,0);
    }
    public static void shark(GraphicsContext g,double x,double y,double scale,int phase) {
        int pixel=Math.max(1,(int)Math.round(scale*2));
        String[] frame=SHARK;
        if ((phase/3)%2==1) {
            frame=new String[SHARK.length];
            for(int row=0;row<SHARK.length;row++) {
                StringBuilder line=new StringBuilder(SHARK[row]);
                for(int col=0;col<8;col++)
                    line.setCharAt(col,row>0 ? SHARK[row-1].charAt(col) : '.');
                frame[row]=line.toString();
            }
        }
        sprite(g,frame,x,y-6,pixel);
    }
    public static void wake(GraphicsContext g,double x,double y,int phase) {
        int travel=(phase%12)*2;
        for(int i=0;i<3;i++) {
            double bx=x-i*13-travel, by=y+(i%2)*5;
            block(g,"#59a6b5",bx,by,6,2);
            block(g,"#a8dad1",bx+1,by-2,2,2);
        }
    }
    public static void log(GraphicsContext g,double x,double y,double size) {
        block(g,"#49313c",x-2,y+2,size+4,size+2);
        block(g,"#a46643",x,y,size,size);
        block(g,"#d39a5b",x+2,y+2,size-4,5);
        block(g,"#eac487",x+4,y+3,size-8,2);
        block(g,"#754638",x+3,y+size-6,size-6,3);
        block(g,"#754638",x+size/2,y+size/2,7,2);
        block(g,"#f0d497",x+3,y+8,2,size-17);
        block(g,"#f0d497",x+size-5,y+8,2,size-17);
    }
    public static void compass(GraphicsContext g,double x,double y) {
        block(g,"#283759",x-17,y-17,34,34);
        block(g,"#ddb374",x-2,y-21,4,42);block(g,"#ddb374",x-21,y-2,42,4);
        block(g,"#f7e4b0",x-5,y-10,10,20);block(g,"#f7e4b0",x-10,y-5,20,10);
        block(g,"#c85d61",x-2,y-15,4,15);block(g,"#101d35",x-2,y-2,4,4);
    }
    public static void sky(GraphicsContext g,double w,double h) {
        block(g,"#332851",0,0,w,h);block(g,"#705074",0,h*.55,w,h*.45);
        double x=w*.76,y=h*.5;
        block(g,"#d47669",x-26,y-18,52,36);block(g,"#f5c07b",x-20,y-24,40,44);
        block(g,"#ffe0a0",x-14,y-26,28,36);
        for(int i=0;i<8;i++) {
            double sx=30+i*91,sy=12+(i%3)*11;
            block(g,"#e9dcc4",sx,sy,2,6);block(g,"#e9dcc4",sx-2,sy+2,6,2);
        }
        block(g,"#dab4b0",30,h*.65,70,6);block(g,"#dab4b0",42,h*.65-6,46,6);
    }
    public static void lighthouse(GraphicsContext g,double x,double y) {
        block(g,"#364b62",x-13,y+47,84,15);
        block(g,"#b79765",x-5,y+44,65,9);
        block(g,"#efe0b2",x+15,y,27,49);
        block(g,"#b95d62",x+15,y+10,27,9);
        block(g,"#b95d62",x+15,y+29,27,9);
        block(g,"#26344b",x+9,y-6,39,6);
        block(g,"#f8c56a",x+18,y-18,22,12);
        block(g,"#26344b",x+13,y-22,31,5);
        block(g,"#d1846e",x+19,y-28,19,6);
        block(g,"#25354c",x+25,y+38,8,11);
    }
    public static void sailboat(GraphicsContext g,double x,double y) {
        block(g,"#aa704f",x,y+39,59,7);
        block(g,"#734647",x+7,y+46,45,5);
        block(g,"#e3c28a",x+27,y,3,42);
        for(int row=0;row<8;row++) {
            block(g,"#f4e4bc",x+3+row*3,y+32-row*4,24-row*3,4);
            block(g,"#d0b896",x+32,y+row*4,4+row*2,4);
        }
    }
    public static void horizon(GraphicsContext g,double w,double y) {
        for(int i=0;i<9;i++) {
            double x=i*139;
            block(g,"#4a5572",x,y-8-(i%3)*5,108,20+(i%3)*5);
            block(g,"#4a5572",x+17,y-18-(i%3)*5,64,16);
        }
        block(g,"#627588",0,y+7,w,3);
    }

}
