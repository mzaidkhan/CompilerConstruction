package ch.unibe.iam.scg.minijava.optimizer;

import java.util.HashMap;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.AssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Block;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.BracketExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Factor;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IfStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodCall;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeChoice;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeListOptional;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeOptional;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.PrimaryExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.PrintStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Statement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Term;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VarDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.WhileStatement;
import ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor;

/**
 * 
 * visit all nodes in the AST and changes subtrees (if they are constant) to a
 * single constant node)
 * 
 * @author j
 * TODO: never replace fields
 */
public class ConstantDetectorVisitor extends DepthFirstVisitor {

	private boolean constant;
	private EvaluationVisitor eval;
	private int optimizations;
	private Node replaceStatementWith;
	private HashMap<String, Object> constantVars;
	private boolean insideMethod;
	private Node replaceIdentifierWith;
	
	private int occurencesOfPrintOrMethodCall;

	public ConstantDetectorVisitor() {
		this.constant = false;
		this.optimizations = 0;
		this.eval = new EvaluationVisitor();
		this.constantVars = null;
		this.replaceStatementWith = null;
		this.replaceIdentifierWith = null;
		this.insideMethod = false;
	}

	@Override
	public void visit(MethodDeclaration n) {
		this.constantVars = new HashMap<String, Object>();
		this.insideMethod = true;
		n.f7.accept(this);
		n.f8.accept(this);
		
		n.f10.accept(this);
		if (g()){
			
			occurencesOfPrintOrMethodCall=0;
			
			n.f8.accept(new DepthFirstVisitor() {
				@Override
				public void visit(PrintStatement n) {
					occurencesOfPrintOrMethodCall++;
				}
				@Override
				public void visit(MethodCall n) {
					occurencesOfPrintOrMethodCall++;
				}
			});
			
			if (occurencesOfPrintOrMethodCall==0) {
				n.f7= new NodeListOptional();
				n.f8=new NodeListOptional();
			}
		}
		
		this.insideMethod = false;
	}

	@Override
	public void visit(VarDeclaration n) {
		super.visit(n);
		if (this.insideMethod) {
			String identifier = n.f1.f0.tokenImage;
			if (n.f0.toString().equals("int"))
				this.constantVars.put(identifier, 0);
			else if (n.f0.toString().equals("boolean") || n.f0.toString().equals("bool"))
				this.constantVars.put(identifier, false);
		}
	}

	@Override
	public void visit(Statement n) {
		super.visit(n);

		if (this.replaceStatementWith != null) {
			n.f0 = new NodeChoice(this.replaceStatementWith);
			this.replaceStatementWith = null;
			this.optimizations++;
		}
	}

	@Override
	public void visit(IfStatement n) {

		Node replace = null;
		n.f2.accept(this);
		if (g()) {
			optimizeToTargetNode(n.f2, "Expression");
			Boolean b = (Boolean) this.eval.getValue();

			NodeSequence seq = new NodeSequence(new NodeToken(""));
			if (b) {
				seq.addNode(n.f4);
				n.f4.accept(this);
			}
			else{
				seq.addNode(n.f6);
				n.f6.accept(this);
			}
			seq.addNode(new NodeToken(""));

			replace = new Block(new NodeListOptional(seq));
		} else {
			HashMap<String, Object> copy = (HashMap<String, Object>)this.constantVars.clone();
			n.f4.accept(this);
			this.constantVars= copy;
			n.f6.accept(this);
			
			if (constantVars != null) {
				n.f4.accept(new DepthFirstVisitor() {
					@Override
					public void visit(AssignmentStatement n) {
						String id = n.f0.f0.tokenImage;
						constantVars.remove(id);
					}
				});
				n.f6.accept(new DepthFirstVisitor() {
					@Override
					public void visit(AssignmentStatement n) {
						String id = n.f0.f0.tokenImage;
						constantVars.remove(id);
					}
				});
			}
			
		}
		this.replaceStatementWith = replace;
	}

	@Override
	public void visit(WhileStatement n) {
		// check for assigned vars, they are not constants
		if (constantVars != null) {
			n.f4.accept(new DepthFirstVisitor() {
				@Override
				public void visit(AssignmentStatement n) {
					String id = n.f0.f0.tokenImage;
					constantVars.remove(id);
				}
			});
		}

		n.f2.accept(this);
		Node replace = null;
		if (g()) {
			optimizeToTargetNode(n.f2, "Expression");
			Boolean b = (Boolean) this.eval.getValue();

			if (!b) {
				NodeSequence seq = new NodeSequence(new NodeToken(""));
				seq.addNode(new NodeToken(""));
				replace = new Block(new NodeListOptional(seq));
			}
		}
		n.f4.accept(this);
		this.replaceStatementWith = replace;
	}

	@Override
	public void visit(Expression n) {
		n.f0.accept(this); // visit first element
		if (n.f1.present()) {
			boolean c1 = g(); // save whether constant
			n.f1.accept(this); // visit second element
			boolean c2 = g(); // save whether constant
			if (c1 && c2) {
				n.f0 = (Sum) optimizeToTargetNode(n, "Sum");
				n.f1 = new NodeOptional();
			}
			sc(c1 && c2);
		}
	}

	@Override
	public void visit(Sum n) {
		n.f0.accept(this); // visit first element
		if (n.f1.present()) {
			boolean c1 = g(); // save whether constant
			n.f1.accept(this); // visit second element
			boolean c2 = g(); // save whether constant
			if (c1 && c2) {
				n.f0 = (Term) optimizeToTargetNode(n, "Term");
				n.f1 = new NodeOptional();
			}
			sc(c1 && c2);
		}
	}

	@Override
	public void visit(Term n) {
		n.f0.accept(this); // visit first element
		if (n.f1.present()) {
			boolean c1 = g(); // save whether constant
			n.f1.accept(this); // visit second element
			boolean c2 = g(); // save whether constant
			if (c1 && c2) {
				n.f0 = (Factor) optimizeToTargetNode(n, "Factor");
				n.f1 = new NodeOptional();
			}
			sc(c1 && c2);
		}
	}

	public void optimize(Node n) {
		n.accept(this);
	}

	public Expression optimizeExpression(Node n) {
		// needs bracket expression around to update reference
		BracketExpression expr = new BracketExpression((Expression) n);
		this.optimizations = 0;
		expr.accept(this);
		return expr.f1;
	}

	private Node optimizeToTargetNode(Node n, String targetNodeType) {
		this.optimizations++;
		this.eval.setConstantVars(this.constantVars);
		Node result = this.eval.evaluate(targetNodeType, n);
		this.eval.setConstantVars(null);
		return result;
	}

	@Override
	public void visit(AssignmentStatement n) {
		n.f2.accept(this);
		String identifier = n.f0.f0.tokenImage;

		if (g()) {
			optimizeToTargetNode(n.f2, "Expression");
			if (this.constantVars.containsKey(identifier))
				this.constantVars.put(identifier, this.eval.getValue());
		} else if (this.constantVars != null) {
			this.constantVars.remove(identifier);
		}
	}

	@Override
	public void visit(ArrayAllocationExpression n) {
		super.visit(n);
		nc();
	}
	
	@Override
	public void visit(ArrayLookUp n) {
		super.visit(n);
		nc();
	}

	@Override
	public void visit(IntegerLiteral n) {
		c();
	}

	@Override
	public void visit(TrueLiteral n) {
		c();
	}

	@Override
	public void visit(FalseLiteral n) {
		c();
	}

	@Override
	public void visit(PrimaryExpression n) {
		super.visit(n);

		if (this.replaceIdentifierWith != null) {
			n.f0 = new NodeChoice(this.replaceIdentifierWith);
			this.replaceIdentifierWith = null;
			this.optimizations++;
		}
	}

	@Override
	public void visit(Identifier n) {
		String id = n.f0.tokenImage;
		if (this.constantVars == null || !this.constantVars.containsKey(id))
			nc();
		else {
			ConstantGenerator cg = new ConstantGenerator();
			Node newNode = cg.generateConstantLiterals("PrimaryExpression",
					this.constantVars.get(id));
			this.replaceIdentifierWith = newNode;
			c();
		}
	}

	// memorizes, that the current value is constant
	private void c() {
		this.constant = true;
	}

	// returns whether the current value is constant
	public boolean g() {
		return this.constant;
	}

	// memorizes, that the current value is not constant
	private void nc() {
		this.constant = false;
	}

	// memorizes, whether the current value is constant
	private void sc(boolean c) {
		this.constant = c;
	}

	public int getOptimizedValue() {
		return this.optimizations;
	}

}
