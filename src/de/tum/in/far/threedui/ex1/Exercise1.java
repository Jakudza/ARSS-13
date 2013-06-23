package de.tum.in.far.threedui.ex1;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.Box;

import de.tum.in.far.threedui.general.BinaryEnv;
import de.tum.in.far.threedui.general.BlueAppearance;
import de.tum.in.far.threedui.general.FakeShadow;
import de.tum.in.far.threedui.general.Viewer;

public class Exercise1 {

	public static final String EXERCISE = "Exercise 1";

	static {
		BinaryEnv.init();
	}
	
	public static void main(String[] args) {
		Exercise1 exercise1 = new Exercise1();
		exercise1.initializeJava3D();
	}
	
	private void initializeJava3D() {
		System.out.println("Creating Viewer - " + EXERCISE);
		Viewer viewer = new Viewer(EXERCISE);

		System.out.println("Create Sphere Object");
		SphereObject sphereObject = new SphereObject(0.1f);
		viewer.addObject(sphereObject);
		
		Transform3D sphereT3D = new Transform3D();
		sphereT3D.setTranslation(new Vector3d(0.0, 0.2, 0.0));
		sphereObject.getTransformGroup().setTransform(sphereT3D);
		
		System.out.println("Move Camera backwards and a little bit up");
		Transform3D cameraTransform = new Transform3D();
		cameraTransform.setTranslation(new Vector3d(0.0, 0.15, 1.0));
		TransformGroup cameraTG = viewer.getCameraTransformGroup();
		cameraTG.setTransform(cameraTransform);
		
		System.out.println("Create Blue Sphere Object");
		BlueAppearance blueAppearance = new BlueAppearance();
		SphereObject blueSphereObject = new SphereObject(0.04f, blueAppearance);

		Transform3D blueSphereT3D = new Transform3D();
		blueSphereT3D.setTranslation(new Vector3d(0.2, 0.2, 0.0));
		blueSphereObject.getTransformGroup().setTransform(blueSphereT3D);
		//-------
		// adding light
		PointLight light = new PointLight();
		BoundingSphere worldBounds = new BoundingSphere(new Point3d(0.0, 0.0,
		        0.0), // Center
		        1000.0); // Extent

		    // Set the light color and its influencing bounds
		    light = new PointLight();
		    light.setEnable(true);
		    light.setColor(new Color3f(1,0,0));
		    light.setCapability(PointLight.ALLOW_STATE_WRITE);
		    light.setCapability(PointLight.ALLOW_COLOR_WRITE);
		    light.setCapability(PointLight.ALLOW_POSITION_WRITE);
		    light.setCapability(PointLight.ALLOW_ATTENUATION_WRITE);
		    light.setInfluencingBounds(worldBounds);
		blueSphereObject.getTransformGroup().addChild(light);
		//------------			
		
		//-----------
		BranchGroup bg = new BranchGroup();
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		Transform3D transform3d = new Transform3D();
		transform3d.setTranslation(new Vector3d(0,0,0));
		tg.setTransform(transform3d);
		Box box = new Box(0.4f,0.1f,0.1f, new BlueAppearance());
		tg.addChild(box);
		bg.addChild(tg);
		viewer.addObject(bg);
		//-----------
		
		//------------
		FakeShadow fs = new FakeShadow(blueSphereObject.getGeometry(), new Color3f(1,1,1));
		viewer.addObject(fs);
		//--------------
		System.out.println("Animation");
		AnimationRotation animationRotation = new AnimationRotation(blueSphereObject);
		viewer.addObject(animationRotation);
		
		System.out.println("Done - Enjoy");
	}
	

}
