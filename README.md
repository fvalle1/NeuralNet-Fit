#Fit data with Neural Net


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
																											    .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.MSE) //MeanSquare set (Y-yobs)^2 minimum
																											    	      	  						      		       .activation("identity")
																																					            .nIn(numHiddenNodes).nOut(numOutputs).build())
																																						       .pretrain(false).backprop(true).build();

																																						        MultiLayerNetwork net=new  MultiLayerNetwork(conf);
																																									  net.init();