package de.tum.in.far.threedui.general;

import java.awt.Font;
import java.awt.Shape;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3f;

public class TextDrawer extends BranchGroup {

	protected Text3D text3d;
	
	private final static String SCORE ="Score :";
	private final static String GAME_OVER ="Game over!";
	private final static String SHAKE_IT ="Shake it!";
	private final static String FINAL_SCORE ="Your final score: ";
	
	private final static double TEXT_SCALE = 0.03;
	
	public TextDrawer(Appearance app) {
		double X1 = 0;
		double Y1 = 0;
		double X2 = 0.05;
		double Y2 = 0;
		Shape extrusionShape = new java.awt.geom.Line2D.Double(X1, Y1, X2, Y2);
		FontExtrusion fontEx = new FontExtrusion(extrusionShape);
		Font3D f3d = new Font3D(new Font("Arial", Font.BOLD, 1), fontEx);
		text3d = new Text3D(f3d, SCORE, new Point3f(0.0f, 1.0f, 0.0f), 	Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
		text3d.setCapability(Text3D.ALLOW_STRING_READ);
		text3d.setCapability(Text3D.ALLOW_STRING_WRITE);
		Shape3D shape = new Shape3D(text3d, app);
		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(TEXT_SCALE);
		tg.setTransform(t3d);
		tg.addChild(shape);
		addChild(tg);
	}
	
	public void changeScore(int score){
		text3d.setString(TextDrawer.SCORE + score);
	}
	
	public void shaked() {
		text3d.setString(SHAKE_IT);
	}
	
	public void finalScore(int score) {
		text3d.setString(FINAL_SCORE + score);
	}
	
	public void gameOver() {
		text3d.setString(GAME_OVER);
	}
}
