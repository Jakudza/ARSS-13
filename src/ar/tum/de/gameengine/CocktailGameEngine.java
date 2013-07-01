package ar.tum.de.gameengine;

import ar.de.tum.gamelogic.Bottle;
import ar.de.tum.gamelogic.Fruit;
import ar.de.tum.gamelogic.Receipt;
import ar.de.tum.gamelogic.Receipt.Ingredient;
import ar.tum.de.gameengine.GameScore.Modifier;
import ar.tum.de.gameengine.GameScore.ScoreListener;
import ar.tum.de.gameengine.rules.GameActionFactory;
import ar.tum.de.gameengine.rules.ReceiptBuildedRule;

public class CocktailGameEngine extends GameEngine{

	public interface ProgressListener extends ScoreListener {
		
		void onChangedBottleIngredient(Ingredient<Bottle.Type> bottleType);
		
		void onChangedFruitIngredient(Ingredient<Fruit.Type> fruitType);
	}
	
	private static final String LOG_TAG = CocktailGameEngine.class.getSimpleName();
	
	private static final float DESCREASE_MODIFIER_TIME = 1f; 
	
	private static final int INCREASE_MODIFIER_TIME = 750;

	private float timeTrackDecrease;

	private long timeTrackIncrease;
	
	private Bottle.Type lastBottleCatched;

	/**
	 * Modified by applying to actions {@link ReceiptBuildedRule}.
	 */
	private Receipt currentProgress;
	
	private GameScore score;
	
	private ProgressListener progressListener;
	
	public CocktailGameEngine(GameRule successRule, GameRule failRule, Receipt currentProgress) {
		super(successRule, failRule);
		this.currentProgress = currentProgress;
		this.score = new GameScore();
	}

	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
		score.setListener(progressListener);
	}

	public void onFruitMissed(Fruit.Type fruitType){
	//	if (Log.VERBOSE) Log.v(LOG_TAG, "missed " + fruitType);
		System.out.println(LOG_TAG + " Missed: " + fruitType);
		actionOcurred(GameActionFactory.createFruitMissedAction(fruitType, currentProgress.getIngredient(fruitType) != null));
	}

	public void onFruitCatched(Fruit.Type fruitType){
	//	if (Log.VERBOSE) Log.v(LOG_TAG, "catched " + fruitType);
		System.out.println(LOG_TAG + " Catched: " + fruitType);
		Ingredient<Fruit.Type> fruit = currentProgress.getIngredient(fruitType);
		actionOcurred(GameActionFactory.createFruitCatchedAction(fruitType, fruit != null));
		
		if (fruit != null) {
			score.upgradeScore(fruitType.getScore());
			if (progressListener != null) progressListener.onChangedFruitIngredient(fruit);
			
			long currentTime = System.currentTimeMillis();
			if (timeTrackIncrease != 0 && (currentTime - timeTrackIncrease < INCREASE_MODIFIER_TIME)) {
				score.increaseModifier();
			}	
			timeTrackIncrease = currentTime;
		} else {
			score.resetModifier();
		}
	}

	public void onBottleOpened(Bottle.Type bottleType){
		actionOcurred(GameActionFactory.createBottleOpenedAction(bottleType, currentProgress.getIngredient(bottleType) != null));
	}

	public void onLiquidMissed(Bottle.Type bottleType, int milliliters){
		actionOcurred(GameActionFactory.createLiquidMissedAction(bottleType, currentProgress.getIngredient(bottleType) != null, milliliters));
	}

	public void onLiquidCatched(Bottle.Type bottleType, int milliliters){
	//	if (Log.VERBOSE) Log.v(LOG_TAG, "catched bottle = " + bottleType + " mili = " + milliliters);
		Ingredient<Bottle.Type> bottle = currentProgress.getIngredient(bottleType);
		actionOcurred(GameActionFactory.createLiquidCatchedAction(bottleType, bottle != null, milliliters));
		
		if (bottle != null) {
			score.upgradeScore(bottleType.getScore() * milliliters);
			if (progressListener != null) progressListener.onChangedBottleIngredient(bottle);
			
			long currentTime = System.currentTimeMillis();
			if (timeTrackIncrease != 0 && (currentTime - timeTrackIncrease < INCREASE_MODIFIER_TIME) && bottleType != lastBottleCatched) {
				score.increaseModifier();
			}	
			timeTrackIncrease = currentTime;
			lastBottleCatched = bottleType;
		} else {
			score.resetModifier();
		}
	}
	
	public void update(float deltaTime) {
		actionOcurred(GameActionFactory.createTimePassed(deltaTime));
		
		if (score.getModifier() != Modifier.SINGLE) {
			timeTrackDecrease += deltaTime;
			if (timeTrackDecrease > DESCREASE_MODIFIER_TIME) {
				score.resetModifier();
				timeTrackDecrease = 0;
			}
		}
	}
}
