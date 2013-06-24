package ar.tum.de.gameengine;

import java.util.Enumeration;
import java.util.Vector;

import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Node;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnCollisionEntry;
import javax.media.j3d.WakeupOnCollisionExit;
import javax.media.j3d.WakeupOnCollisionMovement;
import javax.media.j3d.WakeupOr;
import javax.vecmath.Point3d;

import ar.de.tum.resources.Notifiable;

public class CollisionDetector extends Behavior  {

	private WakeupCriterion[] wakeups;
	private WakeupOr oredCriteria;
	private Node collidingShape;
	
	private Vector<Notifiable> observer;
	
	public CollisionDetector(Node n) {
		collidingShape = n;
		setSchedulingBounds(new BoundingSphere(new Point3d(), 200.0));
		
		observer = new Vector<Notifiable>();
	}
	
	public void initialize() {
		wakeups = new WakeupCriterion[3];
		wakeups[0] = new WakeupOnCollisionEntry(collidingShape);
		wakeups[1] = new WakeupOnCollisionExit(collidingShape);
		wakeups[2] = new WakeupOnCollisionMovement(collidingShape);
		oredCriteria = new WakeupOr(wakeups);
		wakeupOn(oredCriteria);
	}

	public void processStimulus(Enumeration e) {
		WakeupCriterion criterion = (WakeupCriterion) e.nextElement();
		
		boolean collision = false;
		
		if (criterion instanceof WakeupOnCollisionEntry || criterion instanceof WakeupOnCollisionExit) {
			if (criterion instanceof WakeupOnCollisionEntry) {
				collision = true;
			}
			for (Notifiable n: observer) {
				n.update(this, new Boolean(collision));
			}
		}			
				
		wakeupOn(oredCriteria);
	}

	public void addObserver(Notifiable o) {
		observer.add(o);
	}

}
