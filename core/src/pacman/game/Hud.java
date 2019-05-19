package pacman.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {

    public Stage stage;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    //private Hud hud;
    //public PlayScreen (TiledTest2 game){
       // this.game = game;
        //hud = new Hud(game.batch);

    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label marioLabel;

    public Hud(Viewport sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        stage = new Stage(sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer),new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score),new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        timeLabel = new Label("TIME",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        levelLabel = new Label("1-1",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        worldLabel = new Label("World",new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        marioLabel = new Label("Mario",new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expand();

        stage.addActor(table);
    }

}
