package com.beslenge.iterio.model;

public class BunkCalculator {
    public BunkCalculator() {
    }

    public int classesRequiredToAttend(double attended, double total, double percentage, double requiredPercentage) {
        double x = ((total * (requiredPercentage / 100)) - attended) / (1 - (requiredPercentage / 100));
        if (requiredPercentage > percentage) {
            if (possible(x, attended, total, requiredPercentage))
                return (int) (x + 1);
            else
                return (int) (x);
        } else
            return 0;

    }

    public int classesRequiredToBunk(double attended, double total, double percentage, double requiredPercentage) {
        double x = ((total * (requiredPercentage / 100)) - attended) / ((requiredPercentage / 100)) * (-1);
        if (requiredPercentage < percentage) {
            if (!possible(x, attended, total, requiredPercentage))
                return (int) (x - 1);
            else
                return (int) (x);
        } else
            return 0;

    }


    private boolean possible(double needToAttend, double attended, double total, double requiredPercentage) {
        double resultPercentage = ((attended + needToAttend) / (total + needToAttend)) * 100;
        return Math.abs(resultPercentage) >= requiredPercentage;
    }
}
