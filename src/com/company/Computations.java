package com.company;
public class Computations {
    static double planck=6.6260755; //6.62*Math.pow(10,-34); // stała Plancka
    static double qe =1.60217733;   // 1.60217662*Math.pow(10,-19);
    static double m0=9.1093897;     //9.109*Math.pow(10,-31);
    static double vdist = 5.65/4;   //5,65/4 Ǻ - grubość jednej komórki
    static double U=10000;          //eV siła działa elektronowego
    static double c2=299792458; //prędkość światła

    static int n_atomic_planes=101; //ilość wartsw atomowych
    static int n_angles=1000;       //ilość podzielnych kątów
    static int utits_per_cell=800; //ilość cienkich warstw w jednej komórce
    static int n_slices=110*utits_per_cell/4;

    //współczynniki dotyczące odległości międzywarstwowych
    static double zt=0*vdist;
    static double zb=-110*vdist;
    static double zstep=(zt-zb)/n_slices;

    //współczynniki
    static double Bga=2;
    static double Bas=2;
    static double[] a_Ga ={0.2135,0.9768,1.6669,2.5662,1.6790};
    static double[] b_Ga ={00.1020+Bga,01.0219+Bga,04.6275+Bga,22.8742+Bga,80.1535+Bga};
    static double[] a_As ={0.2059,0.9518,1.6372,3.0490,1.4756};
    static double[] b_As ={00.0926+Bas,00.9182+Bas,04.3291+Bas,19.2996+Bas,58.9329+Bas};

    static double YRe=0.85;
    static double Ylm=0.2;

    //Funkcja obliczająca k dla podanej tety
    public static double kForTheta(double teta){
        //double K2=((2*m0*qe*2*Math.PI*2*Math.PI)/2*(planck*planck))*0.01*(1+U/(2*c))*U;
        double K22=(1+(qe*U)/(2*m0*c2*c2))*2*m0/planck/planck*qe*U;
        return Math.sqrt(K22)*Math.sin(teta/1.5834);
    }
    public static ComplexNumber []v(double a,double b){

        double []zpl=new double[n_atomic_planes];
        for(int i=0;i<n_atomic_planes;i++)
        {

            zpl[i]=-(i+5)*vdist;
            if(i==n_atomic_planes-2)
                zpl[i]=-(i+4)*vdist-vdist*a;
            if(i==n_atomic_planes-1)
                zpl[i]=zpl[i-1]-vdist*b;
        }

        ComplexNumber[] tmpv=new ComplexNumber[n_slices];
        double []a_tmp=new double[5];
        double []b_tmp=new double[5];
        double clatt=5.65;
        double omega=Math.abs(Math.pow(clatt,2)/2);


        for(int i=0;i<5;i++){
            //a_Ga[i]*=(1+U/c);
            a_Ga[i]*=(1+qe*U/(2*m0*c2*c2));
            //a_As[i]*=(1+U/c);
            a_As[i]*=(1+qe*U/(2*m0*c2*c2));
        }
        for(int k=0;k<n_slices;k++){
            double z_current=zb+(k+1)*zstep;
            double v_current=0;
            for(int j=0;j<n_atomic_planes;j++){
                double z_diff=(z_current-zpl[j])*(z_current-zpl[j]);
                for(int l=0;l<5;l++) {
                    if (j % 2 == 0) {
                        a_tmp[l] = a_As[l];
                        b_tmp[l] = b_As[l];
                    } else {
                        a_tmp[l] = a_Ga[l];
                        b_tmp[l] = b_Ga[l];
                    }
                    double suma_a_b_1 = ((-4 * Math.PI * Math.PI) / b_tmp[l]) * z_diff;
                    if (suma_a_b_1 < -15)
                        continue;
                    double suma_a_b_2 = (a_tmp[l] / Math.sqrt(b_tmp[l])) * Math.exp(suma_a_b_1);
                    v_current += suma_a_b_2;
                }
            }
            double conf=-8*Math.PI*Math.sqrt(Math.PI)/omega;
            v_current*=conf;
            ComplexNumber tmp=new ComplexNumber(YRe*v_current,Ylm*v_current);
            tmpv[k]=tmp;
        }
        a_Ga =new double[]{0.2135,0.9768,1.6669,2.5662,1.6790};
        a_As =new double[]{0.2059,0.9518,1.6372,3.0490,1.4756};
        return tmpv;
    }
    public static double []intensity(double a,double b){

        double h = zstep; //grubość jednej warstwy, na które zdyskredytowaliśmy obszar

        double angles_start=0; //początek kąta
        double angles_end=10; //koniec kąta
        double []tmpIntensity=new double[n_angles];
            ComplexNumber []vz=v(a,b);
            for(int j=0;j<n_angles;j++) {

                double angles_current=j*(angles_end-angles_start)/(n_angles);
                double k = Computations.kForTheta(angles_current/180*Math.PI);

                if(k*k<0.01){
                    tmpIntensity[j]=-1;
                }else {
                    ComplexNumber r_prev=new ComplexNumber(0,0);
                    r_prev.imaginary=0;
                    r_prev.real=0;

                    for (int g = 0; g < n_slices; g++) {
                        ComplexNumber top1 = new ComplexNumber(0, k);

                        ComplexNumber top2 = new ComplexNumber(vz[g].imaginary / (2 * k), -vz[g].real / (2 * k));

                        ComplexNumber top3 = new ComplexNumber(1 + h * (top1.real + top2.real), h * (top1.imaginary + top2.imaginary));

                        ComplexNumber top4 = new ComplexNumber((-k * k + vz[g].real) * h * h * 0.5, 0.5 * h * h * vz[g].imaginary);

                        top3.add(top4);

                        ComplexNumber top_result = new ComplexNumber(r_prev.real,r_prev.imaginary);

                        top_result.multiply(top3);

                        top_result.add(new ComplexNumber(h * top2.real, h * top2.imaginary));

                        ComplexNumber bot_result = new ComplexNumber(1 - h * (top1.real + top2.real), 0 - h * (top1.imaginary + top2.imaginary));

                        ComplexNumber bot2 = new ComplexNumber(r_prev.real * top2.real - r_prev.imaginary * top2.imaginary, r_prev.imaginary * top2.real + r_prev.real * top2.imaginary);

                        bot_result.substract(new ComplexNumber(h * bot2.real, h * bot2.imaginary));

                        bot_result.add(top4);

                        top_result.divide(bot_result);

                        r_prev = top_result;

                    }
                    tmpIntensity[j]=Math.pow(r_prev.absValue(),2);

                }
            }
            return tmpIntensity;
    }
    public static double findDiffFunction(Point a, double[]toCompare){
        double []tmpIntensity=intensity(a.a,a.b);
        double sumdiff=0;
        for(int i=0;i<n_angles;i++){

                sumdiff+=Math.abs(toCompare[i]-tmpIntensity[i]);
        }
        sumdiff/=n_angles;
        return sumdiff;
    }
    public static Point findDistances (double[] sampleIntensity) {
        double beta=2; //expansion
        double gamma=0.5; //contraction
        double delta=0.5; //shrinkage
        double tolerance=0.00001;

        //Searching for the best point in discredited to (p+1)^2 areas between 0.4 and 1.4
        double start=0.4;
        double end=1.4;
        int p=50;
        double dp=(end-start)/p;
        Point best=new Point(start,start);
        double best_value=findDiffFunction(best,sampleIntensity);
        for (int i = 0; i <=p; i++) {
            for (int j = 0; j <=p; j++) {
                double diff = findDiffFunction(new Point(start + i * dp, start + j * dp), sampleIntensity);
                if (diff < best_value) {
                    best_value = diff;
                    best = new Point(new Point(start + i * dp, start + j * dp));
                }
            }
            System.out.println("Szukanie punktu startowego: "+(((double)i/(double)p)*100)+"% ");
            System.out.flush();
        }
        if(best_value<1E-18){
            return best;
        }
        Point []points;
        if(best.a+0.1>1.4){
            points= new Point[]{best, new Point(best.a, best.b + 0.1), new Point(best.a - 0.1, best.b)};
        }else if(best.b+0.1>1.4){
            points= new Point[]{best,new Point(best.a,best.b-0.1),new Point(best.a+0.1,best.b)};
        }else {
            points= new Point[]{best, new Point(best.a, best.b + 0.1), new Point(best.a + 0.1, best.b)};
        }
        int l=0,h=0,m=0;
        System.out.println("Znaleziono punkt startowy. Szukanie najlepszego rozwiązania.");
        while(points[0].distance(points[1])>tolerance && points[1].distance(points[2])>tolerance && points[2].distance(points[0])>tolerance){
            //System.out.println(points[l]);
            l=0;
            h=0;
            m=0;
            for(int i=1;i<3;i++){
                if(findDiffFunction(points[i],sampleIntensity)<findDiffFunction(points[l],sampleIntensity))
                    l=i;
                if(findDiffFunction(points[i],sampleIntensity)>findDiffFunction(points[h],sampleIntensity))
                    h=i;
            }
            for(int i=0;i<3;i++){
                if(i!=l && i!=h)
                    m=i;
            }
            //Centroid
            double suma=0;
            double sumb=0;
            for(int i=0;i<3;i++){
                if(i!=h){
                    suma+=points[i].a;
                    sumb+=points[i].b;
                }
            }
            Point Pc=new Point((suma/2),(sumb/2));
            //Reflection
            Point Pr=new Point(2*Pc.a-points[h].a,2*Pc.b-points[h].b);
            if(findDiffFunction(points[l],sampleIntensity)<=findDiffFunction(Pr,sampleIntensity) && findDiffFunction(Pr,sampleIntensity)<findDiffFunction(points[m],sampleIntensity)){
                points[h]=new Point(Pr);
                continue;

            }
            //Expansion
            if(findDiffFunction(Pr,sampleIntensity)<findDiffFunction(points[l],sampleIntensity)){
                Point Pe=new Point(Pc.a+beta*(Pr.a-Pc.a),Pc.b+beta*(Pr.b-Pc.b));
                if(findDiffFunction(Pe,sampleIntensity)<findDiffFunction(Pr,sampleIntensity)){
                    points[h]=new Point(Pe);
                    continue;
                }
            }
            //Outside Contraction
            if(findDiffFunction(Pr,sampleIntensity)<=findDiffFunction(points[h],sampleIntensity)){
                Point Pcon=new Point(Pc.a+gamma*(Pr.a-Pc.a),Pc.b+gamma*(Pr.b-Pc.b));
                if(findDiffFunction(Pcon,sampleIntensity)<=findDiffFunction(Pr,sampleIntensity)){
                    points[h]=new Point(Pcon);
                    continue;
                }
                //Inside Contraction
            }else{
                Point Pcon=new Point(Pc.a-gamma*(Pr.a-Pc.a),Pc.b-gamma*(Pr.b-Pc.b));
                if(findDiffFunction(Pcon,sampleIntensity)<=findDiffFunction(Pr,sampleIntensity)){
                    points[h]=new Point(Pcon);
                    continue;
                }
            }
            //Shrinkage
            for(int i=0;i<3;i++){
                if(i!=l){
                    Point tmp=new Point(points[i]);
                    points[i]=new Point(points[l].a+delta*(tmp.a-points[l].a),points[l].b+delta*(tmp.b-points[l].b));
                }
            }
        }
        return points[l];
    }
}
