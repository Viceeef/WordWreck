package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;

/** Resolution-independent illustrations. No external images or network needed. */
public final class OceanArt {
    private OceanArt() {}
    public static void sea(GraphicsContext g, double w, double h) {
        g.clearRect(0,0,w,h);
        g.setFill(new LinearGradient(0,0,1,1,true,CycleMethod.NO_CYCLE,
            new Stop(0,Color.web("#174b60")),new Stop(1,Color.web("#0c2c43"))));
        g.fillRoundRect(0,0,w,h,18,18);
        g.setStroke(Color.web("#78c9cb",0.14)); g.setLineWidth(1.2);
        for(int y=15;y<h-6;y+=24) for(int x=8;x<w-25;x+=54)
            g.strokeArc(x+(y%3)*4,y,26,7,180,180,ArcType.OPEN);
    }
    public static void island(GraphicsContext g, double x, double y, double scale) {
        g.save(); g.translate(x,y); g.scale(scale,scale);
        g.setFill(Color.web("#75d3cd",0.18)); g.fillOval(-9,24,124,31);
        g.setFill(Color.web("#d5ac6d")); g.fillOval(0,25,103,20);
        g.setFill(Color.web("#edcf93")); g.fillOval(9,25,77,12);
        g.setStroke(Color.web("#9e724e")); g.setLineWidth(7);
        g.beginPath();g.moveTo(53,30);g.quadraticCurveTo(65,-1,53,-30);g.stroke();
        g.setStroke(Color.web("#50a58c"));g.setLineWidth(5);
        for(int side=-1;side<=1;side+=2) for(int i=0;i<3;i++) {
            g.beginPath();g.moveTo(53,-28);
            g.quadraticCurveTo(53+side*(18+i*5),-52+i*11,53+side*(37+i*3),-22+i*10);g.stroke();
        }
        g.restore();
    }
    public static void shark(GraphicsContext g, double x, double y, double scale) {
        g.save();g.translate(x,y);g.scale(scale,scale);
        g.setFill(Color.web("#76bbc3",0.2));g.fillOval(-4,17,76,13);
        g.setFill(Color.web("#7797a8"));
        g.fillPolygon(new double[]{13,0,3,0,16},new double[]{12,0,13,26,20},5);
        g.fillOval(9,4,51,22);
        g.fillPolygon(new double[]{28,31,44},new double[]{9,-9,10},3);
        g.setFill(Color.web("#cfdee0"));g.fillOval(23,16,34,9);
        g.setFill(Color.web("#112f41"));g.fillOval(49,10,3,3);
        g.setStroke(Color.web("#426677"));g.setLineWidth(1.3);
        for(int i=0;i<3;i++)g.strokeLine(34+i*4,12,32+i*4,18);
        g.restore();
    }
    public static void log(GraphicsContext g,double x,double y,double size) {
        g.setFill(Color.web("#65482f"));g.fillRoundRect(x,y+3,size,size,7,7);
        g.setFill(Color.web("#b58c57"));g.fillRoundRect(x,y,size,size-2,7,7);
        g.setStroke(Color.web("#e4bd7f"));g.setLineWidth(1);
        g.strokeLine(x+5,y+5,x+size-5,y+5);
        g.setStroke(Color.web("#755638"));
        g.strokeLine(x+4,y+size-8,x+size-4,y+size-8);
        g.strokeOval(x+size*.6,y+size*.35,6,3);
    }
    public static void compass(GraphicsContext g,double x,double y) {
        g.setStroke(Color.web("#e3c58b",0.32));g.setLineWidth(1);
        g.strokeOval(x-19,y-19,38,38);
        g.setFill(Color.web("#e3c58b",0.5));
        g.fillPolygon(new double[]{x,x-4,x,x+4},new double[]{y-26,y,y+26,y},4);
        g.fillPolygon(new double[]{x-26,x,x+26,x},new double[]{y,y-4,y,y+4},4);
    }
}
