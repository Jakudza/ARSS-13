package ar.de.tum.resources;

import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.gameengine.NotifyTransformGroup;

import de.tum.in.far.threedui.general.PoseReceiver;

public class Shaker implements Notifiable {

	private shakerJ3DObject j3dObject; 
	private BranchGroup shakerBranchGroup;

	private Vector<Notifiable> observer;
	private NotifyTransformGroup shakerTransformGroup;
	private PoseReceiverShaker shakerReceiver;

	public Shaker()
	{
		j3dObject = new shakerJ3DObject();
		shakerBranchGroup = new BranchGroup();
		shakerTransformGroup = new NotifyTransformGroup();
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);


		shakerBranchGroup.addChild(shakerTransformGroup);
		shakerTransformGroup.addChild(j3dObject);


		observer = new Vector<Notifiable>();
	}

	public void setPoseReceiver(PoseReceiverShaker p) {
		shakerReceiver = p;
		p.setObserver(this);

		p.setTransformGroup(shakerTransformGroup);

	}

	@Override
	public void update(Object o, Object aData) {
		// TODO Auto-generated method stub
		if (o.equals((Object)shakerReceiver) && aData instanceof Boolean) {
			notifyObserver();
		}
	}

	private void notifyObserver() {
		// TODO Auto-generated method stub
		for (Notifiable n: observer) {
			n.update(this, true);
		}
	}

	public BranchGroup getJ3dObject() {
		// TODO Auto-generated method stub
		return shakerBranchGroup;
	}

	public void addObserver(Notifiable notifiable) {
		// TODO Auto-generated method stub
		observer.add(notifiable);
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
		// Probably send msg to player about successful collection
	}
}


