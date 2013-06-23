package ar.tum.de.gameengine;

/**
 * Game rule entity used for describing success/fail rules of a game world.<br>
 * Every {@link GameAction} could affect rule. 
 * 
 */
public interface GameRule {

	/**
	 * 
	 * @param action
	 * @return true if action affects rule, false - otherwise
	 * @throws IllegalStateException if rule is completed
	 */
	boolean affectedBy(GameAction action) throws IllegalStateException;
	
	/**
	 * 
	 * @return true if action completed, false - otherwise
	 */
	boolean isCompleted();
}
