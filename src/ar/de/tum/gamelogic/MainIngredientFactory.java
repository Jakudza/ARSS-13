package ar.de.tum.gamelogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ar.de.tum.gamelogic.Receipt.Ingredient;

public class MainIngredientFactory implements IngredientFactory {

	/**
	 * This constant affects appearance of random fruits. <br>
	 * Original fruit percentage is equal to {@code 1 / FRUIT_RANDOM_FACTOR}.
	 */
	private static float FRUIT_RANDOM_FACTOR = 1.5f;
	/**
	 * This constant affects appearance of random bottles. <br>
	 * Original bottles percentage is equal to {@code 1 / BOTTLE_RANDOM_FACTOR}.
	 */
	private static float BOTTLE_RANDOM_FACTOR = 1.0f;

	private List<Ingredient<Fruit.Type>> fruits;
	private List<Ingredient<Bottle.Type>> bottles;
	private List<Fruit.Type> otherFruits;
	private List<Bottle.Type> otherBottles;
	private int totalFruits;
	private int totalBottles;
	private Random random = new Random();
	private RandomChoiceStategy choiceStrategy;

	public MainIngredientFactory(Receipt receipt, RandomChoiceStategy strategy) {
		fruits = receipt.getSortedFruits();
		bottles = receipt.getSortedBottles();
		
		choiceStrategy = strategy;
		
		otherFruits = new ArrayList<Fruit.Type>(Arrays.asList(Fruit.Type.values()));
		otherBottles = new ArrayList<Bottle.Type>(Arrays.asList(Bottle.Type.values()));
		
		for(Ingredient<Fruit.Type> ingredient : receipt.getFruits()) {
			totalFruits += ingredient.getCount();
			otherFruits.remove(ingredient.getValue());
		}
		
		for(Ingredient<Bottle.Type> ingredient : receipt.getBottles()) {
			totalBottles += ingredient.getCount();
			otherBottles.remove(ingredient.getValue());
		}
	}

	@Override
	public Fruit getFruit() {
		int targetFruitCount =  random.nextInt((int)((totalFruits - 1) * FRUIT_RANDOM_FACTOR));

		Fruit.Type targetFruit = null;
		int currentCount = 0;

		for (Ingredient<Fruit.Type> fruit : fruits) {
			currentCount += fruit.getCount();
			if (currentCount > targetFruitCount) {
				targetFruit = fruit.getValue();
				break;
			}
		}

		return targetFruit != null ? new Fruit(targetFruit) : new Fruit(choiceStrategy.getFruitType(otherFruits));
	}

}
