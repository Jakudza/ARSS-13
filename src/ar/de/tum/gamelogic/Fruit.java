package ar.de.tum.gamelogic;



import javax.media.j3d.BranchGroup;

import de.tum.in.far.threedui.general.BlueAppearance;
import ar.de.tum.resources.FruitResources;
import ar.de.tum.resources.Notifiable;
import ar.de.tum.resources.Shaker;
import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.main.CubeObject;

public class Fruit extends CubeObject {

	public interface OnDisappearListener {
		void onDisappear(Fruit fruit);
	}

	public enum Type{
	//  TODO this fruits commented temporarily 	
	//	PLUM(R.image.plum, R.score.plum), KIWI(R.image.kiwi, R.score.kiwi), 
	/*	STRAWBERRY(R.image.strawberry, R.score.strawberry, R.sImage.strawberry),
		KNIFE(R.image.knife, R.score.knife, R.sImage.knife), 
		PAPAYA(R.image.papaya, R.score.papaya, R.sImage.papaya),
		APPLE(R.image.apple, R.score.apple, R.sImage.apple), 
		GRAPE(R.image.grape, R.score.grape, R.sImage.grape), 
		BANANA(R.image.banana, R.score.banana, R.sImage.banana), 
		PINEAPPLE(R.image.pineapple, R.score.pineapple, R.sImage.pineapple), 
		WATERMELON(R.image.watermelon, R.score.watermelon, R.sImage.watermelon), 
		ORANGE(R.image.orange, R.score.orange, R.sImage.orange);*/
		
		ORANGE(R.score.orange);
	
		private int score;
	
		Type(int score){
			this.score = score;
		}
		
		public int getScore() {
			return score;
		}


	}

	public static final String LOG_TAG = Fruit.class.getSimpleName();

	private OnDisappearListener disappearListener;

	private Type type;

	private boolean deleteFruit = false;

	private Shaker shaker;

	private BranchGroup bg;

	private static FruitResources R;
	
	public static void setResources(FruitResources r) {
		R = r;
	}

	public Fruit(Type type) {
		super(0.013f, 0.013f, 0.013f, new BlueAppearance());
		this.type = type;
		System.out.println("Fruit created!");
	}

	public Type getType() {
		return type;
	}

	public boolean getDeleteFruit()
	{
		return deleteFruit;
	}
	
	public void setBranchGroup(BranchGroup bg)
	{
		this.bg = bg;
	}
	public BranchGroup getBranchGroup()
	{
		return bg;
	}

	public void registerWithShaker(Shaker s) {
		// TODO Auto-generated method stub
		shaker = s;
		shaker.addObserver(new Notifiable() {			
			public void update(Object notifier, Object aData) {
				if (true == (Boolean)aData) {
					handleShakerInteraction();
				}				
			}
		});
				
//		shaker.addShakerPoseObserver(poseUpdater);
		
	}

	public void regisetWithCollisionDetector(CollisionDetector detector) {
		// TODO Auto-generated method stub
		detector.addObserver(new Notifiable() {
			public void update(Object notifier, Object aData) {
				if (aData instanceof Boolean)
				handleCollisionInteraction(((Boolean)aData).booleanValue());
			}
		});
	}
	protected void handleCollisionInteraction(boolean booleanValue) {
		// TODO Auto-generated method stub
		System.out.println("Collision, print from fruit");
		deleteFruit  = true;
	}

	protected void handleShakerInteraction() {
		// TODO Auto-generated method stub
		System.out.println("Collision, print from fruit");
		deleteFruit  = true;
	}
}

