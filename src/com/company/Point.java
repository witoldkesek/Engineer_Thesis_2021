package com.company;

import java.text.DecimalFormat;

public class Point {
    public double a;
    public double b;

    public Point(double a, double b) {
        if(a>1.4){
            this.a=1.4;
        }else if(a<0.4){
            this.a=0.4;
        }else {
            this.a = a;
        }
        if(b>1.4){
            this.b=1.4;
        }else if(b<0.4){
            this.b=0.4;
        }else {
            this.b = b;
        }
    }
    public Point(Point x) {
        double a=x.a;
        double b=x.b;
        if(a>1.4){
            this.a=1.4;
        }else if(a<0.4){
            this.a=0.4;
        }else {
            this.a = a;
        }
        if(b>1.4){
            this.b=1.4;
        }else if(b<0.4){
            this.b=0.4;
        }else {
            this.b = b;
        }
    }

    double distance(Point x){
        return Math.sqrt(Math.pow(this.a-x.a,2)+Math.pow(this.b-x.b,2));
    }
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#0.00000000");
        return "a: "+df.format(a) +" b: " +df.format(b);
    }

}
