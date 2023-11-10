package fr.formiko.connectfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Field extends Actor {
    int width,height;
    Player[][] elements;
    private ShapeDrawer sd;

    Field(int width, int height){
        this.width = width;
        this.height = height;
        setSize(width * getMaxRadius(width,height), height* getMaxRadius(width,height));
        elements = new Player[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                elements[i][j] = Player.BLANK;
            }
        }
    }

    private int getMaxRadius(int width, int height) {
        return Math.min(Gdx.graphics.getWidth() / width, Gdx.graphics.getHeight() / height)/2;
    }

    @Override
    public void draw(Batch batch,float alpha){
        System.out.printf("widh : %f height : %f\n",getWidth(),getHeight());
        if (sd == null)
            sd = getShapeDrawer(batch);
        sd.setColor(Color.YELLOW);
        int radius = getMaxRadius(width,height);
        System.out.printf("radius : %d\n",radius);
        setSize(width * radius*2, height * radius * 2);
        setPosition((Gdx.graphics.getWidth()-getWidth())/2,(Gdx.graphics.getHeight()-getHeight())/2);
        sd.filledRectangle(getX(),getY(),getWidth(),getHeight());
        for (int i = 0; i < width;i++) {
            System.out.println(i);
            for (int j = 0; j < height; j++) {
                Color c = Color.BLACK;
                if (elements[i][j] == Player.BLUE)
                    c = Color.BLUE;
                else if (elements[i][j] == Player.RED)
                    c = Color.RED;
                sd.filledCircle(getX()+ i *radius*2+radius,getY()+j*radius*2+radius,radius,c);
            }
        }
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
}
