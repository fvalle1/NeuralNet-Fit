package me.fvalle.nnfit;

import java.awt.geom.Point2D;

import nnwa.GenerateDataset;

public class CompareAnalysis {
	static Analysis[] analysis;
	static Point2D[] dataset;
	
	public static void printCorrelation(){
		for (Analysis item : analysis) {
			item.getCorrelation();
		}
	}
	
	
	public CompareAnalysis() {
		dataset=GenerateDataset.generateRandomErrorGaussianDataset(10, 5, 100, 25);
		analysis=new Analysis[3];
		analysis[0]=new PolynomialAnalysis(dataset,4);
		analysis[1]=new PolynomialAnalysis(dataset,6);
		analysis[2]=new NeuralNet(dataset);
	}
	
	private static void showGraph() throws Exception {
		ChartManager.plotChart("Risultati", ChartManager.createJFreeChartCollection(
				ChartManager.createJFreeChartSeries("Dati",dataset),
				ChartManager.createJFreeChartSeries(analysis[0].getName(), analysis[0].getPoint2D()),
				ChartManager.createJFreeChartSeries(analysis[1].getName(), analysis[1].getPoint2D()),
				ChartManager.createJFreeChartSeries(analysis[2].getName(), analysis[2].getPoint2D())
				));
	}
	
	public static void main(String[] args) {
		new CompareAnalysis();
		try {
			CompareAnalysis.showGraph();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CompareAnalysis.printCorrelation();
	}
	
}
