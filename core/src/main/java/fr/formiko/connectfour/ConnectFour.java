package fr.formiko.connectfour;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class ConnectFour extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    Field field;
    private Stage stage;
    Viewport viewport;
    Camera camera;

    @Override
    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        field = new Field(7,6);
        stage = new Stage(viewport,batch);
        stage.addActor(field);
        stage.act();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 1, 1, 1);
        ScreenUtils.clear(1, 1, 1, 1);
        stage.getBatch().setProjectionMatrix(viewport.getCamera().view);
        stage.getBatch().setTransformMatrix(viewport.getCamera().projection);
        stage.draw();

    }
    @Override
    public void resize(int width,int height){
        viewport.update(width,height);
        viewport.getCamera().update();

    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
