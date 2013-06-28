package ar.de.tum.gamelogic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import de.tum.in.far.threedui.ex1.AnimationRotation;

import ar.de.tum.animatios.FallingAnimation;
import ar.de.tum.resources.Shaker;
import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.main.Runner;

public class FruitSpawner {

	public static final double xGridSize = 0.5;
	public static final double yGridSize = 0.5;
	public static final int xGridCount = 4;
	public static final int yGridCount = 4;
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
		if (time > nextFruitTime) {
			nextFruitTime = rate.getTime();
			time = 0;
			
			Transform3D t3dOffset = new Transform3D();
			int x = rand.nextInt(xGridCount);
			int y = rand.nextInt(yGridCount);
			t3dOffset.setTranslation(new Vector3d(xgrid[x][y], ygrid[x][y], 0.25));
			Fruit fruit = factory.getFruit();
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


			runner.addFruit(bg);
			fruits.add(fruit);
		}

		for(Iterator<Fruit> it = fruits.iterator(); it.hasNext(); )
		{
			Fruit fruit = it.next();
			fruit.update();
			if(fruit.getDeleteFruit())
			{
				BranchGroup b = fruit.getBranchGroup(); 

				it.remove();
				runner.removeFruit(b);
			}
		}
		
	}
}
