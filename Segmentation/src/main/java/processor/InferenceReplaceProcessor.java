package processor;

import JavaExtractor.Visitors.LeavesCollectorVisitor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import evaluation.IEvaluation;
import model.EvaluationResult;
import model.PredictionsResult;
import utils.Listener;
import utils.ParsingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class InferenceReplaceProcessor extends InferenceProcessor {

    public InferenceReplaceProcessor(Listener listener,  IEvaluation eval) {
        super(listener, eval);
    }

    @Override
    public EvaluationResult process(Node node) {
        EvaluationResult evaluationResult = super.process(node);
        if (node.getParentNode() == null  || !node.getParentNode().getClass().equals(BlockStmt.class)) {
            // Don't try to replace subtree which is not in statement (for example, part of an expression)
            return evaluationResult;
        }
        if (evaluationResult.isBestLabeled() ) {
            String label = evaluationResult.getLabel();
            Set<String> variables = getVariables(node);
            String functionCall = label + "(" + String.join(",", variables) + ");";
            Statement compilationUnit = JavaParser.parseStatement(functionCall);
            BlockStmt parent = (BlockStmt)node.getParentNode();
            List<Node> parentChildren = parent.getChildrenNodes();
            List<Statement> statements = parent.getStmts();
            int nodeIndex = 0;
            for (Node n : parentChildren) {

                if (n.equals(node)) {
                    break;
                }
                nodeIndex++;
            }
            // replace old Node with function call
            node.remove();
            compilationUnit.setParentNode(parent);
            parentChildren.remove(parentChildren.size()-1);
            parentChildren.add(nodeIndex, compilationUnit);
            statements.add(nodeIndex, compilationUnit);
            evaluationResult.setReplaceTo(functionCall);
        }
        return evaluationResult;
    }

    private Set<String> getVariables(Node node) {
        final Set<Node> methods = new HashSet<>(node.getNodesByType(MethodCallExpr.class));
        return node.getNodesByType(NameExpr.class)
                .stream()
                .filter(x -> !methods.contains(x.getParentNode()))
                .map(Node::toString)
                .filter(x -> !x.equals("null"))
                .collect(Collectors.toSet());
    }
}
