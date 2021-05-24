package com.company;

import java.io.IOException;
import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        if(args.length>0){
            if(args[0].equals("1") && args.length==4){
                double []sampleIntensity= Computations.intensity(Double.parseDouble(args[1]),Double.parseDouble(args[2]));
                Utilities.exportToAFile(sampleIntensity,args[3],true);
                System.out.print("Zapisano wartosci natezenia wiazki odbiciowej elektronow dla odlegosci stanowiacych "+Double.parseDouble(args[1])+" i "+Double.parseDouble(args[2])+" standardowych miedzy dwoma ostatnimi warstwami do pliku "+args[3]+".");
            }else if(args[0].equals("2") && args.length==2){
                long startTime=System.currentTimeMillis();
                double [] readIntensity=Utilities.importFromAFile(args[1]);
                Point x= Computations.findDistances(readIntensity);
                long elapsedTime=System.currentTimeMillis()-startTime;
                DecimalFormat df = new DecimalFormat("#0.0000000");
                System.out.println("Odlegosci miedzy warstwami wynosza "+df.format(x.a)+" oraz "+df.format(x.b)+".");
                System.out.println("Czas poszukiwan: "+(elapsedTime/1000)+"s.");
            }else{
                System.out.println("Zle argumenty.");
                System.out.println("Poprawne uzycie: ");
                System.out.println("Argumenty dla wyznaczenia natezenia promenia odbitego: ");
                System.out.println("1 przedostatnia_odlegosc_miedzywarstwowa ostatnia_odleglosc_miedzywarstwowa nazwa_pliku.txt");
                System.out.println("Argumenty dla wyznaczenia odlegosci miedzywarstwowych na podstawie natezen: ");
                System.out.println("2 nazwa_pliku.txt");
            }
        }else{
            System.out.println("Niewlasciwa ilosc argumentow.");
        }

    }
}
