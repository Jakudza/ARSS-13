package ar.de.tum.resources;

import java.io.File;

public class FruitResources {

	public final static String COMMON_PATH = "models" + File.separator;
	
	public class Score {
		public int mushroom = 10;
		public int apple = 20;
		public int banana = 30;
		public int tabasco = 50;
    }
	
	public class ModelPath {
		public String tabasco = COMMON_PATH + "tabasco2.wrl";
		public String apple = COMMON_PATH + "apple2.wrl";
		public String banana = COMMON_PATH + "banana2.wrl";
		public String mushroom = COMMON_PATH + "muscroom2.wrl";
	}
	
	public class Scale {
		public float mushroom = 0.25f;
		public float apple = 0.25f;
		public float banana = 0.25f;
		public float tabasco = 0.01f;
    }
	
	public Score score = new Score();
	public ModelPath path = new ModelPath();
	public Scale scale = new Scale();
}
