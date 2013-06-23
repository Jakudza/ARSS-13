package ar.tum.de.gameengine;

public abstract class GameEngine {

	private final GameRule successRule;

	private final GameRule failRule;

	private GameEngineListener listener;

	private boolean gameOver;

	public GameEngine(GameRule successRule, GameRule failRule) {
		this.successRule = successRule;
		this.failRule = failRule;
	}
	
	public void setListener(GameEngineListener listener){
		this.listener = listener;
	}
	
	public void actionOcurred(GameAction action){
		if (gameOver) throw new IllegalStateException("Game is over! No actions accepted");
		
		if (isGameOver(successRule, action) && listener != null) listener.onGameSuccess();
		if (isGameOver(failRule, action) && listener != null) listener.onGameFail();
	}
	
	private boolean isGameOver(GameRule rule, GameAction action){
		if (rule != null && rule.affectedBy(action)){
			if (rule.isCompleted()){
				gameOver = true;
			}	
		}
		return gameOver;
	}
	
	public boolean isGameOver(){
		return gameOver;
	}
}
