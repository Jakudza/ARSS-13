package ar.de.tum.gamelogic;



import java.io.FileNotFoundException;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import ar.de.tum.animations.FallingAnimation;
import ar.de.tum.resources.FruitResources;
import ar.de.tum.resources.Notifiable;
import ar.de.tum.resources.Shaker;
import ar.tum.de.gameengine.CollisionDetector;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

import de.tum.in.far.threedui.general.ModelObject;

public class Fruit {
	
	public interface FruitListener {
		
		void onCollideWithShaker(Fruit.Type fruit);
		
		void onCollideWithGround(Fruit.Type fruit);
	}
	
	public static VrmlLoader loader = new VrmlLoader();
	
	public static ModelObject loadModel(String path, float scale){
		Scene myScene = null;
		try {
			myScene = loader.load(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IncorrectFormatException e) {
			e.printStackTrace();
		} catch (ParsingErrorException e) {
			e.printStackTrace();
		}

		BranchGroup bg = new BranchGroup();
		TransformGroup offset = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
		t3d.setScale(scale);
		
		offset.setTransform(t3d);
		bg.addChild(offset);
		offset.addChild(myScene.getSceneGroup());
		
		return new ModelObject(bg);
	}
	
	public interface OnDisappearListener {
		void onDisappear(Fruit fruit);
	}

	public enum Type {

		APPLE(R.score.apple, R.path.apple, R.scale.apple),
		BANANA(R.score.banana, R.path.banana, R.scale.banana),
		TABASCO(R.score.tabasco, R.path.tabasco, R.scale.tabasco),
		MUSHROOM(R.score.mushroom, R.path.mushroom, R.scale.mushroom);
	
		private int score;
		private String path;
		private float scale;
	
		Type(int score, String path, float scale){
			this.score = score;
			this.path = path;
			this.scale = scale;
		}
		
		public float getScale() {
			return scale;
		}

		public int getScore() {
			return score;
		}

		public String getPath() {
			return path;
		}
	}

	public static final String LOG_TAG = Fruit.class.getSimpleName();

	private OnDisappearListener disappearListener;
	
	private FallingAnimation animation;

	private FruitListener listener;
	
	private Type type;

	private boolean deleteFruit = false;

	private Shaker shaker;

	private BranchGroup bg;

	private static FruitResources R;
	
	public static void setResources(FruitResources r) {
		R = r;
	}

	ModelObject model;
	
	public Fruit(Type type) {
		model = loadModel(type.path, type.scale);
		animation = new FallingAnimation(model);
		this.type = type;
		System.out.println("Fruit created!");
	}
	
	public FallingAnimation getFallingAnimation(){
		return animation;
	}
	
	public void setTranslation(Transform3D transform3d) {
		model.getTransformGroup().setTransform(transform3d);
	}

	public Type getType() {
		return type;
	}

	public boolean getDeleteFruit()
	{
		return deleteFruit;
	}
	
	public void update(){
		if (animation.finished()) { 
			if (listener != null) listener.onCollideWithGround(type);
			deleteFruit = true;
		}
	}
	
	private BranchGroup group;

	private BranchGroup shadowBg;
	
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

	public void setCollisionListner(FruitListener listener) {
		this.listener = listener;
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
		if (listener != null) listener.onCollideWithShaker(type);
		System.out.println("Collision, print from fruit");
		deleteFruit  = true;
	}

	protected void handleShakerInteraction() {
		// TODO Auto-generated method stub
		System.out.println("Collision, print from fruit");
		deleteFruit  = true;
	}

	public void addShadow(BranchGroup shadowBg) {
		// TODO Auto-generated method stub
		this.shadowBg = shadowBg;
	}

	public BranchGroup getShadow() {
		// TODO Auto-generated method stub
		return shadowBg;
	}
}

