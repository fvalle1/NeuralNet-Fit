package me.fvalle.nnfit;

import java.awt.geom.Point2D;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;

public class PolynomialAnalysis implements Analysis {
	private Point2D[] dataset;
	private Point2D[] result;
	int grade;
	
	public PolynomialAnalysis(Point2D[] dataset, int grade) {
		setDataset(dataset);
		this.grade=grade;
	}
	
	public void Analyze(){
		WeightedObservedPoints points=new WeightedObservedPoints();
		for (int i=0;i<dataset.length;i++) {
			points.add(dataset[i].getX(), dataset[i].getY());
		}
		PolynomialCurveFitter fitter=PolynomialCurveFitter.create(grade);
		double[] params=fitter.fit(points.toList());
		
		result=new Point2D[dataset.length];
		for(int i=0;i<dataset.length;i++){
			double x=dataset[i].getX();
			double xwithgrade=1;
			double y=0;
			for (double coeff : params) {
				y+=coeff*xwithgrade;
				xwithgrade*=x;
			}
			result[i]=new Point2D.Double(x, y);
		}
	}

	
	@Override
	public Point2D[] getPoint2D() throws Exception {
		Analyze();
		return result;
	}

	@Override
	public void setDataset(Point2D[] dataset) {
		this.dataset=dataset;

	}

	@Override
	public double getCorrelation() {
		double[] data=new double[dataset.length];
		double[] res=new double[dataset.length];
		for (int i=0;i<dataset.length;i++) {
			data[i]=dataset[i].getY();
			res[i]=result[i].getY();
		}
		double correlation=new Covariance().covariance(StatUtils.normalize(data), StatUtils.normalize(res));
		System.out.println("Polinomio di grado "+grade+", correlazione: "+correlation);
		return correlation;
	}

	@Override
	public String getName() {
		return "Polinomio grado "+grade;
	}
}
