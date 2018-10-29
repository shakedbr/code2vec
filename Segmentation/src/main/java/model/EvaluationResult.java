package model;

import com.github.javaparser.ast.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EvaluationResult {
    private Node node;
    private PredictionsResult predictionsResult;
    private boolean bestLabeled;
    private String label;

    public EvaluationResult(Node node, PredictionsResult predictionsResult, boolean bestLabeled, String label) {
        this.node = node;
        this.predictionsResult = predictionsResult;
        this.bestLabeled = bestLabeled;
        this.label = label;
    }

    public Node getNode() {
        return node;
    }

    public PredictionsResult getPredictionsResult() {
        return predictionsResult;
    }

    public boolean isBestLabeled() {
        return bestLabeled;
    }

    public String getLabel() {
        return label;
    }

    public String formatResults() {
        String code = node.toString();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String predictions =  gson.toJson(predictionsResult);
        String part;
        if (bestLabeled) {
            part = "best labeled with label: " + label;
        } else {
            part = "not best labeled";
        }
        String output  = "The AST of " + code + "\n is " + part
                + "\n with those predictions results: " + predictions + "\n";
        return output;
    }
}
