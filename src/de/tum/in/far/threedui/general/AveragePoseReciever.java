package de.tum.in.far.threedui.general;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import ubitrack.SimplePose;
import ubitrack.SimplePoseReceiver;

public class AveragePoseReciever extends SimplePoseReceiver {
	protected TransformGroup markerTransGroup = null;
	Vector3d[] positions;
			
	public AveragePoseReciever(int averaging){
		positions = new Vector3d[averaging];
	}
			
	public void setTransformGroup(TransformGroup markerTransGroup) {
		this.markerTransGroup = markerTransGroup;
	}
	
	public void receivePose(SimplePose pose) {
		if (markerTransGroup == null){
			return;
		}

		double[] trans = new double[3];
		double[] rot = new double[4];
		trans[0] = pose.getTx();
		trans[1] = pose.getTy();
		trans[2] = pose.getTz();
		rot[0] = pose.getRx();
		rot[1] = pose.getRy();
		rot[2] = pose.getRz();
		rot[3] = pose.getRw();
		
		for (int  i = positions.length - 1; i > 0; i--) {
			positions[i] = positions[i-1];
		}
	
		positions[0] =  new Vector3d(pose.getTx(), pose.getTy(), pose.getTz());
		Quat4d quat = new Quat4d(pose.getRx(), pose.getRy(), pose.getRz(), pose.getRw());

		int i = 1;
		Vector3d sum = positions[0]; 
		for (i = 1; i < positions.length - 1 && positions[i] != null; i++) {
			sum.add(positions[i]);
		}
		sum.scale((double)1/i);
		Transform3D markerTransform = new Transform3D();
		markerTransform.set(quat, sum, 1);
		markerTransGroup.setTransform(markerTransform);
	}
}
