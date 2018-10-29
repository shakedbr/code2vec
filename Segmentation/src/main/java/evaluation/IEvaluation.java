package evaluation;

import model.PredictionsResult;
import model.Tuple;

public interface IEvaluation {

    Tuple<Boolean, String> isBestLabeled(PredictionsResult predictionsResult);
}
