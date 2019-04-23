package pacman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;


public class TiledTest2 extends ApplicationAdapter {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
    SpriteBatch batch;
    Texture texture;
    private Sprite sprite;
    MapLayer objectLayer;
    MapLayer wallLayer;
    private Rectangle rectanglepacmanY;
    private Rectangle wall;

    //Animation<TextureRegion> animation;
    TextureRegion[] regions = new TextureRegion[4];
    private float stateTime;
    private Animation<TextureRegion> animation;
    int x = 0;
    int y = 0;
    float r = 0;


    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("pacmanA.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);

        objectLayer = tiledMap.getLayers().get("objects");
        wallLayer = tiledMap.getLayers().get("wall");
        texture = new Texture(Gdx.files.internal("pacmanY.png"));

        regions[0] = new TextureRegion(texture, 0, 0, 64, 64);        // #3
        regions[1] = new TextureRegion(texture, 64, 0, 64, 64);    // #4
        regions[2] = new TextureRegion(texture, 128, 0, 64, 64);        // #5
        regions[3] = new TextureRegion(texture, 192, 0, 64, 64);        // #5

        animation = new Animation<TextureRegion>(0.2f, regions);

        TextureMapObject tmo = new TextureMapObject(regions[0]);
        tmo.setX(200);
        tmo.setY(200);
        objectLayer.getObjects().add(tmo);

    }

    private boolean isOverlapping(Rectangle rectangle) {
        MapObjects retangles = wallLayer.getObjects();

        for (MapObject retangle : retangles) {
            if (((RectangleMapObject) retangle).getRectangle().overlaps(rectangle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dispose() {
        //batch.dispose();
        texture.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        TextureMapObject character = (TextureMapObject) tiledMap.getLayers().get("objects").getObjects().get(0);
        float x = character.getX();
        float y = character.getY();

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        character.setTextureRegion(currentFrame);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            r = 180;
            if (x <= 0) {
                x = x;
            } else {
                x -= 10;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            r = 0;
            if (x > 1700) {
                x = x;
            } else {
                x += 10;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            r = 90;
            if (y > 1400) {
                y = y;
            } else {
                y += 10;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            r = 270;
            if (y < 0) {
                y = y;
            } else {
                y -= 10;
            }
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setX(x + 10);
        rectangle.setY(y + 10);
        rectangle.setWidth(40);
        rectangle.setHeight(40);

        if (!isOverlapping(rectangle)) {
            character.setX(x);
            character.setY(y);
        }

        //update rotation
        tiledMapRenderer.setRotation(r);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }




        //@Override
    public boolean keyUp ( int keycode){
        return false;
    }

    //@Override
    public boolean keyTyped ( char keycode){
        System.out.println(keycode);
        return false;
    }

    //@Override
    public boolean touchDown ( int screenX, int screenY, int pointer, int button){
        return true;
    }

    //@Override
    public boolean touchUp ( int screenX, int screenY, int pointer, int button){
            return false;
        }

    //@Override
    public boolean touchDragged ( int screenX, int screenY, int pointer){
            return false;
        }

    //@Override
    public boolean mouseMoved ( int screenX, int screenY){
            return false;
        }

    //@Override
    public boolean scrolled ( int amount){
            return false;
        }


}



