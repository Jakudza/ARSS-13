package ar.tum.de.main;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.TransformableObject;

public class CubeObject extends TransformableObject {
	
	public CubeObject(float xSize, float ySize, float zSize, Appearance app) {
		TransformGroup boxOffset = new TransformGroup();
		Transform3D t3dOffset = new Transform3D();
		t3dOffset.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		boxOffset.setTransform(t3dOffset);
		transGroup.addChild(boxOffset);
		
		Box blueBox = new Box(xSize, ySize, zSize, app);
		
		boxOffset.addChild(blueBox);
	}

	
}

