package pacman.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
//import helpers.Values;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

    public class TestPlayScreen{
        public enum  pacman{

            Walking_Forward,
            Walking_Left,
            Walking_Right,
            Dead
        }
        public TestPlayScreen state;
        private Rectangle body;
        private float x,y;
       // public TestPlayScreen() {
            //x = Values.SCREEN_WIDTH/5;
            //y = Values.SCREEN_HEIGHT/5;
            //body = new Rectangle(x, y, Values.Hero_Width, Values.Hero_Height);
            //state = TestPlayScreen.Walking_Forward;
       // }
        public Rectangle getBody() {
            return body;
        }
        public boolean isCollided(Rectangle rect) {
            Gdx.app.log("Collision Detected", ""+body.overlaps(rect));
            return rect.overlaps(body);
        }
        public TestPlayScreen getState() {
            return state;
        }
        public float getX() {
            return x;
        }
        public float getY() {
            return y;
        }
        public void setX(float x) {
            this.x = x;
        }
        public void setY(float y) {
            this.y = y;
        }
    }



