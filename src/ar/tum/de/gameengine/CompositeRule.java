package ar.tum.de.gameengine;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Composite rule used for combining many rules in one.<br>
 * Use builder for creating CompositeRule.
 * Composite rule considered to be affected if any of its rules affected and
 * considered to be completed if any of its rules completed.
 * 
 */
public class CompositeRule implements GameRule {

	private Set<GameRule> rules;
	private boolean isCompleted;
	
	private CompositeRule() {
		rules = new HashSet<GameRule>();
	}

	@Override
	public boolean affectedBy(GameAction action) {
		if (isCompleted) throw new IllegalStateException("Rule is completed");
		
		boolean result = false;
		for (GameRule rule: rules){
			boolean affected = rule.affectedBy(action);
			if (affected){
				isCompleted = rule.isCompleted();
				result = true;
			}
		}
		
		return result;
	}

	@Override
	public boolean isCompleted() {
		return isCompleted;
	}

	public static class Builder {
		
		private CompositeRule compositeRule;
		
		public Builder() {
			compositeRule = new CompositeRule();
		}
		
		public Builder addRule(GameRule rule) {
			compositeRule.rules.add(rule);
			return this;
		}
		
		public CompositeRule build(){
			compositeRule.rules = Collections.unmodifiableSet(compositeRule.rules);
			return compositeRule;
		}
	}
}
