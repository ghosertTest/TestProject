package org.hibernate.hql.ast.tree;

import antlr.collections.AST;
import antlr.Token;
import org.hibernate.util.StringHelper;

/**
 * Generic AST Node.
 * User: Joshua Davis<br>
 * Date: Sep 23, 2005<br>
 * Time: 12:20:53 PM<br>
 */
public class Node extends antlr.CommonAST {
	private String filename;
	private int line;
	private int column;
	private int textLength;

	public Node() {
		super();
	}

	public Node(Token tok) {
		super(tok);  // This will call initialize(tok)!
	}

	public void initialize(Token tok) {
		super.initialize(tok);
		filename = tok.getFilename();
		line = tok.getLine();
		column = tok.getColumn();
		String text = tok.getText();
		textLength = StringHelper.isEmpty(text) ? 0 : text.length();
	}

	public void initialize(AST t) {
		super.initialize(t);
		if (t instanceof Node)
		{
			Node n = (Node)t;
			filename = n.filename;
			line = n.line;
			column = n.column;
			textLength = n.textLength;
		}
	}

	public String getFilename() {
		return filename;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	public int getTextLength() {
		return textLength;
	}
}
