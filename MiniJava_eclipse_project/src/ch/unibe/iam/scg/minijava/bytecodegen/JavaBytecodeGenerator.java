package ch.unibe.iam.scg.minijava.bytecodegen;


import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.optimizer.ConstantDetectorVisitor;
import ch.unibe.iam.scg.minijava.typesys.BuildUpTypesVisitor;
import ch.unibe.iam.scg.minijava.typesys.ErrorReporting;
import ch.unibe.iam.scg.minijava.typesys.GlobalScope;


/**
 * Change at will! 
 * (http://funnyasduck.net/wp-content/uploads/2013/01/funny-star-trek-picard-tv-fire-will-pics.jpg)
 * 
 * @author kursjan
 *
 */
public class JavaBytecodeGenerator
{
    private InstructionList il;
    private ClassGen cg = createTemporaryClass();
    
    private MethodGen mg;

    public InstructionList getInstructionList()
    {
        return il;
    }
    
    public ClassGen getClassGen()
    {
        return cg;
    }
    
    public Method getMethod()
    {
        return mg.getMethod();
    }
    
    private static ClassGen createTemporaryClass()
    {
        String className = "BcelGenerated";
        ClassGen cg = new ClassGen(className, "java.lang.Object", className, Constants.ACC_PUBLIC, new String[0]);
        cg.addEmptyConstructor(Constants.ACC_PUBLIC);
    
        return cg;
    }

    public void generate(Object node)
    {
    	Node n=(Node)node;
    	//optimization
    	ConstantDetectorVisitor opti = new ConstantDetectorVisitor();
    	opti.optimize(n);
    	
		GlobalScope g = new GlobalScope();
		n.accept(new BuildUpTypesVisitor(g, new ErrorReporting()));
    	
    	GenerateVisitor generator = new GenerateVisitor(cg,g);
    	n.accept(generator);
    	this.mg=generator.getMethodGenerator();
    	this.il=generator.getInstructionList();
    }
}
