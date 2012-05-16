package com.WekaStudy;

import java.io.Serializable;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;


public class TreeMaker implements Serializable {
        
        private Classifier decisionTree;
        private DataSet dataset;
        private DataMiner dataMiner;
        private Evaluation evaluation;
       
        
        public TreeMaker() {
                decisionTree = null;
                dataset = null;
                dataMiner = null;
                evaluation = null;
        }
        
        public void writeSerializedTree(String filename) {
                // initialize the dataset
                dataset = new DataSet();

                // intialize decisiontree
                dataMiner = new DataMiner();

                // read the data into a the dataSet
                dataset.setMainSet(dataMiner.readSetFromFile("ResultSet3.csv", false, 1));

                // Build trainingSet and TestSet
                dataMiner.BuildTrainingAndTestSet(dataset);

                // create classifier
                // create the classifier
                Classifier dTree = dataMiner.CreateClassifier(dataset);
                
                // evaluate the classifier
                evaluation = dataMiner.EvaluateClassifier(decisionTree, dataset);

                
                try {
                        /*
                        ObjectOutputStream out = new ObjectOutputStream(fos);
                        out.writeObject(decisionTree);
                        out.close();
                        */
                        
                        
                }
                catch (Exception e) {
                        e.printStackTrace();
                }
        }
        
        public Classifier getDecisionTree() {
                return decisionTree;
        }
}

