package ch.unibe.iam.scg.minijava.typesys;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ErrorReporting {
	
	public List<String> msgs;
	
	public ErrorReporting() {
		this.msgs=new ArrayList<String>();
	}
	
	public void addError(String msg) {
		this.msgs.add(msg);
	}
	
	public boolean hasFoundErrors() {
		return !this.msgs.isEmpty();
	}

	public void reportTo(PrintStream out) {
		for(String msg:msgs) {
			out.println(msg);
		}
	}
	
	public String toString() {
		if (msgs.size()==0)
			return "";
		String result="";
		for(String msg:msgs)
			result+=msg+"\n\n";
		return result.substring(0,result.length()-2);
	}
	
}
