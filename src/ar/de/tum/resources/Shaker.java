package ar.de.tum.resources;


import java.util.Vector;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Vector3d;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import ar.de.tum.gamelogic.Receipt;
import ar.tum.de.gameengine.CollisionDetector;
import ar.tum.de.gameengine.NotifyTransformGroup;

import de.tum.in.far.threedui.general.ModelObject;
import de.tum.in.far.threedui.general.PoseReceiver;

public class Shaker implements Notifiable {

	private shakerJ3DObject j3dObject; 
	private BranchGroup shakerBranchGroup;
	private ModelObject shakerObject;

	private Vector<Notifiable> observer;
	private NotifyTransformGroup shakerTransformGroup;
	private PoseReceiverShaker shakerReceiver;
	private Receipt receipt;
	private Shape3D shape;
	public Shaker()
	{
		receipt = null;
		j3dObject = new shakerJ3DObject();
		
		// load the object file
	    Scene scene = null;
	    shape = null;
		
		ObjectFile objFileloader = new ObjectFile (ObjectFile.RESIZE);
		
		try
		    {
		      scene = objFileloader.load( "C:/Users/Anders/workspace/3DUI-project/models/shaker.obj" );
		      
		    }
		    catch ( Exception e )
		    {
		      scene = null;
		      System.err.println( e );
		    }

		    if( scene == null )
		      System.exit( 1 );

		    Transform3D t3d = new Transform3D();
		    t3d.setScale(0.05);
		    t3d.setRotation(new AxisAngle4d(1.0, 0.0, 0.0, Math.PI/2));
		    t3d.setTranslation(new Vector3d(0.0, 0.0, 0.05));
		    TransformGroup tg = new TransformGroup();
		    tg.setTransform(t3d);
		    
		    // retrieve the Shape3D object from the scene
		    BranchGroup branchGroup = scene.getSceneGroup( );
		    shape = (Shape3D) branchGroup.getChild(0);
		    BranchGroup bg = new BranchGroup();
		    bg.addChild(tg);
		    tg.addChild(branchGroup);
		    shakerObject = new ModelObject(bg);
		    shakerObject.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		    
		shakerBranchGroup = new BranchGroup();
		shakerTransformGroup = new NotifyTransformGroup();
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		shakerTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);


		shakerBranchGroup.addChild(shakerTransformGroup);
		shakerTransformGroup.addChild(shakerObject);


		observer = new Vector<Notifiable>();
	}

	public void setReceipt(Receipt r)
	{
		receipt = r;
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


