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
    private MapLayer punkteLayer;
    private MapLayer kreuzungLayer;

    //Animation<TextureRegion> animation;
    private TextureRegion[] regions = new TextureRegion[4];
    private float stateTime;
    private Animation<TextureRegion> animation;
    private int x = 0;
    private int y = 0;
    private float r = 0;
    private Random random;
    private int[][] delta = {{0,-8},{0,8},{8,0}, {-8,0}};
    private int[][] punkteKoordinaten = {{18, 460}, {460, 720}, {1710, 820}, {920, 685}, {592, 600}};
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
        punkteLayer = tiledMap.getLayers().get("punkte");
        kreuzungLayer = tiledMap.getLayers().get("kreuzung");

        MapObjects objects = kreuzungLayer.getObjects();
        for (MapObject object : objects) {
            RectangleMapObject retangleObject = (RectangleMapObject)object;
            System.out.println("X:" + ((RectangleMapObject) object).getRectangle().getX());
            System.out.println("Y:" + ((RectangleMapObject) object).getRectangle().getY());
            System.out.println("width:" + ((RectangleMapObject) object).getRectangle().getWidth());
            System.out.println("height:" + ((RectangleMapObject) object).getRectangle().getHeight());
        }
        System.out.println("");

        MapLayer layer = tiledMap.getLayers().get("Tiled Punkte");
        texture = new Texture(Gdx.files.internal("pacmanZ.png"));
        geist = new Texture(Gdx.files.internal("GeistY.png"));
        geisterLayer = tiledMap.getLayers().get("geister");
        dot = new Texture(Gdx.files.internal("dotA.png"));


        regions[0] = new TextureRegion(texture, 0, 0, 32, 32);     // #3
        regions[1] = new TextureRegion(texture, 32, 0, 32, 32);    // #4
        regions[2] = new TextureRegion(texture, 63, 0, 32, 32);    // #5
        regions[3] = new TextureRegion(texture, 96, 0, 32, 32);    // #5

        animation = new Animation<TextureRegion>(0.2f, regions);

        TextureMapObject tmo = new TextureMapObject(regions[0]);
        tmo.setX(32);
        tmo.setY(480);
        objectLayer.getObjects().add(tmo);

        ghost[0] = new TextureRegion(geist,0,0,32,32);

        TextureMapObject g = new TextureMapObject(ghost[0]);
        g.setX(40);
        g.setY(776);
        geisterLayer.getObjects().add(g);

        random = new Random();
        deltaGhosts = getDirection();

        // Punkte
        TextureRegion punkt = new TextureRegion(dot,0,0,64,64);
        for (int[] xy : punkteKoordinaten) {
            TextureMapObject d = new TextureMapObject(punkt);
            d.setX(xy[0]);
            d.setY(xy[1]);
            punkteLayer.getObjects().add(d);
        }
    }

    private boolean isOverlapping(Rectangle rectangle, MapLayer layer) {
        MapObjects retangles = layer.getObjects();

        for (MapObject retangle : retangles) {
            if (((RectangleMapObject) retangle).getRectangle().overlaps(rectangle)) {
                return true;
            }
        }
        return false;
    }

    private boolean isKreuzung(float x, float y, MapLayer layer) {
        MapObjects mapObjects = layer.getObjects();
        System.out.println(">isOverlapping");
        System.out.println(x + "/" + y);
        for (MapObject mapObject : mapObjects) {
            RectangleMapObject rectangleObject = (RectangleMapObject)mapObject;
            System.out.println(rectangleObject.getRectangle().getX() + "/" + rectangleObject.getRectangle().getY());
            float diffX = x - rectangleObject.getRectangle().getX();
            if (diffX < 0) diffX = diffX * -1;

            float diffY = y - rectangleObject.getRectangle().getY();
            if (diffY < 0) diffY = diffY * -1;

            System.out.println(diffX + "/" + diffY);
            System.out.println("");
            if ((diffX == 0) && (diffY == 0)) return true;
        }
        System.out.println("<isOverlapping");
        return false;
    }

    private void removeIfOverlapping(Rectangle pacmanRectangle, MapLayer layer) {
        MapObjects mapObjects = layer.getObjects();

        for (MapObject mapObject : mapObjects) {
            if(toRectangleObject(mapObject).overlaps(pacmanRectangle)) {
                mapObjects.remove(mapObject);
            }
        }
    }

    private Rectangle toRectangleObject(MapObject mapObject) {
        TextureMapObject d = (TextureMapObject)mapObject;
        return new Rectangle(d.getX(), d.getY(), 32, 32);
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
                x -= 8;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            r = 0;
            if (x > 1800) {
                x = x;
            } else {
                x += 8;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            r = 90;
            if (y > 1400) {
                y = y;
            } else {
                y += 8;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            r = 270;
            if (y < 0) {
                y = y;
            } else {
                y -= 8;
            }
        }

        Rectangle rectangle = new Rectangle();
        rectangle.setX(x + 8);
        rectangle.setY(y + 8);
        rectangle.setWidth(20);
        rectangle.setHeight(20);

        if (!isOverlapping(rectangle, wallLayer)) {
            character.setX(x);
            character.setY(y);

            removeIfOverlapping(rectangle, geisterLayer);
            removeIfOverlapping(rectangle, punkteLayer);

            /*System.out.println("pacman");
            System.out.println(x);
            System.out.println(y);
            System.out.println("");*/
        }

        //update rotation
        tiledMapRenderer.setRotation(r);
    }

    private void moveGhosts() {
        MapObjects objects = tiledMap.getLayers().get("geister").getObjects();
        if (objects.getCount() > 0) {
            TextureMapObject character = (TextureMapObject) objects.get(0);
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
            rectangle.setWidth(32);
            rectangle.setHeight(32);

            if (!isOverlapping(rectangle, wallLayer)) {
                character.setX(x);
                character.setY(y);

                /*System.out.println("gosts");
                System.out.println(x);
                System.out.println(y);
                System.out.println("");*/
            } else {
                deltaGhosts = getDirection();
            }

            if (isKreuzung(x, y, kreuzungLayer)){
                deltaGhosts = getDirection();
            }
        }
    }

    private int[] getDirection() {
        return delta[random.nextInt(4)];
    }
}



