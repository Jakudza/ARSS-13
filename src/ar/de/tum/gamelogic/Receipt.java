package ar.de.tum.gamelogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Receipt implements Cloneable {

	private List<Ingredient<Bottle.Type>> bottles;
	private List<Ingredient<Fruit.Type>> fruits;

	private Receipt() {
		bottles = new ArrayList<Receipt.Ingredient<Bottle.Type>>();
		fruits = new ArrayList<Receipt.Ingredient<Fruit.Type>>();
	}

	public List<Ingredient<Bottle.Type>> getSortedBottles() {
		Collections.sort(bottles);
		return bottles;
	}

	public List<Ingredient<Fruit.Type>> getSortedFruits() {
		Collections.sort(fruits);
		return fruits;
	}

	public List<Ingredient<Bottle.Type>> getBottles() {
		return bottles;
	}

	public List<Ingredient<Fruit.Type>> getFruits() {
		return fruits;
	}

	/**
	 * Searches for ingredient of a given type.
	 * 
	 * @param bottleType
	 * @return ingredient if search was successful, null - otherwise.
	 */
	public Ingredient<Bottle.Type> getIngredient(Bottle.Type bottleType){
		for (Ingredient<Bottle.Type> ingredient: getBottles()) {
			if (ingredient.getValue() == bottleType) return ingredient; 
		}
		
		return null;
	}
	
	/**
	 * Searches for ingredient of a given type.
	 * 
	 * @param fruitType
	 * @return ingredient if search was successful, null - otherwise.
	 */
	public Ingredient<Fruit.Type> getIngredient(Fruit.Type fruitType){
		for (Ingredient<Fruit.Type> ingredient: getFruits()) {
			if (ingredient.getValue() == fruitType) return ingredient; 
		}
		
		return null;
	}
	
	@Override
	protected Receipt clone() throws CloneNotSupportedException {
		Receipt receipt = (Receipt) super.clone();
		
		receipt.bottles = new ArrayList<Receipt.Ingredient<Bottle.Type>>();
		for (Ingredient<Bottle.Type> bottle : bottles) {
			receipt.bottles.add(new Ingredient<Bottle.Type>(bottle));
		}
		
		receipt.fruits = new ArrayList<Receipt.Ingredient<Fruit.Type>>();
		for (Ingredient<Fruit.Type> fruit : fruits) {
			receipt.fruits.add(new Ingredient<Fruit.Type>(fruit));
		}
		
		return receipt;
	}

	public Receipt copy() {
		try {
			return clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ReceiptBuilder createBuilder() {
		return new ReceiptBuilder();
	}

	public static class Ingredient<T> implements Comparable<Ingredient<T>> {

		private int count;

		private T value;

		private Ingredient(T value, int count) {
			this.value = value;
			this.count = count;
		}
		
		private Ingredient(Ingredient<T> ingredient) {
			this.count = ingredient.count;
			this.value = ingredient.value;
		}

		public T getValue() {
			return value;
		}

		public int getCount() {
			return count;
		}
		
		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public int compareTo(Ingredient<T> o) {
			return count - o.count;
		}
	}

	public static class ReceiptBuilder {

		private Receipt receipt;

		public ReceiptBuilder() {
			receipt = new Receipt();
		}

		public ReceiptBuilder addBottles(Bottle.Type bottle, int count) {
			receipt.bottles.add(new Ingredient<Bottle.Type>(bottle, count));

			return this;
		}

		public ReceiptBuilder addFruits(Fruit.Type fruit, int count) {
			receipt.fruits.add(new Ingredient<Fruit.Type>(fruit, count));

			return this;
		}

		public Receipt commit() {
			receipt.fruits = receipt.fruits;
			receipt.bottles = receipt.bottles;

			return receipt;
		}
	}
}

