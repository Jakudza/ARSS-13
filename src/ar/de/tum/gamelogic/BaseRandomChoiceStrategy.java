package ar.de.tum.gamelogic;

import java.util.List;
import java.util.Random;

public class BaseRandomChoiceStrategy implements RandomChoiceStategy {

	Random random = new Random();
	
	@Override
	public Fruit.Type getFruitType(List<Fruit.Type> types) {
		return types.get(random.nextInt(types.size() - 1));
	}

	@Override
	public int getBottleCount() {
		return random.nextInt(6) + 1;
	}

	@Override
	public Bottle.Type getBottleType(List<Bottle.Type> otherBottles) {
		return otherBottles.get(random.nextInt(otherBottles.size() - 1));
	}

}
