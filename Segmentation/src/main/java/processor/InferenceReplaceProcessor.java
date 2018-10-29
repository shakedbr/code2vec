package processor;

import com.github.javaparser.ast.Node;
import evaluation.IEvaluation;
import model.EvaluationResult;
import model.PredictionsResult;
import utils.Listener;

public class InferenceReplaceProcessor extends InferenceProcessor {

    public InferenceReplaceProcessor(Listener listener,  IEvaluation eval) {
        super(listener, eval);
    }

    @Override
    public EvaluationResult process(Node node) {
        EvaluationResult evaluationResult = super.process(node);
        if (evaluationResult.isBestLabeled()) {
            // TODO: replace node code with function call
        }
        return evaluationResult;
    }
}
