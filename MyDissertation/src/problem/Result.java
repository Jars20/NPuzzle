package problem;

import java.util.ArrayList;
import java.util.List;

public class Result {
    private int countSuccess;
    private int countFailed;
    private int countUnsolvable;
    private int steps;
    private List<Integer> stepsList;
    private double timeCost;

    public int getCountUnsolvable() {
        return countUnsolvable;
    }

    public void setCountUnsolvable(int countUnsolvable) {
        this.countUnsolvable = countUnsolvable;
    }

    public void setStepsList(List<Integer> stepsList) {
        this.stepsList = stepsList;
    }

    public double getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(double timeCost) {
        this.timeCost = timeCost;
    }

    public Result() {
        setCountFailed(0);
        setCountSuccess(0);
        setCountUnsolvable(0);
        setSteps(0);
        setTimeCost(0.0);
        initStepsList();
    }

    public Result(int countSuccess, int countUnsolvable, int countFailed, int steps, double timeCost) {
        this.countSuccess = countSuccess;
        this.countUnsolvable = countUnsolvable;
        this.countFailed = countFailed;
        this.steps = steps;
        initStepsList();
        this.timeCost = timeCost;
    }

    double getAvgStep() {
        double sum = 0.0;
        for (Integer integer : stepsList) {
            sum += (double) integer;
        }
        return sum / stepsList.size();
    }

    public List<Integer> getStepsList() {
        return stepsList;
    }

    public void initStepsList() {
        this.stepsList = new ArrayList<>();
    }

    public void addStepInList(int step) {
        stepsList.add(step);
    }

    public int getCountSuccess() {
        return countSuccess;
    }

    public void setCountSuccess(int countSuccess) {
        this.countSuccess = countSuccess;
    }

    public int getCountFailed() {
        return countFailed;
    }

    public void setCountFailed(int countFailed) {
        this.countFailed = countFailed;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getCountAll() {
        return countFailed + countSuccess+ countUnsolvable;
    }

    @Override
    public String toString() {
        return "The num of all nodes is: " + getCountAll() + "\n" + "The num of successful nodes is: " + getCountSuccess() + "\n" + "The num of fail nodes is: " + getCountFailed() + "\n" + "The num of unsolvable nodes is: " + getCountUnsolvable() + "\n";
    }
}
