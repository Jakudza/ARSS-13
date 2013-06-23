package ar.de.tum.gamelogic;

import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.vecmath.Vector3d;

import ar.tum.de.main.Runner;

public class FruitSpawner {

	public static final double xGridSize = 0.5;
	public static final double yGridSize = 0.5;
	public static final int xGridCount = 4;
	public static final int yGridCount = 4;
	private Random rand = new Random();
	
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
	private Runner runner;
	private double[][] xgrid;
	private double[][] ygrid;
	private long time;
	
	public FruitSpawner(Emission rate, IngredientFactory factory, Runner runner) {
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
	
	public void update(long delta) {
		time += delta;
		if (time > nextFruitTime) {
			nextFruitTime = rate.getTime();
			time = 0;
			
			Fruit fruit = factory.getFruit();
			Transform3D t3dOffset = new Transform3D();
			int x = rand.nextInt(xGridCount);
			int y = rand.nextInt(yGridCount);
			t3dOffset.setTranslation(new Vector3d(xgrid[x][y], ygrid[x][y], 0.25));
			fruit.getTransformGroup().setTransform(t3dOffset);
			
			Transform3D xAxis = new Transform3D();
  		    
			Alpha xAlpha = new Alpha( -1, Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE, 1000, 1000, 5000, 1000, 1000, 10000, 2000, 4000 );
			PositionInterpolator posInt = new PositionInterpolator(xAlpha, fruit.getTransformGroup(), xAxis, -0.8f, 0.8f );
			fruit.getTransformGroup().addChild(posInt);
			runner.addFruit(fruit);
		}
	}
}
