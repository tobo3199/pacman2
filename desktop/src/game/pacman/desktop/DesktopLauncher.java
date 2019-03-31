package game.pacman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pacman.game.Pacman;
import pacman.game.TiledTest;
import pacman.game.TiledTest2;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "My game";
		config.width = 1800;
		config.height = 1480;
		new LwjglApplication(new TiledTest2(), config);
		//new LwjglApplication(new Pacman(), config);
	}
}
