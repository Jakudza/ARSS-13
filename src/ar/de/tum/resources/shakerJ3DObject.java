package ar.de.tum.resources;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.GreenAppearance;
import de.tum.in.far.threedui.general.TransformableObject;

public class shakerJ3DObject extends TransformableObject {
	
	public shakerJ3DObject() {
		Appearance greenAppearance = new GreenAppearance();
		TransformGroup boxOffset = new TransformGroup();
		Transform3D t3dOffset = new Transform3D();
		t3dOffset.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		boxOffset.setTransform(t3dOffset);
		transGroup.addChild(boxOffset);
		
		Box blueBox = new Box(0.01f, 0.01f, 0.01f, greenAppearance);
		
		boxOffset.addChild(blueBox);
	}
}
