package com.WekaStudy;
import java.util.ArrayList;
import java.util.Collections;


public class DataSet {
        private ArrayList <ArrayList<String>> mainSet = new ArrayList<ArrayList<String>>();
        private ArrayList <ArrayList<String>> trainingSet = new ArrayList<ArrayList<String>>();
        private ArrayList <ArrayList<String>> testSet = new ArrayList<ArrayList<String>>();
        
        
        void shuffleMainSet(){
                Collections.shuffle(mainSet);
        }
        
        //getters
        ArrayList <ArrayList<String>> getTrainingSet(){
                return trainingSet;
        }
        ArrayList <ArrayList<String>> getTestSet(){
                return testSet;
        }
        ArrayList <ArrayList<String>> getMainSet(){
                return mainSet;
        }
        
        //setters
        void setTrainingSet(ArrayList <ArrayList<String>> _trSet){
                 trainingSet = _trSet;
        }
        void setTestSet(ArrayList <ArrayList<String>> _tsSet){
                testSet=_tsSet;
        }
        void setMainSet(ArrayList <ArrayList<String>> _mSet){
                mainSet = _mSet;
        }
        
        
}