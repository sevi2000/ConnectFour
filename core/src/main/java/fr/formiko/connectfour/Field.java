package fr.formiko.connectfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Field extends Actor {
    int width, height;
    Player[][] elements;
    private ShapeDrawer sd;
    int transparentPosX = -1;
    int transparentPosY = -1;

    Player currentPlayer = Player.BLUE;
    Field(int width, int height) {
        this.width = width;
        this.height = height;
        setSize(width * getMaxRadius(width, height), height * getMaxRadius(width, height));
        elements = new Player[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                elements[i][j] = Player.BLANK;
            }
        }
        addListener( new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                int col = (int)x/getMaxRadius(width,height)/2;
                play(col);
                currentPlayer = currentPlayer.switchPlayer();
            }
        });
    }

    private int getMaxRadius(int width, int height) {
        return Math.min(Gdx.graphics.getWidth() / width, Gdx.graphics.getHeight() / (height + 1)) / 2;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        update();
        if (sd == null)
            sd = getShapeDrawer(batch);
        sd.setColor(Color.YELLOW);
        int radius = getMaxRadius(width, height);
        setSize(width * radius * 2, height * radius * 2);
        setPosition((Gdx.graphics.getWidth() - getWidth()) / 2,
                (Gdx.graphics.getHeight() - getHeight() - (radius * 2)) / 2);
        sd.filledRectangle(getX(), getY(), getWidth(), getHeight());
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color c = Color.BLACK;
                if (elements[i][j] == Player.BLUE)
                    c = Color.BLUE;
                else if (elements[i][j] == Player.RED)
                    c = Color.RED;
                sd.filledCircle(getX() + i * radius * 2 + radius, getY() + j * radius * 2 + radius, radius, c);
            }
        }
        Color c = currentPlayer == Player.BLUE ? new Color(0,0,1,(float) 0.5):new Color(1,0,0,(float)0.5);
        sd.filledCircle(transparentPosX, transparentPosY, radius, c);
    }

    public ShapeDrawer getShapeDrawer(Batch batch) {
        if (sd == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            Texture texture = new Texture(pixmap); // remember to dispose of later
            pixmap.dispose();
            TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
            sd = new ShapeDrawer(batch, region);
        }
        return sd;
    }

    public void update() {
        if (Gdx.input.getX() < getX())
        transparentPosX = (int)getX();
        else
            transparentPosX = Gdx.input.getX();
        transparentPosY = 445;
    }

    public void play(int col){
        int i = 0;
        while (i < height-1 && elements[col][i] != Player.BLANK)
            i++;
        elements[col][i] = currentPlayer;
    }
}
