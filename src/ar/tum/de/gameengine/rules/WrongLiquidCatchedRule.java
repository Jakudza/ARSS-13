package ar.tum.de.gameengine.rules;

import ar.tum.de.gameengine.GameAction;

public class WrongLiquidCatchedRule extends MarginValueRule {

	public WrongLiquidCatchedRule(int marginValue) {
		super(marginValue);
	}

	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		return action.applyRule(this);
	}
	
}
