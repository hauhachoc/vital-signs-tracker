package com.accelerometerstudy;

public class StatisticCollection {
	
	    private Double classID;
	    private int count;
	    private String label;
	    private Double percentage; 
	    
	    /*getters*/
	    Double getClassID(){
	         return this.classID;
	    }

	    Double getPercentage(){
	         return this.percentage;
	    }
	    
	    String getLabel(){
	         return this.label;
	    }
	    
	    int getCount(){
	         return this.count;
	    }
	    
	    /*setters*/
	    
	    void setClassID(Double _classID){
	         this.classID = _classID;
	    }

	    void setPercentage(Double _percentage){
	         this.percentage = _percentage;
	    }
	    
	    void setLabel(String _label){
	         this.label = _label;
	    }

	    void setClassID(int _count){
	         this.count = _count;
	    }

	    void incrementCount(){
	         this.count++;
	    }
	    
	}


