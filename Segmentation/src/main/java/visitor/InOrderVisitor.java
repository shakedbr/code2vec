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

public class InOrderVisitor extends TreeVisitor {
    private List<EvaluationResult> results;
    private INodeProcessor processor;
    private int currentId = 1;

    public InOrderVisitor(INodeProcessor processor) {
        this.processor = processor;
        results = new ArrayList<>();
    }

    @Override
    public void process(Node node) {
        if (node instanceof Comment) {
            return;
        }
        boolean isGenericParent = VisitorsUtils.isGenericParent(node);
        if (!VisitorsUtils.hasNoChildren(node) && VisitorsUtils.isNotComment(node) && VisitorsUtils.isValid(node)) {
            if (!node.toString().isEmpty() && (!"null".equals(node.toString()) || (node instanceof NullLiteralExpr))) {
                EvaluationResult res = processor.process(node);
                results.add(res);
            }
        }
        if (node.getParentNode() == null) {
            return;
        }
        int childId = VisitorsUtils.getChildId(node);
        node.setUserData(Common.ChildId, childId);
        Property property = new Property(node, false, isGenericParent, currentId++);
        node.setUserData(Common.PropertyKey, property);
    }

    @Override
    public void visitDepthFirst(Node node) {
        Iterator var2 = node.getChildrenNodes().iterator();
        while(var2.hasNext()) {
            Node child = (Node)var2.next();
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
