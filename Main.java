package com.company;

public class Main {

    public static void main(String[] args) {
        double[] jieguo = new double[]{0.5, 0.88, 0.5, 0.769, 0.667, 0.625, 0.75, 0.666, 0.5, 0.877, 0.811, 1.0, 0.872, 1.0, 0.73, 1.0, 0.936, 0.2, 0.893, 1.0, 0.87, 1.0, 0.922, 0.741, 1.0, 0.816, 0.857, 0.667, 0.846, 1.0, 0.813, 1.0, 0.799, 0.961, 0.809, 0.824, 0.851, 1.0, 0.685, 0.75, 0.858, 0.777, 0.785, 0.94, 0.522};
        int len = jieguo.length;
        double sum = 0;

        for (int i = 0; i < len; i++) {

            sum = sum + jieguo[i];
        }
        double qiwang = sum / len;
        double[] cha = new double[len];
        for (int j = 0; j < len; j++) {
            cha[j] = jieguo[j] - qiwang;
            cha[j]=cha[j]*cha[j];

        }
        double sum2=0;
        for(int k=0;k<len;k++){
            sum2=sum2+cha[k];
        }
        double fangcha=sum2/len;
        System.out.println(qiwang);
        System.out.println(fangcha);
    }
}
