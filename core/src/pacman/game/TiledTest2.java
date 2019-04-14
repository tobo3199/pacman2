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
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


public class TiledTest2 extends ApplicationAdapter {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    SpriteBatch sb;
    Texture texture;
    private Sprite sprite;
    MapLayer objectLayer;
    MapLayer wallLayer;

    //Animation<TextureRegion> animation;
    TextureRegion[] regions = new TextureRegion[4];
    private float stateTime;
    private Animation<TextureRegion> animation;
    int x = 0;
    int y = 0;
    private SpriteBatch batch;
    int translate;


    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("pacmanA.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
        //Gdx.input.setInputProcessor(this);

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

        MapObjects retangles = wallLayer.getObjects();
        Rectangle rectangle = new Rectangle();

        for (MapObject retangle : retangles) {
            if (((RectangleMapObject) retangle).getRectangle().overlaps(rectangle)) {
                System.out.println("Overlap");
            } else {
                //System.out.println("Not Overlap");
            }
        }
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

        TextureMapObject character = (TextureMapObject)tiledMap.getLayers().get("objects").getObjects().get(0);
        float x = character.getX();
        float y = character.getY();

        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        character.setTextureRegion(currentFrame);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (x <= 0) {
                x = x;
            } else {
                x -= 10;

                /*if (!sprite.isFlipX()) {
                    flipX();
                }*/
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (x > 1600) {
                x = x;
            } else {
                x += 10;

                /*if (sprite.isFlipX()) {
                    flipX();
                }*/
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (y > 1400) {
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

        character.setX(x);
        character.setY(y);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();


        //sprite = new Sprite(currentFrame);

        // batch.begin();
       // batch.draw(sprite,x,y);
       // batch.end();
    }

    //@Override
    public boolean keyDown(int keycode) {
        /*System.out.println(keycode);
        TextureMapObject character = (TextureMapObject)tiledMap.getLayers().get("objects").getObjects().get(0);
        float x = character.getX();
        float y = character.getY();

        if(keycode == Input.Keys.LEFT) {
            if (x <= 0) {
                x = x;
            } else {
                x -= 10;
            }
        }

        if(keycode == Input.Keys.RIGHT) {
            if (x > 1600) {
                x = x;
            } else {
                x += 10;
            }
        }

        if(keycode == Input.Keys.UP) {
            if (y > 1280) {
                y = y;
            } else {
                y += 10;
            }
        }

        if(keycode == Input.Keys.DOWN) {
            if (y < 0) {
                y = y;
            } else {
                y -= 10;
            }
        }

        character.setX(x);
        character.setY(y);*/
        return false;
    }

    //@Override
    public boolean keyUp(int keycode) {
        System.out.println(keycode);
        //TextureMapObject character = (TextureMapObject)tiledMap.getLayers().get("objects").getObjects().get(0);
        //character.getRectangle().set(position.x, position.y, 10, 10);
        //character.setX(x);
        //character.setY(y);
        //if(keycode == Input.Keys.LEFT)
        //texture.translate(-32,0);
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

    //@Override
    public boolean keyTyped(char keycode){
        System.out.println(keycode);
        return false;
    }

    //@Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
        Vector3 position = camera.unproject(clickCoordinates);
        TextureMapObject character = (TextureMapObject)tiledMap.getLayers().get("objects").getObjects().get(0);
        //character.getRectangle().set(position.x, position.y, 10, 10);
        character.setX((float)position.x);
        character.setY((float)position.y);
        return true;
    }

    //@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    //@Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    //@Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //@Override
    public boolean scrolled(int amount) {
        return false;
    }
}
