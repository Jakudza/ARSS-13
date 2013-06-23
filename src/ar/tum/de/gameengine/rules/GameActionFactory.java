package ar.tum.de.gameengine.rules;

import java.util.Iterator;
import java.util.List;

import ar.de.tum.gamelogic.Bottle;
import ar.de.tum.gamelogic.Fruit;
import ar.de.tum.gamelogic.Receipt.Ingredient;
import ar.tum.de.gameengine.GameAction;

public class GameActionFactory {
	
	public static GameAction createFruitCatchedAction(Fruit.Type fruitType, boolean inIngredients) {
		return new FruitCatchedAction(fruitType, inIngredients);
	}

	public static GameAction createFruitMissedAction(Fruit.Type fruitType, boolean inIngredients) {
		return new FruitMissedAction(fruitType, inIngredients);
	}
	
	public static GameAction createBottleOpenedAction(Bottle.Type bottleType, boolean inIngredients) {
		return new BottleOpenedAction(bottleType, inIngredients);
	}
	
	public static GameAction createLiquidMissedAction(Bottle.Type bottleType, boolean inIngredients, int millilitres) {
		return new LiquidMissedAction(bottleType, inIngredients, millilitres);
	}
	
	public static GameAction createLiquidCatchedAction(Bottle.Type bottleType, boolean inIngredients, int millilitres) {
		return new LiquidCatchedAction(bottleType, inIngredients, millilitres);
	}
	
	public static GameAction createTimePassed(float timePassed) { 
		return new TimePassedAction(timePassed);
	}

	/**
	 * Default action ignores all rules. <br>
	 * Simply override method in subclasses <i> applyRule</i> that you won't ignore.s 
	 * 
	 */
	static class DefaultAction implements GameAction {

		@Override
		public boolean applyRule(ReceiptBuildedRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(MissedFruitRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(WrongFruitCatchedRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(WrongBottleOpenedRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(MissedLiquidRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(WrongLiquidCatchedRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(OutOfTimeRule rule) {
			return false;
		}

		@Override
		public boolean applyRule(SpoiledRecipeRule rule) {
			return false;
		}
	}

	static abstract class FruitAction extends DefaultAction {

		protected Fruit.Type fruitType;
		
		protected boolean inIngredients;

		FruitAction(Fruit.Type fruitType, boolean inIngredients) {
			this.fruitType = fruitType;
			this.inIngredients = inIngredients;
		}
	}
	
	static abstract class BottleAction extends DefaultAction {
		
		protected Bottle.Type bottleType;
		
		protected boolean inIngredients;

		BottleAction(Bottle.Type bottleType, boolean inIngredients) {
			this.bottleType = bottleType;
			this.inIngredients = inIngredients;
		}
	}
	
	static abstract class BottleLiquidAction extends BottleAction {

		protected int millilitres;

		BottleLiquidAction(Bottle.Type bottleType, boolean inIngredients, int millilitres) {
			super(bottleType, inIngredients);
			this.millilitres = millilitres;
		}
	}
	
	static class FruitCatchedAction extends FruitAction {

		private final static float ADDITIONAL_TIME = 3f;
		
		FruitCatchedAction(Fruit.Type fruitType, boolean inIngredients) {
			super(fruitType, inIngredients);
		}

		@Override
		public boolean applyRule(ReceiptBuildedRule rule) {
			if (!inIngredients) return false;

			List<Ingredient<Fruit.Type>> fruits = rule.getReceipt().getFruits();
			Iterator<Ingredient<Fruit.Type>> fruitIterator = fruits.iterator();
			while (fruitIterator.hasNext()){
				Ingredient<Fruit.Type> fruit = fruitIterator.next();
				if (fruit.getValue() == fruitType){
					int fruitCount = fruit.getCount();
					fruit.setCount(--fruitCount);
					if (fruitCount == 0) {
						fruitIterator.remove();
					} 
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean applyRule(WrongFruitCatchedRule rule) {
			return !inIngredients;
		}
		
		@Override
		public boolean applyRule(OutOfTimeRule rule) {
			if (inIngredients) rule.addToCredit(ADDITIONAL_TIME);
			return inIngredients;
		}
		
		@Override
		public boolean applyRule(SpoiledRecipeRule rule) {
			return !inIngredients;
		}
	}
	
	static class LiquidCatchedAction extends BottleLiquidAction {
		private final static float ADDITIONAL_TIME = 0.01f;

		LiquidCatchedAction(Bottle.Type bottleType, boolean inIngredients, int millilitres) {
			super(bottleType, inIngredients, millilitres);
		}

		@Override
		public boolean applyRule(ReceiptBuildedRule rule) {
			if (!inIngredients) return false;

			List<Ingredient<Bottle.Type>> bottles = rule.getReceipt().getBottles();
			Iterator<Ingredient<Bottle.Type>> bottleIterator = bottles.iterator();
			while (bottleIterator.hasNext()){
				Ingredient<Bottle.Type> bottle = bottleIterator.next();
				if (bottle.getValue() == bottleType){
					int liquidRequired = bottle.getCount();
					int liquidLeft = liquidRequired - millilitres;
					if (liquidLeft < 0) { 
						bottleIterator.remove();
						bottle.setCount(0);
					} else { 
						bottle.setCount(liquidLeft);
					}
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean applyRule(WrongLiquidCatchedRule rule) {
			if (!inIngredients) rule.currentValue += millilitres;
			return !inIngredients;
		}
		
		@Override
		public boolean applyRule(OutOfTimeRule rule) {
			if (inIngredients) rule.addToCredit(ADDITIONAL_TIME * millilitres);
			return inIngredients;
		}
		
		@Override
		public boolean applyRule(SpoiledRecipeRule rule) {
			return !inIngredients;
		}
	}

	static class FruitMissedAction extends FruitAction {

		FruitMissedAction(Fruit.Type fruitType, boolean inIngredients) {
			super(fruitType, inIngredients);
		}

		@Override
		public boolean applyRule(MissedFruitRule rule) {
			return inIngredients;
		}
	}
	
	static class BottleOpenedAction extends BottleAction {

		BottleOpenedAction(Bottle.Type bottleType, boolean inIngredients) {
			super(bottleType, inIngredients);
		}
		
		@Override
		public boolean applyRule(WrongBottleOpenedRule rule) {
			return !inIngredients;
		}
	}
	
	static class LiquidMissedAction extends BottleLiquidAction {

		LiquidMissedAction(Bottle.Type bottleType, boolean inIngredients, int millilitres) {
			super(bottleType, inIngredients, millilitres);
		}
		
		@Override
		public boolean applyRule(MissedLiquidRule rule) {
			if (inIngredients) rule.currentValue += millilitres;
			return inIngredients;
		}
	}
	
	static class TimePassedAction extends DefaultAction {
		private float timePassed;
		
		public TimePassedAction(float timePassed) {
			this.timePassed = timePassed;
		}
		
		@Override
		public boolean applyRule(OutOfTimeRule rule) {
			rule.takeAwayTime(timePassed);
			return true;
		}
		
		@Override
		public boolean applyRule(SpoiledRecipeRule rule) {
			rule.update(timePassed);
			return false;
		}
	}
}
