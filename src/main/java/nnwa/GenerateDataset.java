package nnwa;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.Random;


public class GenerateDataset {
	
	public static Point2D[] generateRandomErrorGaussianDataset(double mean,double sigma,int sampleSize,double variationPercentage)
	{
		Random r =  new Random(System.currentTimeMillis());
		double dx=(8*sigma)/sampleSize;
		double x=mean-4*sigma;
		double d;
		double error=gaussianDistribution(mean, mean, sigma)*variationPercentage/100D;
		Point2D[] array = new Point2D[sampleSize];
		for (int i = 0; i < array.length; i++) {
			d = gaussianDistribution(x, mean, sigma);
			array[i]=new Point2D.Double(x, (d-error/2D)+error*r.nextDouble());
			x+=dx;
		}
		return array;
	}
	
	
	public static Point2D[] generateGaussianDataset(double mean,double sigma,int sampleSize)
	{
		double dx=(8*sigma)/sampleSize;
		double x=mean-4*sigma;
		Point2D[] array = new Point2D[sampleSize];
		for (int i = 0; i < array.length; i++) {
			array[i]=new Point2D.Double(x, gaussianDistribution(x, mean, sigma));
			x+=dx;
		}
		return array;
	}

	
	public static double gaussianDistribution(double x, double mean, double sigma){
		return (1D/Math.sqrt(2D*Math.PI*Math.pow(sigma, 2)))*Math.exp(-(1D/(2D*Math.pow(sigma, 2))*Math.pow((x-mean), 2)));
	}


	public static void printData(Point2D[] random2Ddata, PrintStream out) {
		System.out.println("Dataset\n\t(x,y) (x,y) ...\n\n");
		for (Point2D point : random2Ddata) {
			System.out.print("("+point.getX()+","+point.getY()+")");
		}
	}
	
	
	
	
}
