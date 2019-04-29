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

import java.util.Random;

public class TiledTest2 extends ApplicationAdapter {
    private Texture img;
    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
    private SpriteBatch batch;
    private Texture texture;
    private Texture dot;
    private Sprite sprite;
    private MapLayer objectLayer;
    private MapLayer wallLayer;
    private Rectangle rectanglepacmanY;
    private Rectangle wall;
    //private TextureRegion geist;
    private Texture geist;
    private TextureRegion[] ghost = new TextureRegion[1];
    private MapLayer geisterLayer;
    // Punkte
    private TextureRegion[] punkt = new TextureRegion[4];
    private MapLayer punktobject;

    //Animation<TextureRegion> animation;
    private TextureRegion[] regions = new TextureRegion[4];
    private float stateTime;
    private Animation<TextureRegion> animation;
    private int x = 0;
    private int y = 0;
    private float r = 0;
    private Random random;
    private int[][] delta = {{0,-10},{0,10},{10,0}, {-10,0}};
    private int[] deltaGhosts;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("pacmanB.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);

        objectLayer = tiledMap.getLayers().get("objects");
        wallLayer = tiledMap.getLayers().get("wall");
        MapLayer layer = tiledMap.getLayers().get("Tiled Punkte");
        texture = new Texture(Gdx.files.internal("pacmanZ.png"));
        geist = new Texture(Gdx.files.internal("GeistY.png"));
        geisterLayer = tiledMap.getLayers().get("geister");
        dot = new Texture(Gdx.files.internal("dotA.png"));
        punktobject = tiledMap.getLayers().get("punkte");



        regions[0] = new TextureRegion(texture, 0, 0, 32, 32);        // #3
        regions[1] = new TextureRegion(texture, 32, 0, 32, 32);    // #4
        regions[2] = new TextureRegion(texture, 63, 0, 32, 32);        // #5
        regions[3] = new TextureRegion(texture, 96, 0, 32, 32);        // #5

        animation = new Animation<TextureRegion>(0.2f, regions);

        TextureMapObject tmo = new TextureMapObject(regions[0]);
        tmo.setX(30);
        tmo.setY(470);
        objectLayer.getObjects().add(tmo);

        ghost[0] = new TextureRegion(geist,0,0,32,32);

        TextureMapObject g = new TextureMapObject(ghost[0]);
        g.setX(40);
        g.setY(670);
        geisterLayer.getObjects().add(g);

        random = new Random();
        deltaGhosts = getDirection();

        // Punkte
        punkt[0] = new TextureRegion(dot,0,0,64,64);

        TextureMapObject d = new TextureMapObject(punkt[0]);
        d.setX(18);
        d.setY(460);
        punktobject.getObjects().add(d);

        punkt[1] = new TextureRegion(dot,0,0,64,64);

        TextureMapObject f = new TextureMapObject(punkt[1]);
        f.setX(460);
        f.setY(720);
        punktobject.getObjects().add(f);

        punkt[2] = new TextureRegion(dot,0,0,64,64);

        TextureMapObject j = new TextureMapObject(punkt[2]);
        j.setX(1710);
        j.setY(820);
        punktobject.getObjects().add(j);

        punkt[3] = new TextureRegion(dot,0,0,64,64);

        TextureMapObject k = new TextureMapObject(punkt[3]);
        k.setX(920);
        k.setY(685);
        punktobject.getObjects().add(k);
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
        texture.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //ghosts
        moveGhosts();

        //pacman
        movePacman();

        //render
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    private void movePacman() {
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
            if (x > 1800) {
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
        rectangle.setWidth(20);
        rectangle.setHeight(20);

        if (!isOverlapping(rectangle)) {
            character.setX(x);
            character.setY(y);
        }

        //update rotation
        tiledMapRenderer.setRotation(r);
    }

    private void moveGhosts() {
        TextureMapObject character = (TextureMapObject) tiledMap.getLayers().get("geister").getObjects().get(0);
        float x = character.getX();
        float y = character.getY();

        int[] delta = getDirection();

        y = y;
        y += deltaGhosts[0];

        x = x;
        x += deltaGhosts[1];

        Rectangle rectangle = new Rectangle();
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setWidth(20);
        rectangle.setHeight(20);

        if (!isOverlapping(rectangle)) {
            character.setX(x);
            character.setY(y);
        } else {
            deltaGhosts = getDirection();
        }
    }

    private int[] getDirection() {
        return delta[random.nextInt(4)];
    }
}



