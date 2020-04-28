package com.zachl.restock.entities.math;

public class Function {
    /**
     * An equation container class for managing the different equations required for the various result values without overcrowding the ResultsActivity class.
     * I'm in the process of modifying these, so as of now they're largely placeholders.
     */
    private int[] variables;

    public enum Multiplier{
        /**
         * RESTPOINT 1 - FETCH
         *  - The first value is the first metric the user can choose; for toilet paper, this is ONLY sheets, so there are only two variables - sheets and avg. consumption
         *  - For hand san. and water bottles, the first metric is for oz and the second is for mL
         */
        HS(new double[]{0.201d, 5.94d, 11 /*AVERAGE CONSUMPTION*/}),
        TP(new double[]{57, 7 /*AVERAGE CONSUMPTION*/}),
        WB(new double[]{43.26d, 1279.35d, 64/*AVERAGE CONSUMPTION (this is in oz and needs to be addressed as it doesnt cover mL, come back to this)*/}, true);
        double[] mults;
        /**
         * You'll notice the only resource marked with intensity are water bottles. This is bc the user enters how many they drink a day, rather than how many times they use it a day, as this wouldn't function
         * well with water bottles. This is necessary as the number of water bottles they drink needs to be multiplied by the size they previously entered. This will require some changes, as currently the
         * avg. consumption only accounts for oz., so I'll work on that soon.
         */
        boolean intensity;
        Multiplier(double[] mult){
            this.mults = mult;
            intensity = false;
        }
        Multiplier(double[] mult, boolean intensity){
            this.mults = mult;
            this.intensity = intensity;
        }
    }
    public Function(int[] variables){
        this.variables = variables;
    }

    public double[] apply(Multiplier eq){
        double[] needs = new double[3];
        //DAYS SURVIVED
        if(eq.intensity)
            variables[4] *= variables[3];
        needs[0] = (double)Math.round(((variables[1] * variables[2]) / eq.mults[variables[5]]) / variables[3] / ((float)variables[4]/eq.mults[eq.mults.length - 1]));
        //DIFFERENCE IN SURVIVED AND QUARANTINE
        needs[1] = needs[0] - variables[0];
        //RATION PERCENT
        needs[2] = (int)((needs[0] / needs[1]) * 100);
        return needs;
    }
}
