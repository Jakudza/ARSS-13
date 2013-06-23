package ar.tum.de.gameengine;

import ar.tum.de.gameengine.rules.MissedFruitRule;
import ar.tum.de.gameengine.rules.MissedLiquidRule;
import ar.tum.de.gameengine.rules.OutOfTimeRule;
import ar.tum.de.gameengine.rules.ReceiptBuildedRule;
import ar.tum.de.gameengine.rules.SpoiledRecipeRule;
import ar.tum.de.gameengine.rules.WrongBottleOpenedRule;
import ar.tum.de.gameengine.rules.WrongFruitCatchedRule;
import ar.tum.de.gameengine.rules.WrongLiquidCatchedRule;

/**
 * Game action. 
 *
 */
public interface GameAction {

	boolean applyRule(ReceiptBuildedRule rule);

	boolean applyRule(MissedFruitRule rule);

	boolean applyRule(WrongFruitCatchedRule rule);

	boolean applyRule(WrongBottleOpenedRule rule);

	boolean applyRule(MissedLiquidRule rule);
	
	boolean applyRule(WrongLiquidCatchedRule rule);

	boolean applyRule(OutOfTimeRule rule);
	
	boolean applyRule(SpoiledRecipeRule rule);
}
