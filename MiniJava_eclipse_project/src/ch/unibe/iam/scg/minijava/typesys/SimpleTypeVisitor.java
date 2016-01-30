package ch.unibe.iam.scg.minijava.typesys;

import java.lang.reflect.Field;
import java.util.ArrayList;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.AllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.BooleanType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameter;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameterList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.LengthLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreParameterExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ParamList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.StringArrayType;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Type;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VoidType;
import ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor;

public class SimpleTypeVisitor extends DepthFirstVisitor {
	protected VarType currentType;
	private String currentIdentifier;
	protected ArrayList<VarType> parameters;
	protected ArrayList<String> paramNames;
	
	public SimpleTypeVisitor() {
		this.currentType=null;
		this.currentIdentifier=null;
		this.parameters=new ArrayList<VarType>();
		this.paramNames=new ArrayList<String>();
	}

	public VarType getType(Node n) {
		n.accept(this);
		return this.currentType;
	}
	
	/**
	 * <b>Note:</b> make sure node only contains one identifier, otherwise only the last identifier will be passed
	 * @param n
	 * @return
	 */
	public String getIdentifier(Node n) {
		if (n instanceof Identifier){
			this.currentIdentifier=((Identifier)n).f0.tokenImage;
			return this.currentIdentifier;
		}
		n.accept(this);
		return this.currentIdentifier;
	}
	
	/**
	 * <b>Note:</b> make sure node only contains one FormalParameterList, otherwise only the last FormalParameterList will be passed
	 * @param n
	 * @return
	 */
	public VarType[] getParameterTypes(Node n) {
		SimpleTypeVisitor s = new SimpleTypeVisitor();
		n.accept(s);
		return s.parameters.toArray(new VarType[]{});
	}
	
	/**
	 * <b>Note:</b> make sure node only contains one FormalParameterList, otherwise only the last FormalParameterList will be passed
	 * @param n
	 * @return
	 */
	public String[] getParameterNames(Node n) {
		SimpleTypeVisitor s = new SimpleTypeVisitor();
		n.accept(s);
		return s.paramNames.toArray(new String[]{});
	}
	
	@Deprecated
	public void tryToVisitTokens(Node n,int min,int max) {
		for(int cur=min;cur<=max;cur++){
			try {
				Field f=n.getClass().getDeclaredField("f"+cur);
				Node node = (Node)f.get(n);
				node.accept(this);
			} catch (Exception e) {
				System.out.println("could not visit f"+cur);
			}
			
		}
	}
	
	
	@Override
	public void visit(Type n) {
		this.currentType=null;
		n.f0.choice.accept((SimpleTypeVisitor)this);
		if (this.currentType==null) {
			this.currentType=VarType.object(currentIdentifier);
		}
	}
	
	@Override
	public void visit(FormalParameterList n) {
		this.parameters.clear();
		this.paramNames.clear();
		super.visit(n);
	}
	
	@Override
	public void visit(FormalParameter n) {
		this.parameters.add(getType(n.f0));
		this.paramNames.add(n.f1.f0.tokenImage);
	}
	
	@Override
	public void visit(ParamList n) {
		if (n.f0.present()){
			NodeSequence s = (NodeSequence)n.f0.node;
			Node expr = s.elementAt(0);
			Node rest=s.elementAt(1);
			expr.accept(this);
			this.parameters.add(this.currentType);
			rest.accept(this);
		}
	}
	
	@Override
	public void visit(MoreParameterExpression n) {
		n.f0.accept(this);
		this.parameters.add(this.currentType);
	}

	@Override
	public void visit(TrueLiteral n) {
		this.currentType=VarType.bool();
	}
	
	@Override
	public void visit(FalseLiteral n) {
		this.currentType=VarType.bool();
	}
	
	@Override
	public void visit(IntegerLiteral n) {
		this.currentType=VarType.integer();
	}
	
	@Override
	public void visit(AllocationExpression n) {
		this.currentType=VarType.object(n.f1.f0.tokenImage);
	}
	
	@Override
	public void visit(ArrayAllocationExpression n) {
		this.currentType=VarType.intArray();
	}
	
	@Override
	public void visit(LengthLookUp n) {
		this.currentType=VarType.integer();
	}
	
	@Override
	public void visit(BooleanType n) {
		this.currentType=VarType.bool();
	}
	
	@Override
	public void visit(IntegerType n) {
		this.currentType=VarType.integer();
	}
	
	@Override
	public void visit(VoidType n) {
		this.currentType=VarType.voidT();
	}
	
	@Override
	public void visit(ArrayType n) {
		this.currentType=VarType.intArray();
	}
	
	@Override
	public void visit(StringArrayType n) {
		this.currentType=VarType.stringArray();
	}
	
	@Override
	public void visit(Identifier n) {
		this.currentIdentifier=n.f0.tokenImage;
	}
}
