package com.zachl.restock.entities.objects;

public class CalculatorForm {
    public int metric;
    public int[] entered;
    public CalculatorForm(int metric, int[] entered){
        this.metric = metric;
        this.entered = entered;
    }
}
