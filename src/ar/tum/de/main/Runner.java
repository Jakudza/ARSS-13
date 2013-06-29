package ar.tum.de.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import org.jdesktop.j3d.loaders.vrml97.VrmlLoader;

import ar.de.tum.gamelogic.BaseRandomChoiceStrategy;
import ar.de.tum.gamelogic.Bottle;
import ar.de.tum.gamelogic.Fruit;
import ar.de.tum.gamelogic.FruitSpawner;
import ar.de.tum.gamelogic.MainIngredientFactory;
import ar.de.tum.gamelogic.Receipt;
import ar.de.tum.gamelogic.FruitSpawner.Emission;
import ar.de.tum.resources.BottleResources;
import ar.de.tum.resources.FruitResources;
import ar.de.tum.resources.GameScoreResources;
import ar.de.tum.resources.PoseReceiverShaker;
import ar.de.tum.resources.Shaker;
import ar.tum.de.gameengine.CocktailGameEngine;
import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.gameengine.GameEngine;
import ar.tum.de.gameengine.GameRule;
import ar.tum.de.gameengine.GameScore;
import ar.tum.de.gameengine.rules.ReceiptBuildedRule;
import ar.tum.de.gameengine.rules.SpoiledRecipeRule;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

import de.tum.in.far.threedui.general.BackgroundObject;
import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.ImageReceiver;
import de.tum.in.far.threedui.general.ModelObject;
import de.tum.in.far.threedui.general.PoseReceiver;
import de.tum.in.far.threedui.general.UbitrackFacade;
import de.tum.in.far.threedui.general.ViewerUbitrack;

public class Runner implements GameConstants {

	public static final String GAME = "Cocktail Master";
	
	static {
		BinaryEnv.init();
	}
	
	private ViewerUbitrack viewer;
	private UbitrackFacade ubitrackFacade;				

//	private CubeObject cubeObject;
	private ModelObject sheepObject;
	
	private PoseReceiverShaker shakerPoseReceiver;
	private PoseReceiver poseReceiver2;
	private ImageReceiver imageReceiver;
	
	private GameEngine gameEngine;
	private FruitSpawner spawner;
	private Shaker shaker;
	private CollisionDetector detector;
	
	public Runner() {
		ubitrackFacade = new UbitrackFacade();
		shaker = new Shaker();
	}

	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.initializeJava3D();
		runner.initializeResources();
		runner.loadSheep();
		runner.initializeUbitrack();
		runner.inititalizeGameFlow();
		System.out.println("End of main");
	}
	
	private void initializeUbitrack() {
		ubitrackFacade.initUbitrack();
		
		shakerPoseReceiver = new PoseReceiverShaker();
		if (!ubitrackFacade.setPoseCallback("posesink", shakerPoseReceiver)) {
			return;
		}
		poseReceiver2 = new PoseReceiver();
		if (!ubitrackFacade.setPoseCallback("posesink2", poseReceiver2)) {
			return;
		}
		imageReceiver = new ImageReceiver();
		if (!ubitrackFacade.setImageCallback("imgsink", imageReceiver)) {
			return;
		}
		
		BackgroundObject backgroundObject = new BackgroundObject();
		viewer.addObject(backgroundObject);
		imageReceiver.setBackground(backgroundObject.getBackground());
		
		shaker.setPoseReceiver(shakerPoseReceiver);
	//	poseReceiver.setTransformGroup(cubeObject.getTransformGroup());		
		poseReceiver2.setTransformGroup(sheepObject.getTransformGroup());		

		ubitrackFacade.startDataflow();
	}
		
	protected void loadSheep() {
		VrmlLoader loader = new VrmlLoader();
		Scene myScene = null;
		try {
			myScene = loader.load( "models" + File.separator + "Sheep.wrl");
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
		t3d.setTranslation(new Vector3d(0.0, 0.0, 0.025));
		
		offset.setTransform(t3d);
		bg.addChild(offset);
		offset.addChild(myScene.getSceneGroup());
		
		sheepObject = new ModelObject(bg);
		sheepObject.setCapability(Group.ALLOW_CHILDREN_EXTEND);
	//	sheepObject.getTransformGroup().addChild(new Fruit(Fruit.Type.ORANGE));
		viewer.addObject(sheepObject);
		System.out.println("Sheep loaded");
	}
	
	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + GAME);
		viewer = new ViewerUbitrack(GAME, ubitrackFacade);
		viewer.addObject(shaker.getJ3dObject());
		BlueAppearance blueAppearance = new BlueAppearance();
		
	//	cubeObject = new CubeObject(0.023f, 0.023f, 0.023f, blueAppearance);
	//	viewer.addObject(cubeObject);
		
		System.out.println("Done");
	}
	
	private void inititalizeGameFlow(){
		Receipt receipt = Receipt.createBuilder()
				 .addFruits(Fruit.Type.BANANA, 5)
				 .commit();
		GameRule successRule = new ReceiptBuildedRule(receipt);
		SpoiledRecipeRule failRule = new SpoiledRecipeRule(MAX_NUMBER_OF_FAILS, IGNORE_FAIL_TIME);
		gameEngine = new CocktailGameEngine(successRule, failRule, receipt);
		
		spawner = new FruitSpawner(Emission.MEDIUM, new MainIngredientFactory(receipt.copy(), new BaseRandomChoiceStrategy()), this);
		spawner.setShaker(shaker);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				update();
			}
		};
		timer.scheduleAtFixedRate(task, 100, 50);
	}
	
	private void initializeResources(){
		FruitResources fruitResources = new FruitResources();
		Fruit.setResources(fruitResources);
		GameScoreResources scoreResources = new GameScoreResources();
		GameScore.setResources(scoreResources);
		BottleResources bottleResources = new BottleResources();
		Bottle.setResources(bottleResources);
	}
	
	private long lastTimeStamp = System.currentTimeMillis();
	
	private void update(){
		long delta = System.currentTimeMillis() - lastTimeStamp;
		lastTimeStamp = System.currentTimeMillis();
		spawner.update(delta);
	}
	
	public void addFruit(BranchGroup bg){
		sheepObject.getTransformGroup().addChild(bg);
	}

	public void removeFruit(BranchGroup b) {
		// TODO Auto-generated method stub
		sheepObject.getTransformGroup().removeChild(b);
		
	}	
	
}

