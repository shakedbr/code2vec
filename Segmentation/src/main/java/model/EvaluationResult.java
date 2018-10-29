package model;

import com.github.javaparser.ast.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EvaluationResult {
    private Node node;
    private PredictionsResult predictionsResult;
    private boolean bestLabeled;
    private String label;
    private String replaceTo;

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

    public void setReplaceTo(String replaceTo) {
        this.replaceTo = replaceTo;
    }

    public String getReplaceTo() {
        return replaceTo;
    }

    public String formatResults() {
        String code = node.toString();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String predictions =  gson.toJson(predictionsResult);
        String part;
        if (bestLabeled) {
            part = "best labeled with the label: '" + label + "'";
            if (replaceTo != null) {
                part = part + ", and can be replace with this function call: '" + replaceTo + "'";
            }
        } else {
            part = "not best labeled";
        }
        String output  = "The AST of\n" + code + "\n is " + part
                + "\n with those predictions results: " + predictions + "\n";
        return output;
    }
}
