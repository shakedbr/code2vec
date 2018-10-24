package JavaExtractor.Common;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;

public class MethodContent {
	private List<Node> leaves;
	private String name;
	private long length;

	public MethodContent(List<Node> leaves, String name, long length) {
		this.leaves = leaves;
		this.name = name;
		this.length = length;
	}

	public List<Node> getLeaves() {
		return leaves;
	}

	public String getName() {
		return name;
	}

	public long getLength() {
		return length;
	}

}
