package ar.tum.de.gameengine.rules;

import ar.tum.de.gameengine.GameRule;

/**
 * Margin value rule considered to be completed when value exceeds margin. <br>
 * Relies on actions to modify currentValue.
 * 
 */
public abstract class MarginValueRule implements GameRule {

	protected int currentValue;
	private int marginValue;
	
	public MarginValueRule(int marginValue) {
		this.marginValue = marginValue;
	}

	@Override
	public boolean isCompleted() {
		return currentValue >= marginValue;
	}
}
