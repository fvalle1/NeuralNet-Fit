package me.fvalle.nnfit;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class NeuralNet implements Analysis{
	private double[] xOut;
	private double[] yOut;
	private Point2D[] dataset;

	@Override
	public void Analyze(){
		//Random number generator seed, for reproducability
		final long seed = System.currentTimeMillis();
		//Number of iterations per minibatch
		final int iterations = 1;
		//Number of epochs (full passes of the data)
		final int nEpochs = 2000;
		//Number of data points
		final int nSamples = dataset.length;
		//Batch size: i.e., each epoch has nSamples/batchSize parameter updates
		final int batchSize = 10;
		//Network learning rate (how much changes the error)
		final double learningRate = 0.001;
		final int numInputs = 1;
		final int numOutputs = 1;
		int numHiddenNodes=50;
		//Shows Output
		boolean listener=true;
		Random random=new Random(seed);
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				.seed(seed)
				.iterations(iterations)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT) //how to evaluate the error
				.learningRate(learningRate)
				.weightInit(WeightInit.XAVIER)
				.updater(Updater.NESTEROVS).momentum(0.9)
				.list()
				.layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes)
						.activation("identity")
						.build())
				.layer(1, new DenseLayer.Builder().nIn(numHiddenNodes).nOut(numHiddenNodes)
						.activation("tanh")
						.build())
				.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)	//MeanSquare set (Y-yobs)^2 minimum
						.activation("identity")
						.nIn(numHiddenNodes).nOut(numOutputs).build())
				.pretrain(false).backprop(true).build();

		MultiLayerNetwork net=new  MultiLayerNetwork(conf);
		net.init();
		if(listener) net.setListeners(new ScoreIterationListener(500));
				
		//final INDArray x = Nd4j.linspace(-5,5,nSamples).reshape(nSamples, 1);
		final double[] xd=new double[dataset.length];
		for(int i = 0; i < nSamples; i++) {
			xd[i]=dataset[i].getX();
		}
		INDArray x= Nd4j.create(xd, new int[]{xd.length, 1});  //Column vector
		DataSetIterator iterator=getDataset(x,random,batchSize);


		//Train the network on the full data set, and evaluate in periodically
		System.out.println("Train");
		for( int i=0; i<nEpochs; i++ ){
			iterator.reset();
			net.fit(iterator);
		}

		yOut=net.output(x).data().asDouble();
		xOut=x.data().asDouble();

	}

	private DataSetIterator getDataset(INDArray x, Random random, final int batchSize) {
		//Generate the training data
		final double[] xd = x.data().asDouble();
		final double[] yd = new double[xd.length];
		for (int i = 0; i < dataset.length; i++) {
			yd[i]=dataset[i].getY();
		}
		INDArray y= Nd4j.create(yd, new int[]{xd.length, 1});  //Column vector	
		DataSet dataset =new DataSet(x,y);

		final List<DataSet> list = dataset.asList();
		Collections.shuffle(list,random);
		DataSetIterator iterator= new ListDataSetIterator(list,batchSize);
		return iterator;
	}


	@Override
	public Point2D[] getPoint2D() throws Exception {
		Analyze();
		Point2D[] NNresult=new Point2D[xOut.length];
		for(int i=0;i<xOut.length;i++){
			NNresult[i]=new Point2D.Double(xOut[i], yOut[i]);
		}
		return NNresult;
	}

	public double getCorrelation(){
		double[] yData=new double[dataset.length];
		for(int i=0;i<dataset.length;i++){
			yData[i]=dataset[i].getY();
		}
		double correlation=new Covariance().covariance(StatUtils.normalize(yData),StatUtils.normalize(yOut));
		System.out.println("Rete Neurale, correlazione: "+correlation);
		return correlation;
	}


	@Override
	public void setDataset(Point2D[] dataset) {
		this.dataset=dataset;
	}

	@Override
	public String getName() {
		return "Rete Neurale";
	}

	NeuralNet(Point2D[] dataset){
		setDataset(dataset);
	}
}
