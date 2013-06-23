package ar.tum.de.gameengine;

/**
 * Count rule. Considered to be completed when underlying rule was affected by
 * certain number of actions.
 *
 */
public class CountRule implements GameRule {
	
	private final int marginCount;
	private final GameRule underlyingRule;
	private int count;
	
	public CountRule(GameRule underlyingRule, int marginCount){
		if (marginCount < 1) throw new IllegalArgumentException("Margin count should be greater than 0");
		if (underlyingRule == null) throw new NullPointerException();
		
		this.marginCount = marginCount;
		this.underlyingRule = underlyingRule;
	}
	
	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		boolean affected = underlyingRule.affectedBy(action);
		if (affected) count++;
		return affected;
	}
	
	@Override
	public boolean isCompleted() {
		return count >= marginCount;
	}
}
