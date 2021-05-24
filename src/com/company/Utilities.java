package com.company;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Utilities {
    static int n_angles= Computations.n_angles;
    public static void exportToAFile(double[] array,String name, boolean dots){
        NumberFormat nf;
        if(dots) {
            nf = NumberFormat.getNumberInstance(Locale.UK);
        }else
        {
            nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        }
        DecimalFormat df = (DecimalFormat)nf;
        df.setMaximumFractionDigits(18);
        df.setMinimumFractionDigits(18);
        try {
            PrintWriter out = new PrintWriter(name);
            for (int i = 0; i < n_angles; i++) {
                out.println(df.format(array[i]));
            }
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static double[] importFromAFile(String name){
        double[] tmp=new double[n_angles];
        Path path = Paths.get(name);
        try {for(int i=0;i<n_angles;i++) {
            tmp[i] = Double.parseDouble(Files.readAllLines(path).get(i).replaceAll(",","."));
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return tmp;
    }
}
