package processor;

import com.github.javaparser.ast.Node;
import com.google.gson.Gson;
import evaluation.IEvaluation;
import model.EvaluationResult;
import model.Predictions;
import model.PredictionsResult;
import model.Tuple;
import utils.Listener;

public class InferenceProcessor implements INodeProcessor {
    private Listener listener;
    private IEvaluation eval;



    public InferenceProcessor(Listener listener,  IEvaluation eval) {
        this.listener = listener;
        this.eval = eval;
    }

    @Override
    public EvaluationResult process(Node node) {
        String code = node.toString();
        String jsonResult = listener.runMethod(code);
        Gson gson = new Gson();
        PredictionsResult predictionsResult = gson.fromJson(jsonResult, PredictionsResult.class);
        Tuple<Boolean, String> evaluationResults = evaluateNode(predictionsResult);
        String label = evaluationResults.getSecond();
        boolean isBestLabeled = evaluationResults.getFirst();
        return new EvaluationResult(node, predictionsResult,isBestLabeled, label);
    }

    protected Tuple<Boolean, String> evaluateNode(PredictionsResult predictionsResult) {
        return this.eval.isBestLabeled(predictionsResult);
    }
}
