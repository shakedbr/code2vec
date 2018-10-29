package processor;

import com.github.javaparser.ast.Node;
import model.EvaluationResult;
import model.PredictionsResult;
import model.Tuple;

public interface INodeProcessor {

    EvaluationResult process(Node node);
}
