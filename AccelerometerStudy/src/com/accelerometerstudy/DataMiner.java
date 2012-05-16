package com.accelerometerstudy;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class DataMiner implements Serializable {

        Instances isTrainingSet = null;
        ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
        private FastVector featureV;
        private ArrayList<String> classList = new ArrayList<String>();

        // read the content of the file into a matrix vector (*)
        // get the test set and the trainig set.
        // have a function to build the weka training and testing set
        // call the classifiers and output result

        FastVector getFeatureVector() {
                return featureV;
        }

        String classifySet(Instances iset, Classifier model,FastVector featureV) {
                String statResult = null;
                Double result = null;
                ArrayList<StatisticCollection> results = new ArrayList<StatisticCollection>(featureV.size());

                // classify each element in the set
                for (int i = 0; i < iset.numInstances(); i++) {
                        try {
                                result = model.classifyInstance(iset.instance(i));

                                boolean flag = false;

                                for (StatisticCollection s : results) {
                                        if (s.getClassID().equals(result)) {
                                                s.incrementCount();
                                                flag = true;
                                        }
                                }

                                if (flag == false) {
                                        StatisticCollection element = new StatisticCollection();
                                        element.setClassID(result);
                                        element.setLabel(classList.get(result.intValue()));
                                        element.incrementCount();
                                        results.add(element);
                                }
                        } catch (Exception e) {
                                // TODO Auto-generated catch block
                                statResult +=e.getMessage();
                        }
                }// end of for loop

                // record the percentage of each class
                int sumOfElements = iset.numInstances();

                // return the class that has the most classifications.
                for (StatisticCollection s : results) {
                        s.setPercentage((Double) (double) s.getCount()
                                        / (double) sumOfElements);
                        statResult += "\nclass: " + s.getLabel()
                                        + " :\n" + String.valueOf(s.getPercentage()) + "\n";
                }

                // return result result
                return statResult;
        }

        String classifySet(String path, Classifier model,
                        FastVector featureV) throws Exception {
                Double result = null;
                Instances iset = this.readInstancesFromFile(path, featureV);
                String statResult = null;

                ArrayList<StatisticCollection> results = new ArrayList<StatisticCollection>(featureV.size());

                // classify each element in the set
                for (int i = 0; i < iset.numInstances(); i++) {
                        try {
                                result = model.classifyInstance(iset.instance(i));

                                boolean flag = false;

                                for (StatisticCollection s : results) {
                                        if (s.getClassID().equals(result)) {
                                                s.incrementCount();
                                                flag = true;
                                        }
                                }

                                if (flag == false) {
                                        StatisticCollection element = new StatisticCollection();
                                        element.setClassID(result);
                                        element.incrementCount();
                                        results.add(element);
                                }

                        } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }

                }// end of for loop

                // record the percentage of each class
                int sumOfElements = iset.numInstances();

                // return the class that has the most classifications.
                for (StatisticCollection s : results) {
                        s.setPercentage((Double) (double) s.getCount()
                                        / (double) sumOfElements);
                        statResult += "\nclassID: " + s.getLabel()
                                        + "% :" + String.valueOf(s.getPercentage());
                }

                // print the result
                return statResult;

        }

        // put the content of a file in an Instances object
        Instances readInstancesFromFile(String path, FastVector featureV) {
                Instances iset = new Instances("set", featureV, 1);

                // read the values from the file into a matrix
                ArrayList<ArrayList<String>> values = this.readSetFromFile(path, true,
                                1);

                Instance irecord = null;

                // Fill the content of the matrix into an array of instances
                for (int i = 0; i < values.size(); i++) {
                        // Create an object Instance
                        irecord = getSetInstance(values.get(i), values.get(i).size(),
                                        featureV);

                        // add the instance to a set of instances
                        iset.add(irecord);
                }
                iset.setClassIndex(featureV.size() - 1);

                return iset;
        }

        Classifier CreateClassifier(DataSet dataset) {
                // declare 7 attributes
                // number 8 is the class

                // returns the number of attributes + class in a single instance
                int count = dataset.getTrainingSet().get(0).size();

                // create an array list of the size of the number of attributes in the
                // set
                ArrayList<Attribute> attributes = new ArrayList<Attribute>(count - 1);

                // Initialize the name of the attributes
                // attributes are the variables per instance, (X, Y, Z)
                for (int i = 0; i < count - 1; i++) {
                        Attribute A = new Attribute(String.valueOf(i));
                        attributes.add(A);
                }
                

                // declare the classes
                // should be modified so that the classes are read from the file rather
                // than being harcoded.
                // -- add to the vclasses the different labels in the dataset
                // TODO: write a function to automate this
                FastVector vclasses = new FastVector();
                vclasses.addElement("Fall Detected");
              
                
                // populate the classList vector with the vclasses elements
                // these elements are the different classes to be expected in the dataset
                // passed for the decision tree genesis
                
                Attribute classAttribute = new Attribute("the class", vclasses);

                // fill the feature vector with the data from the elements in the set
                featureV = new FastVector(count);

                for (int i = 0; i < count - 1; i++) {
                        featureV.addElement(attributes.get(i));
                }
                featureV.addElement(classAttribute);

                // now featureV contains the vector describing the features in the set.

                // train the classifier
                // create an empty training set
                isTrainingSet = new Instances("training data", featureV, dataset
                                .getTrainingSet().size());

                // set the class index
                isTrainingSet.setClassIndex(count - 1);

                // fill the training instances with the content of the trainingset
                int tsSize = dataset.getTrainingSet().size();
                for (int i = 0; i < tsSize; i++) {
                        isTrainingSet.add((Instance) getSetInstance(dataset
                                        .getTrainingSet().get(i), count, featureV));
                }

                // chose classifier C4.5 in our case
                Classifier model = (Classifier) new J48();
                try {
                        model.buildClassifier(isTrainingSet);
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.out.println(e.getMessage());
                }
                return model;
        }

        // fills out the content of a record from the set into an Instance object
        Instance getSetInstance(ArrayList<String> record, int count,
                        FastVector featureV) {
                Instance irecord  = new Instance(count);
                try {
                        for (int i = 0; i < count - 1; i++) {
                                irecord.setValue((Attribute) featureV.elementAt(i), Float
                                                .parseFloat(record.get(i)));
                        }
                        // add the class
                        irecord.setValue((Attribute) featureV.elementAt(count - 1),
                                        (String) record.get(count - 1));
                } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                }
                return irecord;
        }

        // evaluates the accuracy of a model
        Evaluation EvaluateClassifier(Classifier model, DataSet dataset) {

                // returns the number of attributes + class in a single instance
                int count = dataset.getTrainingSet().get(0).size();

                int tsSize = dataset.getTrainingSet().size();

                Evaluation test = null;

                // test the classifier
                try {
                        test = new Evaluation(isTrainingSet);

                        // create testset
                        // train the classifier
                        // create an empty training set
                        Instances isTestSet = new Instances("training data", featureV,
                                        dataset.getTestSet().size());

                        // set the class index
                        isTestSet.setClassIndex(count - 1);

                        // fill the training instances with the content of the trainingset
                        tsSize = dataset.getTestSet().size();
                        for (int i = 0; i < tsSize; i++) {
                                isTestSet.add((Instance) getSetInstance(dataset.getTestSet()
                                                .get(i), count, featureV));
                        }

                        // replace with test set
                        test.evaluateModel(model, isTestSet);

                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

                return test;
        }

        void BuildTrainingAndTestSet(DataSet dataset) {

                // shuffle the data in the mainSet
                dataset.shuffleMainSet();

                float count = dataset.getMainSet().size();

                int index = 0;

                // 2/3 of the data goes to the training set
                while (index < count * ((float) 2 / (float) 3)) {
                        // if the record contains a missing class "?" do not include it.
                        // String value =
                        // dataset.getMainSet().get(index).get(dataset.getMainSet().get(index).size()-1);
                        // if(!value.contains("?"))
                        dataset.getTrainingSet().add(dataset.getMainSet().get(index));
                        index++;
                }

                // put the remaining third into the test set
                while (index < count) {
                        dataset.getTestSet().add(dataset.getMainSet().get(index));
                        index++;
                }

        }

        // reads the content of the file in to a two dimensional Array of elements
        ArrayList<ArrayList<String>> readSetFromFile(String path,
                        boolean removeFirstColumn, int filetype) {

                ArrayList<ArrayList<String>> set = new ArrayList<ArrayList<String>>();
                try {
                        String file = path;

                        BufferedReader in = new BufferedReader(new FileReader(file));

                        String line = in.readLine(); //discard the first line

                        while ((line = in.readLine()) != null) {
                                // System.out.println(line);

                                ArrayList<String> record = this.parseLine(line,
                                                removeFirstColumn, filetype);
                                set.add(record);
                                // this.printRecord(record);
                        }

                } catch (Exception ex) {
                        System.out.print(ex.getMessage());

                }
                return set;
        }

        void printRecord(ArrayList<String> record) {
                for (int i = 0; i < record.size(); i++) {
                        System.out.print(record.get(i) + " ");
                }
                System.out.print("\n");
        }

        ArrayList<String> parseLine(String line, boolean removeFirstColumn,
                        int fileType) {
                StringTokenizer st = new StringTokenizer(line);
                // csv
                if (fileType == 1)
                        st = new StringTokenizer(line, ","); //delimited by commas
                
                ArrayList<String> record = new ArrayList<String>();

                int cnt = 0;
                while (st.hasMoreTokens()) {
                        if (cnt != 0)
                                // s += st.nextToken() + ",";
                                record.add((String) st.nextToken());
                        cnt++;
                }
                if (removeFirstColumn)
                        record.remove(0);

                return record;
        }

}