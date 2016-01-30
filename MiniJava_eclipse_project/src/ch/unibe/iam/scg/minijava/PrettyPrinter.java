package ch.unibe.iam.scg.minijava;

import java.util.Stack;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.AllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.AssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Block;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.BooleanType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.BracketExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.F;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Factor;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameter;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameterList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Goal;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IfStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.LengthLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MainClass;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MainFunction;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodCall;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreFormalParameter;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreParameterExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeListOptional;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeOptional;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NotExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ParamList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.PrimaryExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.PrintStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Statement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.StringArrayType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Term;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ThisExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Type;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VarDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VoidType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.WhileStatement;
import ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor;

/**
 * 
 * @author Julian Schelker, Zaid Khan
 */
public class PrettyPrinter extends DepthFirstVisitor {

	private static final String blockIndentationString = "    ";

	protected StringBuffer currentString;
	private int indentation;
	private Stack<Boolean> addNewLineBetweenNodes;
	private Stack<String> additionalSeparation;

	public String prettyPrint(Node node) {
		// start building string for the syntaxtree that is passed
		this.currentString = new StringBuffer("");
		this.indentation = 0;
		this.addNewLineBetweenNodes = new Stack<Boolean>();
		this.additionalSeparation = new Stack<String>();
		this.addNewLineBetweenNodes.push(true);
		this.additionalSeparation.push("");

		// traverse tree and add everything to string
		node.accept(this);

		return this.currentString.toString();
	}

	/**
	 * adds the indentation to the current string
	 */
	private void ind() {
		for (int i = 0; i < this.indentation; i++)
			this.currentString.append(blockIndentationString);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeList)
	 */
	@Override
	public void visit(NodeList n) {
		for (int i = 0; i < n.size(); i++) {
			indNode(n.elementAt(i));
		}
	}

	private void indNode(Node n) {
		indNode(n, false);
	}

	private void indNode(Node n, boolean lastOne) {
		if (this.addNewLineBetweenNodes.peek())
			ind();
		n.accept(this);
		if (this.addNewLineBetweenNodes.peek() && !lastOne)
			add("\n");
		if (!lastOne)
			add(this.additionalSeparation.peek());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeListOptional)
	 */
	@Override
	public void visit(NodeListOptional n) {
		if (n.present()) {
			// exactly the same as visit(NodeList n)
			for (int i = 0; i < n.size(); i++) {
				indNode(n.elementAt(i), i == n.size() - 1);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeOptional)
	 */
	@Override
	public void visit(NodeOptional n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence)
	 */
	@Override
	public void visit(NodeSequence n) {
		for (int i = 0; i < n.size(); i++) {
			indNode(n.elementAt(i), i == n.size() - 1);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken)
	 */
	@Override
	public void visit(NodeToken n) {
		super.visit(n);
		add(n.tokenImage);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Goal)
	 */
	@Override
	public void visit(Goal n) {
		add("//Pretty Printer says Hi There!\n");
		n.f0.accept(this);
		this.additionalSeparation.push("\n\n");
		if (n.f1.present())
			add("\n\n");
		n.f1.accept(this);
		this.additionalSeparation.pop();
		add("\n\n");
		n.f2.accept(this);
	}

	@Override
	public void visit(MainClass n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		add(" ");
		n.f2.accept(this);
		add("\n\n");
		this.indentation++;
		n.f3.accept(this);
		this.indentation--;
		add("\n\n");
		n.f4.accept(this);
	}

	@Override
	public void visit(MainFunction n) {
		ind();
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		add(" ");
		n.f2.accept(this);
		add(" ");
		n.f3.accept(this);
		n.f4.accept(this);
		n.f5.accept(this);
		add(" ");
		n.f6.accept(this);
		n.f7.accept(this);
		add(" ");
		n.f8.accept(this);
		this.indentation++;
		add("\n");
		n.f9.accept(this);
		this.indentation--;
		add("\n");
		ind();
		n.f10.accept(this);
	}
	
	@Override
	public void visit(StringArrayType n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration)
	 */
	@Override
	public void visit(ClassDeclaration n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		add(" ");
		
		// extends <Identifier>
		if (n.f2.present()){
			this.addNewLineBetweenNodes.push(false);
			this.additionalSeparation.push(" ");
			n.f2.accept(this);
			this.additionalSeparation.pop();
			this.addNewLineBetweenNodes.pop();
			add(" ");
		}
		
		n.f3.accept(this);
		add("\n");
		this.indentation++;
		
		n.f4.accept(this);
		if (n.f4.present())
			add("\n");
		add("\n");
		
		this.additionalSeparation.push("\n");
		n.f5.accept(this);
		this.additionalSeparation.pop();
		
		add("\n\n");
		this.indentation--;
		n.f6.accept(this);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.VarDeclaration)
	 */
	@Override
	public void visit(VarDeclaration n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		n.f2.accept(this);
	}

	private void add(String string) {
		this.currentString.append(string);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration)
	 */
	@Override
	public void visit(MethodDeclaration n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		add(" ");
		n.f2.accept(this);
		n.f3.accept(this);
		this.addNewLineBetweenNodes.push(false);
		n.f4.accept(this);
		this.addNewLineBetweenNodes.pop();
		n.f5.accept(this);
		add(" ");
		n.f6.accept(this);
		add("\n");
		this.indentation++;
		n.f7.accept(this);
		// if there are variable declarations present separate them from
		// statements
		if (n.f7.present())
			add("\n\n");
		n.f8.accept(this);
		if (n.f8.present())
			add("\n");
		ind();
		n.f9.accept(this);
		add(" ");
		n.f10.accept(this);
		n.f11.accept(this);
		add("\n");
		this.indentation--;
		ind();
		n.f12.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameterList)
	 */
	@Override
	public void visit(FormalParameterList n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameter)
	 */
	@Override
	public void visit(FormalParameter n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreFormalParameter)
	 */
	@Override
	public void visit(MoreFormalParameter n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Type)
	 */
	@Override
	public void visit(Type n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.VoidType)
	 */
	@Override
	public void visit(VoidType n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayType)
	 */
	@Override
	public void visit(ArrayType n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.BooleanType)
	 */
	@Override
	public void visit(BooleanType n) {
		add("bool");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerType)
	 */
	@Override
	public void visit(IntegerType n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Statement)
	 */
	@Override
	public void visit(Statement n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Block)
	 */
	@Override
	public void visit(Block n) {
		n.f0.accept(this);
		add("\n");
		this.indentation++;
		n.f1.accept(this);
		this.indentation--;
		add("\n");
		n.f2.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.AssignmentStatement)
	 */
	@Override
	public void visit(AssignmentStatement n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		add(" ");
		n.f2.accept(this);
		n.f3.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAssignmentStatement)
	 */
	@Override
	public void visit(ArrayAssignmentStatement n) {
		n.f0.accept(this);
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		add(" ");
		n.f4.accept(this);
		add(" ");
		n.f5.accept(this);
		n.f6.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.IfStatement)
	 */
	@Override
	public void visit(IfStatement n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		add(" ");
		n.f4.accept(this);
		add(" ");
		n.f5.accept(this);
		add(" ");
		n.f6.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.WhileStatement)
	 */
	@Override
	public void visit(WhileStatement n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		add(" ");
		n.f4.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.PrintStatement)
	 */
	@Override
	public void visit(PrintStatement n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression)
	 */
	@Override
	public void visit(Expression n) {
		this.additionalSeparation.push("");
		n.f0.accept(this);
		this.additionalSeparation.pop();
		if (n.f1.present())
			add(" ");
		this.additionalSeparation.push(" ");
		this.addNewLineBetweenNodes.push(false);
		n.f1.accept(this);
		this.addNewLineBetweenNodes.pop();
		this.additionalSeparation.pop();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Term)
	 */
	@Override
	public void visit(Term n) {
		n.f0.accept(this);
		if (n.f1.present())
			add(" ");
		this.additionalSeparation.push(" ");
		this.addNewLineBetweenNodes.push(false);
		n.f1.accept(this);
		this.addNewLineBetweenNodes.pop();
		this.additionalSeparation.pop();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum)
	 */
	@Override
	public void visit(Sum n) {
		n.f0.accept(this);
		if (n.f1.present())
			add(" ");
		this.additionalSeparation.push(" ");
		this.addNewLineBetweenNodes.push(false);
		n.f1.accept(this);
		this.addNewLineBetweenNodes.pop();
		this.additionalSeparation.pop();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Factor)
	 */
	@Override
	public void visit(Factor n) {
		this.addNewLineBetweenNodes.push(false);
		super.visit(n);
		this.addNewLineBetweenNodes.pop();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.F)
	 */
	@Override
	public void visit(F n) {
		this.addNewLineBetweenNodes.push(false);
		super.visit(n);
		this.addNewLineBetweenNodes.pop();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp)
	 */
	@Override
	public void visit(ArrayLookUp n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.LengthLookUp)
	 */
	@Override
	public void visit(LengthLookUp n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodCall)
	 */
	@Override
	public void visit(MethodCall n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ParamList)
	 */
	@Override
	public void visit(ParamList n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreParameterExpression)
	 */
	@Override
	public void visit(MoreParameterExpression n) {
		add(" ");
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.PrimaryExpression)
	 */
	@Override
	public void visit(PrimaryExpression n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral)
	 */
	@Override
	public void visit(IntegerLiteral n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral)
	 */
	@Override
	public void visit(TrueLiteral n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral)
	 */
	@Override
	public void visit(FalseLiteral n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier)
	 */
	@Override
	public void visit(Identifier n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ThisExpression)
	 */
	@Override
	public void visit(ThisExpression n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression)
	 */
	@Override
	public void visit(ArrayAllocationExpression n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
		n.f4.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.AllocationExpression)
	 */
	@Override
	public void visit(AllocationExpression n) {
		n.f0.accept(this);
		add(" ");
		n.f1.accept(this);
		n.f2.accept(this);
		n.f3.accept(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.NotExpression)
	 */
	@Override
	public void visit(NotExpression n) {
		super.visit(n);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor#visit(ch.unibe.iam.scg.minijava.ast.syntaxtree.BracketExpression)
	 */
	@Override
	public void visit(BracketExpression n) {
		super.visit(n);
	}

}
