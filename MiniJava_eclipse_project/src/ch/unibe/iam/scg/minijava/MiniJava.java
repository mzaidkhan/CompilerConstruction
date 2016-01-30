package ch.unibe.iam.scg.minijava;

import ch.unibe.iam.scg.javacc.ParseException;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;

/**
 * Please, do not change this interface. Let me know, if you need to change it
 * by sending email to cc-staff@iam.unibe.ch
 * 
 * @author kursjan
 */
public interface MiniJava {
	
	public Node ClassDeclaration() throws ParseException;
	
	public Node Expression() throws ParseException;
	
	public Node Goal() throws ParseException;
	
	public Node Identifier() throws ParseException;
	
	public Node MainClass() throws ParseException;
	
	public Node MethodDeclaration() throws ParseException;
	
	public Node Statement() throws ParseException;
	
	public Node Type() throws ParseException;
	
	public Node VarDeclaration() throws ParseException;
}
