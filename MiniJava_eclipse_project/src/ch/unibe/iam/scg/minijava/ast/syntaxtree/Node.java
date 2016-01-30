//
// Generated by JTB 1.3.2
//

package ch.unibe.iam.scg.minijava.ast.syntaxtree;

import ch.unibe.iam.scg.minijava.PrettyPrinter;

/**
 * The interface which all syntax tree classes must implement.
 */
public abstract class Node implements java.io.Serializable {
   public abstract void accept(ch.unibe.iam.scg.minijava.ast.visitor.Visitor v);
   public abstract <R,A> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVisitor<R,A> v, A argu);
   public abstract <R> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJNoArguVisitor<R> v);
   public abstract <A> void accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVoidVisitor<A> v, A argu);
   
   @Override
	public String toString() {
	   PrettyPrinter p = new PrettyPrinter();
	   return p.prettyPrint(this);
	}
}

