package visitor;

import JavaExtractor.Common.Common;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.visitor.TreeVisitor;
import model.EvaluationResult;
import model.PredictionsResult;
import model.Tuple;
import processor.INodeProcessor;
import utils.VisitorsUtils;
import JavaExtractor.FeaturesEntities.Property;


import java.util.*;

public class PostOrderVisitor extends TreeVisitor {
    private List<EvaluationResult> results;
    private INodeProcessor processor;
    private int currentId = 1;

    public PostOrderVisitor(INodeProcessor processor) {
        this.processor = processor;
        results = new ArrayList<>();
    }

    @Override
    public void process(Node node) {
        if (node instanceof Comment) {
            return;
        }
        if (!VisitorsUtils.hasNoChildren(node) && VisitorsUtils.isNotComment(node) && VisitorsUtils.isValid(node)) {
            if (!node.toString().isEmpty() && (!"null".equals(node.toString()) || (node instanceof NullLiteralExpr))) {
                EvaluationResult res = processor.process(node);
                results.add(res);
            }
        }
    }

    @Override
    public void visitDepthFirst(Node node) {
        List<Node> childrenNodes = node.getChildrenNodes();
        int size = childrenNodes.size();
        for (int i = 0; i < size; i++) {
            Node child = childrenNodes.get(i);
            this.visitDepthFirst(child);
        }
        this.process(node);
    }

    @Override
    public void visitBreadthFirst(Node node) {
        System.out.println("BFS is forbidden, please use visitDepthFirst method");
    }

    public List<EvaluationResult>  getResults() {
        return results;
    }

}
