package ar.tum.de.gameengine.rules;

import ar.tum.de.gameengine.GameAction;
import ar.tum.de.gameengine.GameRule;

public class SpoiledRecipeRule implements GameRule{
	
	public interface FailListener {
		
		void onFail(int numberOfFails, int maxFails);
	}

	private final int maxFails;
	private final float ignoreFailTime;
	private boolean ignoreFail;
	private int numberOfFails;
	private float timeTrack;
	private FailListener listener;
	
	/**
	 * 
	 * @param maxFails - maximum number of fails
	 * @param ignoreFailTime - during this time consecutive fails will be ignored
	 */
	public SpoiledRecipeRule(int maxFails, float ignoreFailTime) {
		this.maxFails = maxFails;
		this.ignoreFailTime = ignoreFailTime;
	}
	
	public void setFailListener(FailListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		boolean affected = action.applyRule(this) && !ignoreFail;
		if (affected) {
			ignoreFail = true;
			numberOfFails++;
			if (listener != null) listener.onFail(numberOfFails, maxFails);
		}
		return affected;
	}

	@Override
	public boolean isCompleted() {
		return numberOfFails > maxFails;
	}
	
	protected void update(float time) {
		if (ignoreFail) { 
			timeTrack += time;
			if (timeTrack > ignoreFailTime) {
				timeTrack = 0;
				ignoreFail = false;
			}
		}
	}
}
