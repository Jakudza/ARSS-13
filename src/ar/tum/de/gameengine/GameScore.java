package ar.tum.de.gameengine;

import java.awt.Color;

import ar.de.tum.resources.GameScoreResources;

public class GameScore {
	
	public interface ScoreListener {
		
		void onChangedScore(int oldScore, int newScore);
		void onChangedModifier(Modifier oldModifier, Modifier newModifier);
	}
	
	private static GameScoreResources R;
	
	public static void setResources(GameScoreResources res) {
		R = res;
	}	
	
	private int currentScore;
	private Modifier currentModifier = Modifier.SINGLE;
	private ScoreListener listener;
	
	public void setListener(ScoreListener listener) {
		this.listener = listener;
	}
	
	public void upgradeScore(int additionalScore) {
		int oldScore = currentScore;
		currentScore += additionalScore * currentModifier.value;
		if (listener != null) listener.onChangedScore(oldScore, currentScore);
	}
	
	public int getScore() {
		return currentScore;
	}
	
	public Modifier getModifier() {
		return currentModifier;
	}
	
	public void increaseModifier() {
		Modifier oldModifier = currentModifier;
		currentModifier  = oldModifier.next();
		if (listener != null) listener.onChangedModifier(oldModifier, currentModifier);
	}
	
	public void decreaseModifier() {
		Modifier oldModifier = currentModifier;
		currentModifier = oldModifier.previous();
		if (listener != null) listener.onChangedModifier(oldModifier, currentModifier);
	}
	
	public void resetModifier() {
		Modifier oldModifier = currentModifier;
		currentModifier = Modifier.SINGLE;
		if (listener != null) listener.onChangedModifier(oldModifier, currentModifier);
	}
	
	public enum Modifier {
		SINGLE(1, R.string.normal, R.color.normal) {
			@Override
			public Modifier next() {
				return DOUBLE;
			}

			@Override
			public Modifier previous() {
				return SINGLE;
			}
		}, 
		DOUBLE(2, R.string.perfect, R.color.perfect) {
			@Override
			public Modifier next() {
				return QUADRUPLE;
			}

			@Override
			public Modifier previous() {
				return SINGLE;
			}
		}, 
		QUADRUPLE(4, R.string.excellent, R.color.excellent) {
			@Override
			public Modifier next() {
				return OCTUPLE;
			}

			@Override
			public Modifier previous() {
				return DOUBLE;
			}
		},
		OCTUPLE(8, R.string.ownage, R.color.ownage) {
			@Override
			public Modifier next() {
				return DOUBLE_OCTUPLE;
			}

			@Override
			public Modifier previous() {
				return QUADRUPLE;
			}
		}, 
		DOUBLE_OCTUPLE(16, R.string.domination, R.color.domination) {
			@Override
			public Modifier next() {
				return DOUBLE_OCTUPLE;
			}

			@Override
			public Modifier previous() {
				return OCTUPLE;
			}
		};
		
		private int value;
		private java.awt.Color color;
		private String inscrpiption;
		
		Modifier(int value, String inscription, Color color) {
			this.value = value;
			this.color = color;
			this.inscrpiption = inscription;
		}
		
		public int getValue() {
			return value;
		}
		
		public Color getColor() {
			return color;
		}
		
		public String getInscription() {
			return inscrpiption;
		}
		
		public abstract Modifier next();
		public abstract Modifier previous();
	}

}
