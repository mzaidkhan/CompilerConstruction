package ch.unibe.iam.scg.minijava.optimizer;

import java.util.HashMap;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NotExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Term;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor;

/**
 * just stored the evaluated value of the node and returns it by getter
 * @author j
 *
 */
public class EvaluationVisitor extends DepthFirstVisitor {
	
	private ConstantGenerator conGen;
	private Object currentValue;
	private HashMap<String, Object> constantVars;

	public EvaluationVisitor() {
		this.conGen=new ConstantGenerator();
		this.currentValue=0;
		this.constantVars=null;
	}

	public Node evaluate(String method,Node n) {
		n.accept(this);
		return conGen.generateConstantLiterals(method,this.currentValue);
	}
	
	public Object getValue() {
		return this.currentValue;
	}
	
	@Override
	public void visit(Expression n) {
		n.f0.accept(this);
		Object v1= this.currentValue;
		
		if (n.f1.present()) {
			n.f1.accept(this);
			NodeSequence seq = (NodeSequence)n.f1.node;
			Node operator=seq.elementAt(0);
			String op=operator.toString();
			if (op.equals("&&"))
				this.currentValue = (Boolean)v1 && (Boolean)this.currentValue;
			else if (op.equals("<"))
				this.currentValue = (Boolean)((Integer)v1 < (Integer)this.currentValue);
			else if (op.equals(">"))
				this.currentValue = (Boolean)((Integer)v1 > (Integer)this.currentValue);
		}
	}
	
	@Override
	public void visit(Sum n) {
		n.f0.accept(this);
		Object v1= this.currentValue;
		
		if (n.f1.present()) {
			n.f1.accept(this);
			NodeSequence seq = (NodeSequence)n.f1.node;
			Node operator=seq.elementAt(0);
			String op=operator.toString();
			if (op.equals("+"))
				this.currentValue = (Integer)v1 + (Integer)this.currentValue;
			if (op.equals("-"))
				this.currentValue = (Integer)v1 - (Integer)this.currentValue;
		}
	}
	
	
	@Override
	public void visit(Term n) {
		n.f0.accept(this);
		Object v1=this.currentValue;
		
		if (n.f1.present()) {
			n.f1.accept(this);
			this.currentValue = (Integer)v1 * (Integer)this.currentValue;
		}
	}
	
	@Override
	public void visit(NotExpression n) {
		n.f1.accept(this);
		this.currentValue=!((Boolean)this.currentValue);
	}
	
	@Override
	public void visit(IntegerLiteral n) {
		this.currentValue=Integer.valueOf(n.f0.tokenImage);
	}
	
	@Override
	public void visit(TrueLiteral n) {
		this.currentValue = Boolean.TRUE;
	}
	
	@Override
	public void visit(FalseLiteral n) {
		this.currentValue=Boolean.FALSE;
	}
	
	@Override
	public void visit(Identifier n) {
		if (this.constantVars==null)
			throw new RuntimeException("hashmap for constant not set. Var "+n+" cannot be looked up.");
		String identifier=n.f0.tokenImage;
		if (this.constantVars.containsKey(identifier)){
			this.currentValue= this.constantVars.get(identifier);
		}
	}

	public void setConstantVars(HashMap<String, Object> constantVars) {
		this.constantVars=constantVars;
	}

}
