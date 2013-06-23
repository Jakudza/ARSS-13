package ar.de.tum.gamelogic;

import java.util.List;

import ar.de.tum.gamelogic.Bottle.Type;

public interface RandomChoiceStategy {

	public Fruit.Type getFruitType(List<Fruit.Type> types);

	public int getBottleCount();

	public Bottle.Type getBottleType(List<Type> otherBottles);
}
