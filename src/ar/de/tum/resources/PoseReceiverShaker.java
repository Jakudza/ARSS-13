package ar.de.tum.resources;

import java.util.Timer;
import java.util.TimerTask;

import de.tum.in.far.threedui.general.PoseReceiver;
import ar.tum.de.gameengine.NotifyTransformGroup;

public class PoseReceiverShaker extends PoseReceiver  {

	private static final long TIME_OUT = 1000;
	
	private Notifiable observer;
	private long timeStamp = System.currentTimeMillis();
	private boolean statePressed;
	private boolean initialized = false;
	
	public PoseReceiverShaker() {
		observer = null;
		final Object observee = this;
		Timer t = new Timer();
		TimerTask tt = new TimerTask(){
			public void run() {
				if(!initialized)
					return;
				long currTime = System.currentTimeMillis();
				
				if (currTime - timeStamp > TIME_OUT) {
					if (!statePressed) {
						statePressed = true;
						observer.update(observee, new Boolean(true));
					}
				} else {
					if (statePressed) {
						statePressed = false;
						observer.update(observee, new Boolean(false));
					}
				}
			}
		};
		t.scheduleAtFixedRate(tt, 0, TIME_OUT/10);	
	}
	public void setObserver(Notifiable o) {
		observer = o;
		
	}

}
