package me.fvalle.nnfit;

import java.awt.geom.Point2D;

public interface Analysis {
	public Point2D[] getPoint2D() throws Exception;
	public void setDataset(Point2D[] dataset);
	public double getCorrelation();
	public void Analyze();
	public String getName();
}
