package ar.tum.de.gameengine.rules;

import ar.tum.de.gameengine.GameAction;
import ar.tum.de.gameengine.GameRule;

public class OutOfTimeRule implements GameRule {

	private float timeCredit;
	
	/**
	 * 
	 * @param initialAmountOfTime in milliseconds
	 */
	public OutOfTimeRule(int initialAmountOfTime) {
		timeCredit = initialAmountOfTime / 1000;
	}
	
	protected void addToCredit(float amountOfTime) {
		timeCredit += amountOfTime;
	}

	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		return action.applyRule(this);
	}

	@Override
	public boolean isCompleted() {
		return (timeCredit <= 0);
	}
	
	protected void takeAwayTime(float amountOfTime) {
		timeCredit -= amountOfTime;
	}
	
	/**
	 * 
	 * @return milliseconds left
	 */
	public int getTimeCredit() {
		return (int) (timeCredit * 1000);
	}
}
