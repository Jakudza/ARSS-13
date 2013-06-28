package ar.de.tum.gamelogic;



import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;

import ar.de.tum.animatios.FallingAnimation;
import ar.de.tum.resources.FruitResources;
import ar.de.tum.resources.Notifiable;
import ar.de.tum.resources.Shaker;
import ar.tum.de.main.CubeObject;
import de.tum.in.far.threedui.general.BlueAppearance;

public class Fruit {
	
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
	
	private FallingAnimation animation;

	private Type type;

	private boolean deleteFruit = false;

	private Shaker shaker;

	private BranchGroup bg;

	private static FruitResources R;
	
	public static void setResources(FruitResources r) {
		R = r;
	}

	CubeObject cube = new CubeObject (0.013f, 0.013f, 0.013f, new BlueAppearance());
	
	public Fruit(Type type) {
		animation = new FallingAnimation(cube);
		this.type = type;
		System.out.println("Fruit created!");
	}
	
	public FallingAnimation getFallingAnimation(){
		return animation;
	}
	
	public void setTranslation(Transform3D transform3d) {
		cube.getTransformGroup().setTransform(transform3d);
	}

	public Type getType() {
		return type;
	}

	public boolean getDeleteFruit()
	{
		return deleteFruit;
	}
	
	public void update(){
		if (animation.finished()) deleteFruit = true;
	}
	
	private BranchGroup group;
	
	public BranchGroup getBranchGroup()
	{
		return group;
	}
	
	public void setBranchGroup(BranchGroup bg)
	{
		group = bg;
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

	protected void handleShakerInteraction() {
		// TODO Auto-generated method stub
		System.out.print("Collision, print from fruit");
		deleteFruit  = true;
	}
}

