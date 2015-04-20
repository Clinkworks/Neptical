package com.clinkworks.neptical.junit.statements;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.google.common.collect.Lists;
import com.google.inject.Injector;

public class FrameworkMethodsWrapper extends Statement{
	
	private final Statement fNext;
	private final List<Statement> fBeforeStatements;
	
	public FrameworkMethodsWrapper(Injector injector, Statement next, List<FrameworkMethod> beforeMethods, Object testContext) {
		fNext = next;
		fBeforeStatements = Lists.newArrayList();
		
		for(FrameworkMethod beforeMethod : beforeMethods){
			fBeforeStatements.add(new FrameworkMethodWrapper(injector, beforeMethod, testContext));
		}
	}
	
	@Override
	public void evaluate() throws Throwable {
		for(Statement statment : fBeforeStatements){
			statment.evaluate();
		}
		fNext.evaluate();
	}

}
