//
// Generated by JTB 1.3.2
//

package ch.unibe.iam.scg.minijava.ast.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * f0 -> MainClass()
 * f1 -> ( ClassDeclaration() )*
 * f2 -> &lt;EOF&gt;
 * </PRE>
 */
public class Goal extends Node {
   public MainClass f0;
   public NodeListOptional f1;
   public NodeToken f2;

   public Goal(MainClass n0, NodeListOptional n1, NodeToken n2) {
      f0 = n0;
      f1 = n1;
      f2 = n2;
   }

   public Goal(MainClass n0, NodeListOptional n1) {
      f0 = n0;
      f1 = n1;
      f2 = new NodeToken("");
   }

   public void accept(ch.unibe.iam.scg.minijava.ast.visitor.Visitor v) {
      v.visit(this);
   }
   public <R,A> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVisitor<R,A> v, A argu) {
      return v.visit(this,argu);
   }
   public <R> R accept(ch.unibe.iam.scg.minijava.ast.visitor.GJNoArguVisitor<R> v) {
      return v.visit(this);
   }
   public <A> void accept(ch.unibe.iam.scg.minijava.ast.visitor.GJVoidVisitor<A> v, A argu) {
      v.visit(this,argu);
   }
}
