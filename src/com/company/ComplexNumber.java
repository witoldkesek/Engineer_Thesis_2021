package com.company;

public class ComplexNumber {
    double real;
    double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    double absValue(){
        return Math.sqrt(Math.pow(real,2)+Math.pow(imaginary,2));
    }
    void add(ComplexNumber c){
        this.real+=c.real;
        this.imaginary+=c.imaginary;
    }
    void substract(ComplexNumber c){
        this.real-=c.real;
        this.imaginary-=c.imaginary;
    }
    void multiply(ComplexNumber c){
        double r=this.real;
        double i=this.imaginary;
        this.real=r*c.real-1*(i*c.imaginary);
        this.imaginary=r*c.imaginary+i*c.real;
    }
    public String toString() {
        return "{" +
                real +
                " + " + imaginary +
                "i}";
    }
    void divide(ComplexNumber c){
        ComplexNumber tmp=new ComplexNumber(this.real,this.imaginary);
        tmp.multiply(new ComplexNumber(c.real,c.imaginary*-1));
        c.multiply(new ComplexNumber(c.real,c.imaginary*-1));
        tmp.real/=c.real;
        tmp.imaginary/=c.real;
        this.real= tmp.real;
        this.imaginary=tmp.imaginary;
    }

}
