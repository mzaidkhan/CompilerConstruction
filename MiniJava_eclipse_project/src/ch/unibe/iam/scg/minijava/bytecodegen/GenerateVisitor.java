package ch.unibe.iam.scg.minijava.bytecodegen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ARRAYLENGTH;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DUP;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IALOAD;
import org.apache.bcel.generic.IAND;
import org.apache.bcel.generic.IASTORE;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.IMUL;
import org.apache.bcel.generic.ISHL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.NEWARRAY;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.bcel.generic.Type;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.AllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.AssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameter;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameterList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IfStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.LengthLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodCall;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreParameterExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeChoice;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NotExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ParamList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.PrintStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Term;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ThisExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VarDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.WhileStatement;
import ch.unibe.iam.scg.minijava.optimizer.ConstantDetectorVisitor;
import ch.unibe.iam.scg.minijava.optimizer.EvaluationVisitor;
import ch.unibe.iam.scg.minijava.typesys.CheckTypesVisitor;
import ch.unibe.iam.scg.minijava.typesys.ClassScope;
import ch.unibe.iam.scg.minijava.typesys.ErrorReporting;
import ch.unibe.iam.scg.minijava.typesys.GlobalScope;
import ch.unibe.iam.scg.minijava.typesys.MethodScope;
import ch.unibe.iam.scg.minijava.typesys.Scope;
import ch.unibe.iam.scg.minijava.typesys.VarObjectType;
import ch.unibe.iam.scg.minijava.typesys.VarType;

/**
 * @author j asdf
 */
public class GenerateVisitor extends TrackExpressionTypesVisitor {
	private InstructionList il;
	private ClassGen cg;
	private ConstantPoolGen cp;
	private MethodGen mg;
	private ArrayList<Type> argTypes;
	private ArrayList<String> argNames;
	private Stack<Boolean> pushIdentifierToStack;
	private HashMap<String, Integer> variableToRegisterMap;
	private int nextVariableRegister;
	private ArrayList<VarType> paramListTypes;
	private Stack<Boolean> insideMethod;

	public GenerateVisitor(ClassGen cg, GlobalScope g) {
		super(g);
		this.il = new InstructionList();
		this.cg = cg;
		this.cp = new ConstantPoolGen();
		this.pushIdentifierToStack = new Stack<Boolean>();
		this.pushIdentifierToStack.push(false);
		this.variableToRegisterMap = new HashMap<String, Integer>();
		this.nextVariableRegister = 1; // 0 is reserved for this

		this.insideMethod = new Stack<Boolean>();
		this.insideMethod.push(false);
	}

	public int getRegisterForVar(String var) {
		Integer i = this.variableToRegisterMap.get(var);
		if (i == null) {
			this.variableToRegisterMap.put(var, this.nextVariableRegister);
			i = this.nextVariableRegister;
			this.nextVariableRegister++;
		}
		return i;
	}

	public MethodGen getMethodGenerator() {
		return this.mg;
	}

	@Override
	public void visit(ThisExpression n) {
		super.visit(n);
		// put this on stack
		il.append(new ALOAD(0));
	}

	@Override
	public void visit(MethodCall n) {
		String methodName = n.f1.f0.tokenImage;

		InstructionFactory factory = new InstructionFactory(this.cg);
		ClassScope classScope = getClassScopeFromType(this.currentType);

		// Get Parameter types (see visit(ParamList and MoreParameterExpression)
		// puts them on stack as well
		n.f3.accept(this);

		MethodScope m = classScope.findMethod(methodName,
				this.paramListTypes.toArray(new VarType[] {}));
		Type returnType = convertVarTypeToType(m.returntype);
		Type[] params = new Type[this.paramListTypes.size()];
		for (int i = 0; i < this.paramListTypes.size(); i++)
			params[i] = convertVarTypeToType(this.paramListTypes.get(i));

		il.append(factory.createInvoke(classScope.name, methodName, returnType,
				params, Constants.INVOKEVIRTUAL));

		this.currentType = m.returntype;
	}

	@Override
	public void visit(ParamList n) {
		this.paramListTypes = new ArrayList<VarType>();
		if (n.f0.present()) {
			NodeSequence n1 = (NodeSequence) n.f0.node;
			Node expr = n1.elementAt(0);
			Node rest = n1.elementAt(1);

			expr.accept(this);
			this.paramListTypes.add(getTypeForNode(expr));
			rest.accept(this);
		}
	}

	@Override
	public void visit(MoreParameterExpression n) {
		n.f0.accept(this);
		this.paramListTypes.add(getTypeForNode(n.f0));
	}

	public VarType getTypeForNode(Node n) {
		Scope s = this.currentScope.peek();
		CheckTypesVisitor cv = new CheckTypesVisitor(s, new ErrorReporting());
		n.accept(cv);
		return cv.getCurrentType();
	}

	@Override
	public void visit(MethodDeclaration n) {
		this.insideMethod.push(true);
		// if (n.f0.tokenImage.equals("public")) //public by default
		int access_flag = Constants.ACC_PUBLIC;

		Type returnType = convertNodeToType(n.f1.f0.choice);
		String methodName = n.f2.f0.tokenImage;
		this.argTypes = new ArrayList<Type>();
		this.argNames = new ArrayList<String>();

		String className = cg.getClassName();

		this.il = new InstructionList();
		this.cp = this.cg.getConstantPool();

		super.visit(n);

		il.append(InstructionFactory.createReturn(returnType));

		this.mg = new MethodGen(access_flag, returnType,
				argTypes.toArray(new Type[] {}),
				argNames.toArray(new String[] {}), methodName, className, il,
				cp);

		this.mg.setMaxStack();
		this.mg.setMaxLocals();
		this.cg.addMethod(this.mg.getMethod());
		this.insideMethod.pop();
	}

	@Override
	public void visit(Identifier n) {
		super.visit(n);
		if (this.pushIdentifierToStack.peek()) {
			String varName = n.f0.tokenImage;

			VarType t = super.lookUpType(varName);
			VariableLevel level = super.lookUpVarLevel();
			Type var = convertVarTypeToType(t);
			String varSignature = var.getSignature();

			if (level.equals(VariableLevel.local)) {
				int register = getRegisterForVar(n.f0.tokenImage);
				if (t.equals(VarType.integer()))
					this.il.append(new ILOAD(register));
				else if (t.equals(VarType.bool()))
					this.il.append(new ILOAD(register));
				else if (t.equals(VarType.intArray()))
					this.il.append(new ALOAD(register));
				else if (t.equals(VarType.stringArray()))
					this.il.append(new ALOAD(register));
				else if (t.s.equals(VarType.SimpleType.OBJECT))
					this.il.append(new ALOAD(register));
			} else if (level.equals(VariableLevel.instanceVar)) {

				this.il.append(new ALOAD(0));
				ConstantPoolGen cpg = cg.getConstantPool();
				int index = cpg.lookupFieldref(cg.getClassName(), varName,
						varSignature);
				il.append(new GETFIELD(index));

			} else {
				throw new RuntimeException("global vars not implemented");
			}
		}
	}

	@Override
	public void visit(FormalParameterList n) {
		this.nextVariableRegister = 1;
		super.visit(n);
	}

	@Override
	public void visit(FormalParameter n) {
		super.visit(n);
		Type type = convertNodeToType(n.f0);
		String argName = n.f1.f0.tokenImage;

		this.argTypes.add(type);
		this.argNames.add(argName);
		getRegisterForVar(argName);

		Scope s = this.currentScope.peek();
		try {
			s.addVariable(argName, convertTypeToVarType(type, argName));
		} catch (Exception e) {
			// not needed, only for typechecking this exception is relevant
			// e.printStackTrace();
		}
	}

	protected Type convertVarTypeToType(VarType type) {
		if (type.equals(VarType.integer()))
			return Type.INT;
		else if (type.equals(VarType.bool()))
			return Type.BOOLEAN;
		else if (type.equals(VarType.voidT()))
			return Type.VOID;
		else if (type.equals(VarType.intArray()))
			return ArrayType.INT;
		else if (type.equals(VarType.stringArray()))
			return ArrayType.STRING;
		else {
			VarObjectType o = (VarObjectType) type;
			return new ObjectType(o.className);
		}
	}

	protected VarType convertTypeToVarType(Type type, String className) {
		if (Type.INT.equals(type))
			return VarType.integer();
		else if (Type.BOOLEAN.equals(type))
			return VarType.bool();
		else if (Type.VOID.equals(type))
			return VarType.voidT();
		else if (ArrayType.INT.equals(type))
			return VarType.intArray();
		// Dimension not relevant for now
		else if (ArrayType.STRING.equals(type))
			return VarType.stringArray();
		// Dimension not relevant for now
		else {
			return VarType.object(className);
		}
	}

	protected Type convertNodeToType(Node n) {
		String type = n.toString();
		if (type.equals("int"))
			return Type.INT;
		else if (type.equals("bool"))
			return Type.BOOLEAN;
		else if (type.equals("void"))
			return Type.VOID;
		else if (type.equals("String[]"))
			return new ArrayType(Type.STRING, 1);
		// Dimension not relevant for now
		else if (type.equals("int[]"))
			return new ArrayType(Type.INT, 1);
		// Dimension not relevant for now
		else {
			return new ObjectType(type);
		}
	}

	@Override
	public void visit(IntegerLiteral n) {
		super.visit(n);
		il.append(new PUSH(cp, Integer.valueOf(n.f0.tokenImage)));
	}

	// XXX: Node order of evaluation is wrong since right Expression is
	// evaluated before the subtraction (
	@Override
	public void visit(Sum n) {
		super.visit(n);
		if (n.f1.present()) {
			String op = getFirstOpTokenFromNodeSeq(n.f1.node);
			if (op.equals("+"))
				il.append(InstructionFactory.IADD);
			else if (op.equals("-"))
				il.append(InstructionFactory.ISUB);
		}
	}

	@Override
	public void visit(TrueLiteral n) {
		super.visit(n);
		il.append(new PUSH(cp, true));
	}

	@Override
	public void visit(FalseLiteral n) {
		super.visit(n);
		il.append(new PUSH(cp, false));
	}

	@Override
	public void visit(AssignmentStatement n) {
		String varName = n.f0.f0.tokenImage;
		VarType t = super.lookUpType(varName);
		VariableLevel level = super.lookUpVarLevel();
		Type var = convertVarTypeToType(t);
		String varSignature = var.getSignature();

		if (level.equals(VariableLevel.local)) { // local variables
			int register = getRegisterForVar(n.f0.f0.tokenImage);
			n.f2.accept(this);

			if (t.s.equals(VarType.SimpleType.INTEGER))
				this.il.append(new ISTORE(register));
			else if (t.s.equals(VarType.SimpleType.BOOLEAN))
				this.il.append(new ISTORE(register));
			else if (t.s.equals(VarType.SimpleType.INTEGER_ARRAY))
				this.il.append(new ASTORE(register));
			else if (t.s.equals(VarType.SimpleType.OBJECT))
				this.il.append(new ASTORE(register));
			else if (t.s.equals(VarType.SimpleType.STRING_ARRAY))
				this.il.append(new ASTORE(register));
		} else if (level.equals(VariableLevel.instanceVar)) { // instance
																// variables
			il.append(new ALOAD(0));
			n.f2.accept(this);

			ConstantPoolGen cpg = cg.getConstantPool();
			int index = cpg.addFieldref(cg.getClassName(), varName,
					varSignature);

			il.append(new PUTFIELD(index));
		} else {
			throw new RuntimeException("global Variables not implemented");
		}
	}

	@Override
	public void visit(VarDeclaration n) {
		super.visit(n);

		if (!this.insideMethod.peek()) {

			String varName = n.f1.f0.tokenImage;
			VarType t = super.lookUpType(varName);
			Type var = convertVarTypeToType(t);
			ConstantPoolGen cpg = cg.getConstantPool();
			cg.addField(new FieldGen(Constants.ACC_PUBLIC, var, varName, cpg)
					.getField());
		}
	}

	@Override
	public void visit(AllocationExpression n) {
		super.visit(n);

		String className = n.f1.f0.tokenImage;
		ObjectType classType = new ObjectType(className);
		InstructionFactory factory = new InstructionFactory(this.cg);
		il.append(factory.createNew(classType));
		il.append(new DUP());
		il.append(factory.createInvoke(className, "<init>", Type.VOID,
				new Type[] {}, Constants.INVOKESPECIAL));
	}

	@Override
	public void visit(Expression n) {
		this.pushIdentifierToStack.push(true);
		super.visit(n);
		this.pushIdentifierToStack.pop();
		if (n.f1.present()) {
			String op = getFirstOpTokenFromNodeSeq(n.f1.node);
			if (op.equals("&&"))
				il.append(new IAND());
			else if (op.equals("<")) {
				il.append(InstructionFactory.ISUB);
				InstructionHandle blockIf = il.append(new PUSH(cp, false));
				InstructionHandle blockElse = il.append(new PUSH(cp, true));
				InstructionHandle endif = il.append(new NOP());
				il.insert(blockIf, new IFLT(blockElse));
				il.insert(blockElse, new GOTO(endif));
			} else if (op.equals(">")) {
				il.append(InstructionFactory.ISUB);
				InstructionHandle blockIf = il.append(new PUSH(cp, false));
				InstructionHandle blockElse = il.append(new PUSH(cp, true));
				InstructionHandle endif = il.append(new NOP());
				il.insert(blockIf, new IFGT(blockElse));
				il.insert(blockElse, new GOTO(endif));
			}
		}
	}

	/**
	 * since no bytecode for the boolean-not operation exists, this is rather
	 * difficult:<br>
	 * if (boolean) return false;<br>
	 * else return true;<br>
	 * see:
	 * http://stackoverflow.com/questions/12886628/generating-jvm-bytecode-for
	 * -a-unary-not-expression
	 */
	@Override
	public void visit(NotExpression n) {
		super.visit(n);

		InstructionHandle blockIf = il.append(new PUSH(cp, false));
		InstructionHandle blockElse = il.append(new PUSH(cp, true));
		InstructionHandle endif = il.append(new NOP());

		il.insert(blockIf, new IFEQ(blockElse));
		il.insert(blockElse, new GOTO(endif));
	}

	@Override
	public void visit(Term n) {
		
		boolean done=false;
		if (n.f1.present()) {
			ConstantDetectorVisitor c1 = new ConstantDetectorVisitor();
			n.f0.accept(c1);
			ConstantDetectorVisitor c2 = new ConstantDetectorVisitor();
			n.f1.accept(c2);
			if (c1.g() || c2.g()) {
				EvaluationVisitor e = new EvaluationVisitor();
				Node rest;
				if (c1.g()) {
					e.evaluate("Factor", n.f0);
					rest=n.f1;
				}
				else{
					e.evaluate("Expression", n.f1);
					rest=n.f0;
				}
				int value = (Integer) e.getValue();
				int shiftL=(int) Math.round(Math.log(value)/Math.log(2));
				
				if (Math.pow(2, shiftL) == value) {
					rest.accept(this);
					il.append(new PUSH(cp,shiftL));
					this.il.append(new ISHL());
					done=true;
				}
			}
		}

		if (!done) {
			super.visit(n);
			if (n.f1.present()) {
				il.append(new IMUL());
			}
		}
	}

	/**
	 * @param node
	 * @return fetch operator String from a nodeSequence<br>
	 *         e.g. (("+" | "-") Expression()) will return "+" or "-"
	 */
	public String getFirstOpTokenFromNodeSeq(Node node) {
		NodeSequence n = (NodeSequence) node;
		// fetch nodeChoice: ("+" | "-")
		NodeChoice operator = (NodeChoice) n.nodes.elementAt(0);
		NodeToken opToken = (NodeToken) operator.choice;
		return opToken.tokenImage;
	}

	public InstructionList getInstructionList() {
		return il;
	}

	@Override
	public void visit(ArrayAllocationExpression n) {
		super.visit(n);
		n.f3.accept(this);
		il.append(new NEWARRAY(Type.INT));
	}

	@Override
	public void visit(ClassDeclaration n) {
		String className = n.f1.f0.tokenImage;
		String superClass = "java.lang.Object";
		if (n.f2.present()) { // extends some class
			// XXX: untested
			NodeSequence list = (NodeSequence) n.f2.node;
			Identifier i = (Identifier) list.nodes.get(1);
			superClass = i.f0.tokenImage;
		}

		this.cg = new ClassGen(className, superClass, className,
				Constants.ACC_PUBLIC, new String[0]);
		this.cg.addEmptyConstructor(Constants.ACC_PUBLIC);

		super.visit(n);

		JavaClass jclass = this.cg.getJavaClass();
		try {
			jclass.dump("bin/" + cg.getClassName().replaceAll("\\.", "/")
					+ ".class");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(LengthLookUp n) {
		super.visit(n);
		il.append(new ARRAYLENGTH());
	}

	@Override
	public void visit(ArrayLookUp n) {
		super.visit(n);
		il.append(new IALOAD());
	}

	@Override
	public void visit(WhileStatement n) {
		// while expression
		InstructionHandle startCondition = il.getEnd();
		if (startCondition == null) {
			startCondition = il.append(new NOP());
		}
		n.f2.accept(this);
		startCondition = startCondition.getNext();
		InstructionHandle endOfCondition = il.getEnd();

		// statement block
		n.f4.accept(this);

		il.append(new GOTO(startCondition));
		InstructionHandle endOfWhile = il.append(new NOP());

		il.append(endOfCondition, new IFEQ(endOfWhile));
	}

	@Override
	public void visit(PrintStatement n) {
		ObjectType p_stream = new ObjectType("java.io.PrintStream");
		InstructionFactory factory = new InstructionFactory(this.cg);
		il.append(factory.createFieldAccess("java.lang.System", "out",
				p_stream, Constants.GETSTATIC));
		n.f2.accept(this);
		il.append(factory.createInvoke("java.io.PrintStream", "println",
				Type.VOID, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));
	}

	@Override
	public void visit(IfStatement n) {
		// condition
		n.f2.accept(this);
		InstructionHandle endOfCondition = il.getEnd();

		// if block
		n.f4.accept(this);
		InstructionHandle endOfIf = il.getEnd();

		// else block
		n.f6.accept(this);
		InstructionHandle startOfElse = endOfIf.getNext();
		InstructionHandle endOfElse = il.append(new NOP());

		// after if block jump over else block
		il.append(endOfIf, new GOTO(endOfElse));

		// after condition jump to else, if condition is false
		il.append(endOfCondition, new IFEQ(startOfElse));
	}

	@Override
	public void visit(ArrayAssignmentStatement n) {
		this.pushIdentifierToStack.push(true);
		n.f0.accept(this);
		this.pushIdentifierToStack.pop();
		n.f2.accept(this);
		n.f5.accept(this);
		il.append(new IASTORE());
	}

}
