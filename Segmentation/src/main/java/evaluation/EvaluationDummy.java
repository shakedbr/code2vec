package evaluation;

import model.PredictionsResult;
import model.Tuple;

public class EvaluationDummy implements IEvaluation {

    @Override
    public Tuple<Boolean, String> isBestLabeled(PredictionsResult predictionsResult) {
        return new Tuple<>(true, "DummyLabel");
    }

}
