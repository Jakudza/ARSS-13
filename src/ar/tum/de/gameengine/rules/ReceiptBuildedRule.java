package ar.tum.de.gameengine.rules;

import ar.de.tum.gamelogic.Receipt;
import ar.tum.de.gameengine.GameAction;
import ar.tum.de.gameengine.GameRule;


public class ReceiptBuildedRule implements GameRule {

	private Receipt receipt;

	public ReceiptBuildedRule(Receipt receipt) {
		this.receipt = receipt;
	}

	Receipt getReceipt(){
		return receipt;
	}
	
	@Override
	public boolean isCompleted() {
		return receipt.getFruits().isEmpty() && receipt.getBottles().isEmpty();
	}

	@Override
	public boolean affectedBy(GameAction action) throws IllegalStateException {
		return action.applyRule(this);
	}
}
