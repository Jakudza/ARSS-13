package ar.de.tum.gamelogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

import de.tum.in.far.threedui.ex1.AnimationRotation;
import de.tum.in.far.threedui.ex1.SphereObject;
import de.tum.in.far.threedui.general.FakeShadow;

import ar.de.tum.animations.FallingAnimation;
import ar.de.tum.resources.Shaker;
import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.main.Runner;

public class FruitSpawner {

	public static final double xGridSize = 0.25;
	public static final double yGridSize = 0.25;
	public static final int xGridCount = 3;
	public static final int yGridCount = 3;
	private Random rand = new Random();
	private CollisionDetector detector;
	private List<Fruit> fruits;
	
	public enum Emission {
		XFAST(500f), FAST(1000f), MEDIUM(1500f), SLOW(3000f), XSLOW(6000f);

		private float time;

		private Emission(float time) {
			this.time = time;
		}

		public float getTime() {
//			float error = (float) ((Math.random() - 50f) / 2);
			//TODO
			return time; //+ error;
		}
	}
	
	private IngredientFactory factory;
	private Emission rate;
	private float nextFruitTime;
	private float clearTime;
	private Runner runner;
	private double[][] xgrid;
	private double[][] ygrid;
	private long time;
	private Shaker shaker;
	
	public FruitSpawner(Emission rate, IngredientFactory factory, Runner runner) {
		detector = null;
		fruits = new ArrayList<Fruit>();
		this.rate = rate;
		this.factory =  factory;
		this.runner = runner;
		xgrid = new double[xGridCount][yGridCount];
		ygrid = new double[xGridCount][yGridCount];
		double xStep = xGridSize / xGridCount;
		double yStep = yGridSize / yGridCount;
		for (int i = 0 ; i < xGridCount; i++){
			for (int j = 0; j < yGridCount; j++) {
				xgrid[i][j] = -xGridSize / 2 + i * xStep;
				ygrid[i][j] = -yGridSize / 2 + j * yStep;
			}
		}
	}
	
	private boolean stopped = false;
	
	public void stop(){
		stopped = true;
	}
	
	public void setDetector(CollisionDetector d)
	{
		detector = d;
	}
	
	public void setShaker(Shaker s)
	{
		shaker = s;
	}
	
	public void update(long delta) {
		time += delta;
		if (time > nextFruitTime && !stopped) {
			nextFruitTime = rate.getTime();
			time = 0;
			
			Transform3D t3dOffset = new Transform3D();
			int x = rand.nextInt(xGridCount);
			int y = rand.nextInt(yGridCount);
			t3dOffset.setTranslation(new Vector3d(xgrid[x][y], ygrid[x][y], 0.25));
			Fruit fruit = factory.getFruit();
			fruit.setCollisionListner(runner);
			fruit.setTranslation(t3dOffset);

			BranchGroup bg = new BranchGroup();
			bg.setCapability(TransformGroup.ENABLE_COLLISION_REPORTING);
			bg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
			bg.setCapability(BranchGroup.ALLOW_DETACH);

			detector = new CollisionDetector(bg);
			bg.addChild(fruit.getFallingAnimation());
			bg.addChild(detector);

			fruit.registerWithShaker(shaker);
			fruit.setBranchGroup(bg);
			fruit.regisetWithCollisionDetector(detector);

			// Shadow
			BranchGroup shadowBg = new BranchGroup();
			shadowBg.setCapability(BranchGroup.ALLOW_DETACH);
			
			SphereObject s = new SphereObject(0.01f);
			FakeShadow fs = new FakeShadow((GeometryArray) s.getGeometry(), new Color3f(0.2f, 0.2f, 0.2f));

			Transform3D fruitShadowT3D = new Transform3D();
			fruitShadowT3D.setTranslation(new Vector3d(xgrid[x][y], ygrid[x][y], 0.001));
			fruitShadowT3D.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
			fs.getTransformGroup().setTransform(fruitShadowT3D);
			
			shadowBg.addChild(fs);
			fruit.addShadow(shadowBg);
			
			
			runner.addFruit(bg);
			runner.addShadow(shadowBg);
			fruits.add(fruit);
		}

		for(Iterator<Fruit> it = fruits.iterator(); it.hasNext(); )
		{
			Fruit fruit = it.next();
			fruit.update();
			if(fruit.getDeleteFruit())
			{
				BranchGroup b = fruit.getBranchGroup(); 
				BranchGroup shadowBg = fruit.getShadow();

				it.remove();
				runner.removeFruit(b);
				runner.removeShadow(shadowBg);
			}
		}
		
	}
}
