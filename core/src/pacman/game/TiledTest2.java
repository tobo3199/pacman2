package pacman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
    private TextureRegion[] ghost = new TextureRegion[1];
    private MapLayer geisterLayer;
    private Texture geistBlau;
    private Texture geistRot;
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
    private int[][] punkteKoordinaten = {{64, 360}, {460, 720}, {1710, 820}, {920, 685}, {592, 600}};
    private int[] deltaGhosts;
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
        gameOver = new Texture(Gdx.files.internal("GameOver.jpg"));
        smallDot = new Texture("dotB.png");


        //button = new ImageButton(button,250,250);
        imageButtonTextureRegion = new TextureRegion(imageButtonTexture,800,350);
        //imageButtonDrawable = new TextureRegionDrawable(imageButtonTexture,250,250);

        /*Label.LabelStyle label1Style = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("bitmapfont/Amble-Regular-26.fnt"));
        label1Style.font = myFont;
        label1Style.fontColor = Color.RED;

        Label label1 = new Label("Title (BitmapFont)",label1Style);
        label1.setSize(Gdx.graphics.getWidth(),32);
        label1.setPosition(0,Gdx.graphics.getHeight()-32*2);
        label1.setAlignment(Align.center);

        imageButtonLayer.getObjects().add(new TextureMapObject(label1));*/




        TextureMapObject ib = new TextureMapObject(imageButtonTextureRegion);
        ib.setX(600);
        ib.setY(1000);
        imageButtonLayer.getObjects().add(ib);

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

        ghost[0] = new TextureRegion(geistRot,0,0,32,32);

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

        /*TextureMapObject sd = new TextureMapObject(smallDotRegion);
        sd.setX(200);
        sd.setY(480);
        smallDotLayer.getObjects().add(sd);
*/
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
       // System.out.println(">isOverlapping");
       // System.out.println(x + "/" + y);
        for (MapObject mapObject : mapObjects) {
            RectangleMapObject rectangleObject = (RectangleMapObject)mapObject;
            //System.out.println(rectangleObject.getRectangle().getX() + "/" + rectangleObject.getRectangle().getY());
            float diffX = x - rectangleObject.getRectangle().getX();
            if (diffX < 0) diffX = diffX * -1;

            float diffY = y - rectangleObject.getRectangle().getY();
            if (diffY < 0) diffY = diffY * -1;

           // System.out.println(diffX + "/" + diffY);
           // System.out.println("hallo");
            if ((diffX == 0) && (diffY == 0)) return true;
        }
        //System.out.println("<isOverlapping");
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
        font.draw(batch, text, 500, 1050);
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



