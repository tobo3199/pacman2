package pacman.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

public class Pacman implements ApplicationListener {
	private SpriteBatch batch;
	private Texture texture;
	private Sprite sprite;
	private TextureRegion[] regions = new TextureRegion[3];
	private Animation<TextureRegion> animation;
	private float stateTime;
	int x = 0;
	int y = 0;

	TiledMap tiledMap;
	OrthographicCamera camera;
	TiledMapRenderer tiledMapRenderer;

	@Override
	public void create() {
		batch = new SpriteBatch();
		texture = new Texture(Gdx.files.internal("pacman5.0.png"));

		regions[0] = new TextureRegion(texture, 0, 0, 16, 16);        // #3
		regions[1] = new TextureRegion(texture, 16, 0, 16, 16);    // #4
		regions[2] = new TextureRegion(texture, 32, 0, 16, 16);        // #5
		regions[3] = new TextureRegion(texture, 48, 0, 16, 16);        // #5

		animation = new Animation<TextureRegion>(0.2f, regions);
		stateTime = 0f;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
	}

	private void flipX() {
		for (TextureRegion region : regions)
			region.flip(true, false);
	}

	private void flipY() {
		for (TextureRegion region : regions)
			region.flip(false, true);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stateTime += Gdx.graphics.getDeltaTime();

		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
		sprite = new Sprite(currentFrame);

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (x <= 0) {
				x = x;
			} else {
				x -= 10;

				if (!sprite.isFlipX()) {
					flipX();
				}
			}

		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (x > 1600) {
				x = x;
			} else {
				x += 10;

				if (sprite.isFlipX()) {
					flipX();
				}
			}

		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (y > 1280) {
				y = y;
			} else {
				y += 10;
			}
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (y < 0) {
				y = y;
			} else {
				y -= 10;
			}
		}


		batch.begin();
		batch.draw(sprite,x,y);
		batch.end();

}


	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}