package ar.tum.de.gameengine;

import java.util.Vector;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import ar.de.tum.resources.Notifiable;

public class NotifyTransformGroup extends TransformGroup {

	private Vector<Notifiable> observer;
	
	public NotifyTransformGroup() {
		observer = new Vector<Notifiable>();
	}
	
	public void addObserver(Notifiable n) {
		observer.add(n);
	}
	
	public void setTransform(Transform3D arg0) {
		super.setTransform(arg0);
		notifyObserver();
	}

	private void notifyObserver() {
		for(Notifiable n: observer) {
			n.update(this, null);
		}
		
	}
	
}
