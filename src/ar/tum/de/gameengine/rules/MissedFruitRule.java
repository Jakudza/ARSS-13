package ar.tum.de.gameengine.rules;

import ar.tum.de.gameengine.GameAction;
import ar.tum.de.gameengine.GameRule;

public class MissedFruitRule implements GameRule {

	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		return action.applyRule(this);
	}

	@Override
	public boolean isCompleted() {
		return false;
	}
}
