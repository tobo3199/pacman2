package pacman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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

public class TiledTest2 extends ApplicationAdapter implements InputProcessor {
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
    private boolean stop = false;
    private boolean e = false;
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
    private int[][] geisterKoordinaten2 = {{1728, 672}, {40, 776}, {1000, 576}, {608, 608}, {1280, 832}, {480, 736}};
    private int[][] deltaGhosts;
    private Texture gameOver;
    private TextureRegion gameOverRegion;
    private BitmapFont font;


    //ImageButton
    private Texture imageButtonTexture;
    private TextureRegion imageButtonTextureRegion;
    private TextureRegionDrawable imageButtonDrawable;
    private MapLayer imageButtonLayer;
    private Texture startButton;
    private Texture stopButton;
    private Texture restartButton;
    private TextureRegion startRegion;
    private TextureRegion restartRegion;
    private TextureRegion stopRegion;

    //Level 1 & 2
    private Texture Level1Texture;
    private Texture Level2Texture;
    private  TextureRegion Level1Region;
    private TextureRegion Level2Region;

    //Scoreboard
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

        imageButtonTexture = new Texture("ButtonStart.png");
        imageButtonTextureRegion = new TextureRegion(imageButtonTexture);
        imageButtonDrawable = new TextureRegionDrawable(imageButtonTextureRegion);


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
        gameOver = new Texture(Gdx.files.internal("GameOver.jpg"));
        smallDot = new Texture("dotB.png");

        //startButton
        startButton = new Texture(Gdx.files.internal("ButtonStart1.png"));
        startRegion = new TextureRegion(startButton);

        TextureMapObject st = new TextureMapObject(startRegion);
        st.setX(-100);
        st.setY(1265);
        imageButtonLayer.getObjects().add(st);

        //restartButton
        restartButton = new Texture(Gdx.files.internal("ButtonRestart1.png"));
        restartRegion = new TextureRegion(restartButton);

        TextureMapObject re = new TextureMapObject(restartRegion);
        re.setX(1020);
        re.setY(1400);
        imageButtonLayer.getObjects().add(re);

        //Level 1
        Level1Texture = new Texture(Gdx.files.internal("Level1.png"));
        Level1Region = new TextureRegion(Level1Texture);

        TextureMapObject lr = new TextureMapObject(Level1Region);
        lr.setX(50);
        lr.setY(100);
        imageButtonLayer.getObjects().add(lr);

        //Level 2
        Level2Texture = new Texture(Gdx.files.internal("Level2.png"));
        Level2Region = new TextureRegion(Level2Texture);

        TextureMapObject l2 = new TextureMapObject(Level2Region);
        l2.setX(500);
        l2.setY(100);
        imageButtonLayer.getObjects().add(l2);

        //restartRegion = new TextureRegion(restartButton,800,350);

        //stopButton
        stopButton = new Texture(Gdx.files.internal("ButtonStop1.png"));
        stopRegion = new TextureRegion(stopButton);

        TextureMapObject sto = new TextureMapObject(stopRegion);
        sto.setX(550);
        sto.setY(1400);
        imageButtonLayer.getObjects().add(sto);

        //gameOverBild
        gameOverRegion = new TextureRegion(gameOver,250,200);

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
        font.getData().setScale(2);

        smallDotRegion = new TextureRegion(smallDot,32,32);

        for (MapObject object: kleinePunkteLayer.getObjects()) {
            RectangleMapObject rmo = (RectangleMapObject)object;
            TextureMapObject sd = new TextureMapObject(smallDotRegion);
            sd.setX(rmo.getRectangle().getX());
            sd.setY(rmo.getRectangle().getY());
            smallDotLayer.getObjects().add(sd);
        }

        score = 0;

        Gdx.input.setInputProcessor(this);
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

        if (!stop) {
            //ghosts
            moveGhosts();

            //pacman
            movePacman();

        }

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
        pacmanRectangle.setX(x + 9);
        pacmanRectangle.setY(y + 9);
        pacmanRectangle.setWidth(16);
        pacmanRectangle.setHeight(16);

        if (!isOverlapping(pacmanRectangle, wallLayer)) {
            character.setX(x);
            character.setY(y);

            if(removeIfOverlapping (pacmanRectangle, geisterLayer)){
                if(startTime == 0){
                    //GameOver Zeichen
                    TextureMapObject go = new TextureMapObject(gameOverRegion);
                    go.setX(700);
                    go.setY(950);
                    gameOverLayer.getObjects().add(go);

                    stop = true;

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

        if(TimeUtils.timeSinceMillis(startTime) > 8000) {
            // timer ist nach 8 Sekunden zu Ende
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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp" + screenX);
        System.out.println("touchUp" + screenY);
        if (button == 0) {
            //startButton
            if (screenX >= 90 && screenX <= 520 &&  screenY >= 180 && screenY <= 360) {
                System.out.println("Start Button");
                    stop = false;
            }

            //restartButton
            if (screenX >= 1020 && screenX <= 1430 && screenY >= 135 && screenY <= 300) {
                create();
                stop = true;
            }
            //stopButton
            if (screenX <= 960 && screenX >= 550 && screenY >= 150 && screenY <= 300) {
                stop = true;
            }
            //Level1
            if (screenX <= 460 && screenX >= 50 && screenY >= 1450 && screenY <= 1600) {
                stop = true;

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

            }
            //Level2
            if (screenX <= 910 && screenX >= 500 && screenY >= 1450 && screenY <= 1600) {
                stop = true;

                TextureRegion ghostRegion = new TextureRegion(geistRot,0,0,32,32);
                for (int[] xy : geisterKoordinaten2) {
                    TextureMapObject d = new TextureMapObject(ghostRegion);
                    d.setX(xy[0]);
                    d.setY(xy[1]);
                    geisterLayer.getObjects().add(d);
                }

                random = new Random();
                deltaGhosts = new int[6][6];
                deltaGhosts[0] = getDirection();
                deltaGhosts[1] = getDirection();
                deltaGhosts[2] = getDirection();
                deltaGhosts[3] = getDirection();
                deltaGhosts[4] = getDirection();
                deltaGhosts[5] = getDirection();
            }

        }
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



