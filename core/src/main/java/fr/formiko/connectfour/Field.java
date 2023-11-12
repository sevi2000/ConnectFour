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
    Win victory = null;
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
                System.out.println("width  = "+width+"height = "+height);
                if (!won()){
                    play(col);
                    currentPlayer = currentPlayer.switchPlayer();
                }
                if (won())
                  System.out.println("won");
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
        if (victory != null){
            switch (victory.type){

                case VERTICAL -> {

                    sd.setDefaultLineWidth(5);
                    sd.setColor(Color.GREEN);
                    for (int i = 0; i < 4; i++) {
                        sd.circle(getX() + victory.col * radius * 2 + radius, getY() + (victory.row + i) * radius * 2 + radius, radius);
                    }

                }
                case HORIZONTAL -> {

                    sd.setDefaultLineWidth(5);
                    sd.setColor(Color.GREEN);
                    for (int i = 0; i < 4; i++) {
                        sd.circle(getX() + (victory.col + i) * radius * 2 + radius, getY() + victory.row * radius * 2 + radius, radius);
                    }

                }
                case SWDIAG -> {
                    sd.setDefaultLineWidth(5);
                    sd.setColor(Color.GREEN);
                    for (int i = 0; i < 4; i++) {
                        sd.circle(getX() + (victory.col + i) * radius * 2 + radius, getY() + (victory.row + i) * radius * 2 + radius, radius);
                    }
                }
                case NWDIAG -> {
                    sd.setDefaultLineWidth(5);
                    sd.setColor(Color.GREEN);
                    for (int i = 0; i < 4; i++) {
                        sd.circle(getX() + (victory.col + i) * radius * 2 + radius, getY() + (victory.row - i) * radius * 2 + radius, radius);
                    }
                }
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

    public void update() {
        if (Gdx.input.getX() < (int)getX() + getMaxRadius(width,height))
            transparentPosX = (int)getX() + getMaxRadius(width,height);
        else if(Gdx.input.getX() > getMaxRadius(width,height)*(width+1)*2)
            transparentPosX = getMaxRadius(width,height)*2*(width+1);
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


    // Function to check for horizontal victory
    Win checkHorizontal() {
         for (int j = 0; j < height; j++) {
             for (int i = 0; i < width-3; i++) {
                 if (elements[i][j] != Player.BLANK && elements[i][j] == elements[i + 1][j] &&
                     elements[i+1][j] == elements[i + 2][j] &&
                     elements[i+2][j] == elements[i + 3][j])
                     return new Win(Win.Type.HORIZONTAL,i,j);
             }
         }

         return null;
    }

    Win checkVertical() {
        for (int j = 0; j < height-3; j++) {
            for (int i = 0; i < width; i++) {
                if (elements[i][j] != Player.BLANK && elements[i][j] == elements[i][j + 1] &&
                    elements[i][j + 1] == elements[i][j + 2] &&
                    elements[i][j + 2] == elements[i][j + 3])
                    return new Win(Win.Type.VERTICAL,i,j);
            }
        }

        return null;
    }

    Win checkDiagonals() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (f(col,row) )
                    return new Win(Win.Type.SWDIAG,col,row);
                if (f2(col,row))
                    return new Win(Win.Type.NWDIAG,col,row);

            }
        }
        return null;
    }

    boolean f(int col, int row) {
        if (col +3 >= width || row + 3 >= height)
            return false;
        return elements[col][row] != Player.BLANK &&
            elements[col][row] == elements[col + 1][row + 1] &&
            elements[col + 1][row + 1] == elements[col + 2][row + 2] &&
            elements[col +2][row + 2] == elements[col + 3][row + 3];
    }

    boolean f2(int col, int row) {
        if (col +3 >= width || row - 3 < 0)
            return false;
        return elements[col][row] != Player.BLANK &&
            elements[col][row] == elements[col + 1][row - 1] &&
            elements[col + 1][row - 1] == elements[col + 2][row - 2] &&
            elements[col +2][row - 2] == elements[col + 3][row - 3];
    }

    boolean won(){
        System.out.println(victory);
        if (checkVertical() != null)
            victory = checkVertical();
        else if (checkHorizontal() != null)
            victory = checkHorizontal();
        else if (checkDiagonals() != null)
            victory = checkDiagonals();
        return victory != null;
    }
}
