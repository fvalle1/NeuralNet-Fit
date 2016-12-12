package me.fvalle.nnfit;

import java.awt.geom.Point2D;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartManager {

	public static XYSeriesCollection createJFreeChartDataset(Point2D[]... series){
		XYSeriesCollection dataset = new XYSeriesCollection();
		int counter=0;
		for (Point2D[] serie : series) {
			XYSeries xyserie=new XYSeries("Serie-"+counter++);
			for (Point2D point : serie) {
				xyserie.add(point.getX(),point.getY());
			}
			dataset.addSeries(xyserie);
		}
		return dataset;
	}

	public static XYSeries createJFreeChartSeries(String title, Point2D[] series){
		XYSeries xyserie=new XYSeries(title);
		for (Point2D point : series) {
			xyserie.add(point.getX(),point.getY());
		}
		return xyserie;
	}

	public static XYSeriesCollection createJFreeChartCollection(XYSeries... xyseries){
		XYSeriesCollection dataset = new XYSeriesCollection();
		for (XYSeries xy : xyseries) {
			dataset.addSeries(xy);
		}
		return dataset;
	};

	public static void plotChart(String title,XYSeriesCollection dataset){
		JFreeChart grafico = ChartFactory.createScatterPlot(
				title, 
				"x", 
				"y", 
				dataset, 
				PlotOrientation.VERTICAL, 
				true, true, false);
		ChartFrame frame = new ChartFrame("Plot",grafico);
		frame.pack();
		frame.setVisible(true);

	}

}
