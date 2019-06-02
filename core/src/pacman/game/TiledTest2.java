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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
    private MapLayer gameOverLayer;
    private Rectangle rectanglepacmanY;
    private Rectangle wall;
    private long startTime = 0;
    //private TextureRegion geist;
    private TextureRegion[] ghost = new TextureRegion[4];
    private MapLayer geisterLayer;
    private Texture geistBlau;
    private Texture geistRot, geistRot2;
    private TextureRegion ghost1;
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
    private int[][] punkteKoordinaten = {{1392, 658}, {144, 752}, {1710, 817}, {915, 687}, {592, 596}};
    private int[][] geisterKoordinaten = {{1728, 672}, {40, 776}, {1000, 576}, {608, 608}};
    private int[][] deltaGhosts;
    private Texture gameOver;
    private TextureRegion gameOverRegion;
    private BitmapFont font;

    //ImageButton
    private Texture imageButtonTexture;
    private TextureRegion imageButtonTextureRegion;
    private TextureRegionDrawable imageButtonDrawable;
    private ImageButton button;
    private MapLayer imageButtonLayer;
    private int score = 0;

    //kleine Punkte
    private MapLayer kleinePunkteLayer;
    private Texture smallDot;
    private TextureRegion smallDotRegion;
    private MapLayer smallDotLayer;

    public TiledTest2() {
    }

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        imageButtonTexture = new Texture("StartButton.png");
        imageButtonTextureRegion = new TextureRegion(imageButtonTexture);
        imageButtonDrawable = new TextureRegionDrawable(imageButtonTextureRegion);
        button = new ImageButton(imageButtonDrawable);

        tiledMap = new TmxMapLoader().load("pacmanB.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap);
        imageButtonLayer = tiledMap.getLayers().get("button");

        objectLayer = tiledMap.getLayers().get("objects");
        wallLayer = tiledMap.getLayers().get("wall");
        punkteLayer = tiledMap.getLayers().get("punkte");
        kreuzungLayer = tiledMap.getLayers().get("kreuzung");
        gameOverLayer = tiledMap.getLayers().get("GameOver");
        geisterLayer = tiledMap.getLayers().get("geister");
        kleinePunkteLayer = tiledMap.getLayers().get("scorePunkte");
        //kpLayer = tiledMap.getLayers().get("ScorePunkte");
        smallDotLayer = tiledMap.getLayers().get("smallDot");

        MapLayer layer = tiledMap.getLayers().get("Tiled Punkte");
        texture = new Texture(Gdx.files.internal("pacmanZ.png"));
        geistBlau = new Texture("GeistG.png");
        geistRot = new Texture(Gdx.files.internal("GeistY.png"));
        dot = new Texture(Gdx.files.internal("dotA.png"));
        gameOver = new Texture(Gdx.files.internal("GameOver4.jpg"));
        smallDot = new Texture("dotB.png");

        imageButtonTextureRegion = new TextureRegion(imageButtonTexture,800,350);

        TextureMapObject ib = new TextureMapObject(imageButtonTextureRegion);
        ib.setX(600);
        ib.setY(1000);
        imageButtonLayer.getObjects().add(ib);

        gameOverRegion = new TextureRegion(gameOver,1000,1000);

        //Pacman
        regions[0] = new TextureRegion(texture, 0, 0, 32, 32);     // #3
        regions[1] = new TextureRegion(texture, 32, 0, 32, 32);    // #4
        regions[2] = new TextureRegion(texture, 63, 0, 32, 32);    // #5
        regions[3] = new TextureRegion(texture, 96, 0, 32, 32);    // #5

        animation = new Animation<TextureRegion>(0.2f, regions);

        TextureMapObject tmo = new TextureMapObject(regions[0]);
        tmo.setX(32);
        tmo.setY(480);
        objectLayer.getObjects().add(tmo);

        /*TextureMapObject f = new TextureMapObject(new TextureRegion(geistRot,0,0,32,32));
        f.setX(400);
        f.setY(776);
        geisterLayer.getObjects().add(f);

        TextureMapObject g = new TextureMapObject(new TextureRegion(geistRot,0,0,32,32));
        g.setX(40);
        g.setY(776);
        geisterLayer.getObjects().add(g);
*/
        TextureRegion ghostRegion = new TextureRegion(geistRot,0,0,32,32);
        for (int[] xy : geisterKoordinaten) {
            TextureMapObject d = new TextureMapObject(ghostRegion);
            d.setX(xy[0]);
            d.setY(xy[1]);
            geisterLayer.getObjects().add(d);
        }

        random = new Random();
        deltaGhosts = new int[4][4];
        deltaGhosts[0] = getDirection();
        deltaGhosts[1] = getDirection();
        deltaGhosts[2] = getDirection();
        deltaGhosts[3] = getDirection();
        // Punkte
        TextureRegion punkt = new TextureRegion(dot,0,0,64,64);
        for (int[] xy : punkteKoordinaten) {
            TextureMapObject d = new TextureMapObject(punkt);
            d.setX(xy[0]);
            d.setY(xy[1]);
            punkteLayer.getObjects().add(d);
        }

        batch = new SpriteBatch();
        sprite = new Sprite(geistRot);

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), Gdx.files.internal("font/font.png"), false);

        smallDotRegion = new TextureRegion(smallDot,32,32);

        for (MapObject object: kleinePunkteLayer.getObjects()) {
            RectangleMapObject rmo = (RectangleMapObject)object;
            TextureMapObject sd = new TextureMapObject(smallDotRegion);
            sd.setX(rmo.getRectangle().getX());
            sd.setY(rmo.getRectangle().getY());
            smallDotLayer.getObjects().add(sd);
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
        for (MapObject mapObject : mapObjects) {
            RectangleMapObject rectangleObject = (RectangleMapObject)mapObject;
            float diffX = x - rectangleObject.getRectangle().getX();
            if (diffX < 0) diffX = diffX * -1;

            float diffY = y - rectangleObject.getRectangle().getY();
            if (diffY < 0) diffY = diffY * -1;

            if ((diffX == 0) && (diffY == 0)) return true;
        }
        return false;
    }

    private boolean removeIfOverlapping(Rectangle pacmanRectangle, MapLayer layer) {
        MapObjects mapObjects = layer.getObjects();

        for (MapObject mapObject : mapObjects) {
            if(toRectangleObject(mapObject).overlaps(pacmanRectangle)) {
                mapObjects.remove(mapObject);
                return true;
            }
        }
        return false;
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

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            System.out.println("hallo");
        }

        //ghosts
        moveGhosts();

        //pacman
        movePacman();

        //render
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        batch.begin();

        //text += "1";
        String text = "Score: " + score;
        font.draw(batch, text,  1500, 1550);
        batch.end();
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


        //pacman Bewegung
        Rectangle pacmanRectangle = new Rectangle();
        pacmanRectangle.setX(x + 8);
        pacmanRectangle.setY(y + 8);
        pacmanRectangle.setWidth(20);
        pacmanRectangle.setHeight(20);

        if (!isOverlapping(pacmanRectangle, wallLayer)) {
            character.setX(x);
            character.setY(y);

            if(removeIfOverlapping (pacmanRectangle, geisterLayer)){
                if(startTime == 0){
                    //GameOver Zeichen
                    TextureMapObject go = new TextureMapObject(gameOverRegion);
                    go.setX(500);
                    go.setY(500);
                    gameOverLayer.getObjects().add(go);
                } else {
                    score += 200;
                }
            }

            if (removeIfOverlapping(pacmanRectangle, punkteLayer)) {
                //starte den Timer
                startTime = TimeUtils.millis();

                changeRegion(geistBlau);
            }
        }

        if(TimeUtils.timeSinceMillis(startTime) > 5000) {
            // timer ist nach 5 Sekunden zu Ende
            startTime = 0;

            changeRegion(geistRot);
        }

        if(removeIfOverlapping(pacmanRectangle,smallDotLayer)){
            score += 20;
        }

        //update rotation
        tiledMapRenderer.setRotation(r);
    }

    private void changeRegion(Texture geistBild) {
        for (MapObject object : geisterLayer.getObjects()) {
            TextureMapObject textureMapObject = (TextureMapObject)object;
            textureMapObject.getTextureRegion().setRegion(geistBild);
        }
    }

    private void moveGhosts() {
        MapObjects objects = tiledMap.getLayers().get("geister").getObjects();
        if (objects.getCount() > 0) {
            int i = 0;
            for (MapObject object : objects) {
                TextureMapObject character = (TextureMapObject) object;
                float x = character.getX();
                float y = character.getY();

                int[] delta = getDirection();

                y += deltaGhosts[i][0];
                x += deltaGhosts[i][1];

                Rectangle rectangle = new Rectangle();
                rectangle.setX(x);
                rectangle.setY(y);
                rectangle.setWidth(32);
                rectangle.setHeight(32);

                if (!isOverlapping(rectangle, wallLayer)) {
                    character.setX(x);
                    character.setY(y);
                } else {
                    deltaGhosts[i] = getDirection();
                }

                if (isKreuzung(x, y, kreuzungLayer)){
                    deltaGhosts[i] = getDirection();
                }
                i++;
            }
        }
    }

    private int[] getDirection() {
        return delta[random.nextInt(4)];
    }
}



