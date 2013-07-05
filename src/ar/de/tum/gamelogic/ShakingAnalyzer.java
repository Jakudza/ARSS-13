package ar.de.tum.gamelogic;


public class ShakingAnalyzer {
	
	public interface ShakingOracle {
		
		boolean isShakingValid();
		
		void onShakingFinished(int points);
		
		void onShakingStarted();
	}
	
	private static final int length = 30;
	private static final long SHAKING_DURATION = 3000l;
	private static final long SHAKING_DELAY = 200l;
	private static final double SHAKING_HIGH_MARGIN = 0.17;

	private double[][] poses =  new double[length][3];
	private long[] time = new long[length];
	private boolean shaking;
	private long startOfShaking;
	private ShakingOracle oracle;
	private double pointsShaking;
	
	public ShakingAnalyzer(ShakingOracle oracle) {
		this.oracle = oracle;
	}
	
	public void receiveNewPose(double[] pose) {
		for (int  i = length - 1; i > 0; i--) {
			time[i] = time[i-1];
			poses[i] = poses[i-1];
		}
	
		time[0] =  System.currentTimeMillis();
		poses[0] = pose;
		
		long time1 = 0;
		long time2 = 0;
		int mid =  length / 2;
		time1 = time[mid] - time[0];
		time2 = time[length - 1] - time[mid];
	
		double[] cVel =  new double[3];
		double[] pVel = new double[3];
		double[] accelerometer = new double[3];
		for(int i = 0; i < 3; i++) {
			cVel[i] =  (poses[0][i] - poses[mid][i]) / time1;
			pVel[i] =  (poses[mid][i] - poses[length - 1][i]) / time2;
			accelerometer[i] = ((cVel[i] - pVel[i]) / time1) * Math.pow(10, 6);
		}
	//	System.out.println("Acc = " + Arrays.toString(accelerometer));
		
		if (shaking) {
			if (System.currentTimeMillis() - startOfShaking > SHAKING_DURATION) {
				oracle.onShakingFinished((int)pointsShaking * 100);
			}
				
			if (isShakingValid(accelerometer)){
				pointsShaking += Math.abs(accelerometer[2]);
			} 
		}
		
		else if (isShakingValid(accelerometer) && oracle.isShakingValid()){
			oracle.onShakingStarted();
			shaking = true;
			startOfShaking = System.currentTimeMillis();
		}
	}
	
	private boolean isShakingValid(double[] accelerometer){
		return Math.abs(accelerometer[2]) > SHAKING_HIGH_MARGIN;
	}
}
