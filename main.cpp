//
//  main.cpp
//  高斯拟合分布
//
//  Created by 李智强 on 2021/1/24.
//  Copyright © 2021 李智强. All rights reserved.
//

#include<iostream.h>
 
#include<math.h>
 
#include<stdlib.h>
 
#include <windows.h>
 
double f(int n,double x){             //f(n,x)用来返回x的n次方
 
        double y=1.0;
 
        if(n==0)return 1.0;
 
               else{
 
               for(int i=0;i<n;i++)y*=x;
 
               return y;
 
        }
 
}
 
int xianxingfangchengzu(double **a,int n,double *b,double *p,double dt)//用高斯列主元法来求解法方程组
 
 {
 
   int i,j,k,l;
 
   double c,t;
 
   for(k=1;k<=n;k++)
 
   {
 
   c=0.0;
 
   for(i=k;i<=n;i++)
 
           if(fabs(a[i-1][k-1])>fabs(c))
 
           {
 
           c=a[i-1][k-1];
 
           l=i;
 
           }if(fabs(c)<=dt)
 
                  return(0);
 
           if(l!=k)
 
           {
 
                  for(j=k;j<=n;j++)
 
                  {
 
                          t=a[k-1][j-1];
 
                          a[k-1][j-1]=a[l-1][j-1];
 
                          a[l-1][j-1]=t;
 
                  }
 
                  t=b[k-1];
 
                  b[k-1]=b[l-1];
 
                  b[l-1]=t;
 
           }
 
                  c=1/c;
 
                  for(j=k+1;j<=n;j++)
 
                  {
 
                          a[k-1][j-1]=a[k-1][j-1]*c;
 
                          for(i=k+1;i<=n;i++)
 
                                  a[i-1][j-1]-=a[i-1][k-1]*a[k-1][j-1];
 
                  }
 
                  b[k-1]*=c;
 
                   for(i=k+1;i<=n;i++)
 
                               b[i-1]-=b[k-1]*a[i-1][k-1];
 
           }
 
           for(i=n;i>=1;i--)
 
                  for(j=i+1;j<=n;j++)
 
                          b[i-1]-=b[j-1]*a[i-1][j-1];
 
                  
 
   cout.precision(12);
 
   for(i=0;i<n;i++)p[i]=b[i];
 
}
 
double** create(int a,int b)//动态生成数组
 
{
 
        double **P=new double *[a];
 
        for(int i=0;i<b;i++)
 
               P[i]=new double[b];
 
        return P;
 
}
 
 
 
void zuixiaoerchengnihe(double x[],double y[],int n,double a[],int m)
 
{
 
        int i,j,k,l;
 
        double **A,*B;
 
        A=create(m,m);
 
        B=new double[m];
 
        for(i=0;i<m;i++)
 
               for(j=0;j<m;j++)A[i][j]=0.0;
 
               for(k=0;k<m;k++)
 
                       for(l=0;l<m;l++)
 
                               for(j=0;j<n;j++)A[k][l]+=f(k,x[j])*f(l,x[j]);//计算法方程组系数矩阵A[k][l]
 
        cout<<"法方程组的系数矩阵为:"<<endl;
 
        for(i=0;i<m;i++)
 
               for(j=0,k=1;j<m;j++,k++){
 
                       cout<<A[i][j]<<'\t';
 
                       if(k&&k%m==0)cout<<endl;
 
               }
 
    for(i=0;i<m;i++)B[i]=0.0;
 
        for(i=0;i<m;i++)
 
        for(j=0;j<n;j++)B[i]+=y[j]*f(i,x[j]);
 
        for(i=0;i<m;i++)cout<<"B["<<i<<"]="<<B[i]<<endl;//记录B[n]
 
        xianxingfangchengzu(A,m,B,a,1e-6);
 
        delete[]A;
 
        delete B;
 
}
 
double pingfangwucha(double x[],double y[],int n,double a[],int m)//计算最小二乘解的平方误差
 
{
 
        double deta,q=0.0,r=0.0;
 
        int i,j;
 
        double *B;
 
        B=new double[m];
 
        for(i=0;i<m;i++)B[i]=0.0;
 
        for(i=0;i<m;i++)
 
        for(j=0;j<n;j++)B[i]+=y[j]*f(i,x[j]);
 
        for(i=0;i<n;i++)q+=y[i]*y[i];
 
        for(j=0;j<m;j++)r+=a[j]*B[j];
 
        deta=fabs(q-r);
 
        return deta;
 
        delete B;
 
}
 
void main(void){
 
        int i,n,m;
 
        double *x,*y,*a;
 
        char ch='y';
 
        do{
 
        system("cls");
 
        cout<<"请输入所给拟合数据点的个数n=";
 
        cin>>n;
 
        cout<<"请输入所要拟合多项式的项数m=";
 
        cin>>m;
 
        while(n<=m){
 
           cout<<"你所输入的数据点无法确定拟合项数，请重新输入"<<endl;
 
           Sleep(1000);
 
           system("cls");
 
           cout<<"请输入所给拟合数据点的个数n=";
 
           cin>>n;
 
           cout<<"请输入所要拟合多项式的项数m=";
 
           cin>>m;
 
           }
 
        x=new double[n];                             //存放数据点x
 
        y=new double[n];                             //存放数据点y
 
        a=new double[m];                             //存放拟合多项式的系数
 
        cout<<"请输入所给定的"<<n<<"个数据x"<<endl;
 
        for(i=0;i<n;i++)
 
        {
 
               cout<<"x["<<i+1<<"]=";
 
               cin>>x[i];
 
        }
 
        cout<<"请输入所给定的"<<n<<"个数据y"<<endl;
 
        for(i=0;i<n;i++)
 
        {
 
               cout<<"y["<<i+1<<"]=";
 
               cin>>y[i];
 
        }
 
        zuixiaoerchengnihe(x,y,n,a,m+1);
 
    cout<<endl;
 
        cout<<"拟合多项式的系数为:"<<endl;
 
        for(i=0;i<=m;i++)cout<<"a["<<i<<"]="<<a[i]<<'\t';
 
        cout<<endl;
 
        cout<<"平方误差为："<<pingfangwucha(x,y,n,a,m+1)<<endl;
 
        delete x;   delete y;
 
        cout<<"按y继续，按其他字符退出"<<endl;
 
            cin>>ch;}
}
    
 
