package ar.de.tum.animations;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

public class FallingAnimation extends BranchGroup {

	private Alpha a = new Alpha(1, 5000); 
	
	public FallingAnimation(BranchGroup targetObject) {
		TransformGroup targetTransformGroup = new TransformGroup();
		targetTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		targetTransformGroup.addChild(targetObject);
		addChild(targetTransformGroup);
		
		Transform3D trans = new Transform3D();
		trans.rotY(3* Math.PI / 2);
		
		a.setStartTime(System.currentTimeMillis());
		PositionInterpolator positionInterpolation = new PositionInterpolator(a, targetTransformGroup, trans, 0f,-0.25f);

		BoundingSphere bounds = new BoundingSphere();
		bounds.setRadius(40.0);
		positionInterpolation.setSchedulingBounds(bounds);
		
		addChild(positionInterpolation);
	}		
	
	public boolean finished(){
		return a.finished();
	}
}
