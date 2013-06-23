package ar.de.tum.gamelogic;

import ar.de.tum.resources.BottleResources;

public class Bottle {
	
	private static BottleResources R;
	
	public static void setResources(BottleResources r) {
		R = r;
	}
	
	private Type type;
	
	public Bottle(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}
	
	public enum Type{
		VODKA(R.score.vodka);
		private int score;
		
		Type(int score){
			this.score = score;
		}
		
		public int getScore() {
			return score;
		}
	}
}
