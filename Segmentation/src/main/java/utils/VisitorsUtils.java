package utils;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class VisitorsUtils {

    public static int getChildId(Node node) {
        Node parent = node.getParentNode();
        List<Node> parentsChildren = parent.getChildrenNodes();
        int childId = 0;
        for (Node child: parentsChildren) {
            if (child.getRange().equals(node.getRange())) {
                return childId;
            }
            childId++;
        }
        return childId;
    }

    public static boolean hasNoChildren(Node node) {
        return node.getChildrenNodes().size() == 0;
    }

    public static boolean isNotComment(Node node) {
        return !(node instanceof Comment);
    }
    public static boolean isNotStatement(Node node) {
        return !(node instanceof Statement);
    }

    public static boolean isGenericParent(Node node) {
        return (node instanceof ClassOrInterfaceType)
                && ((ClassOrInterfaceType)node).getTypeArguments() != null
                && ((ClassOrInterfaceType)node).getTypeArguments().size() > 0;
    }

    public static boolean isValid(Node node) {
        String code = node.toString();
        try {
            ParsingUtils.parseFileWithRetries(code);
        } catch (IOException | ParseProblemException e) {
            //System.out.println("[J]: " + code + " is not valid Java code");
            return false;
        }
        return true;
    }
}
