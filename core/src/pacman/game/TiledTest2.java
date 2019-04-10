package pacman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


public class TiledTest2 extends ApplicationAdapter implements InputProcessor {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    Texture texture;
    Sprite sprite;
    MapLayer objectLayer;
    MapLayer wallLayer;
    TextureRegion[] regions = new TextureRegion[3];
    Animation<TextureRegion> animation;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("pacmanA.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
        Gdx.input.setInputProcessor(this);

        objectLayer = tiledMap.getLayers().get("objects");
        wallLayer = tiledMap.getLayers().get("wall");
        texture = new Texture(Gdx.files.internal("pacmanY.png"));

        regions[0] = new TextureRegion(texture, 0, 0, 192, 193);        // #3
        regions[1] = new TextureRegion(texture, 192, 0, 192, 193);    // #4
        regions[2] = new TextureRegion(texture, 384, 0, 192, 193);        // #5

        animation = new Animation<TextureRegion>(0.2f, regions);

        TextureMapObject tmo = new TextureMapObject(regions[0]);
        tmo.setX(0);
        tmo.setY(0);
        objectLayer.getObjects().add(tmo);


        //private Box2DDebugRenderer b2dr;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //if(keycode == Input.Keys.LEFT)
        //camera.translate(-32,0);
        //if(keycode == Input.Keys.RIGHT)
        //camera.translate(32,0);
        //if(keycode == Input.Keys.UP)
        //camera.translate(0,-32);
        //if(keycode == Input.Keys.DOWN)
        //camera.translate(0,32);
        //if(keycode == Input.Keys.NUM_1)
        //tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        //if(keycode == Input.Keys.NUM_2)
        //tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    @Override
    public boolean keyTyped(char character){

        return false;
        }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 position = camera.unproject(clickCoordinates);
        TextureMapObject character = (TextureMapObject)tiledMap.getLayers().get("objects").getObjects().get(0);
        //character.getRectangle().set(position.x, position.y, 10, 10);
        character.setX((float)position.x);
        character.setY((float)position.y);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
