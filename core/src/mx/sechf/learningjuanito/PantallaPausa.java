package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Csar on 16/02/2017.
 */

public class PantallaPausa implements Screen {
    private static final float ANCHO = 1280;
    private static final float ALTO = 800;
    private final LearningJuanito menu;

    // Camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    // Texturas
    private Texture texturaPausa;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;

    // Sprite batch
    private SpriteBatch batch;

    // Escenas
    private Stage escenaPausa;

    public PantallaPausa(LearningJuanito menu) {
        this.menu = menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaPausa = new Stage(vista,batch);
        Image imgFondo = new Image(texturaPausa);
        escenaPausa.addActor(imgFondo);

        // Botón Regresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO*11/100-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaPausa.addActor(btnRegresar);

        // Acción del botón Regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu));
            }
        });

        // Botón Jugar
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(85*ANCHO/100-btnJugar.getWidth()/2,2*ALTO/12-btnJugar.getHeight()/2);
        escenaPausa.addActor(btnJugar);

        // Acción del botón jugar
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaJuego(menu));
            }
        });

        Gdx.input.setInputProcessor(escenaPausa);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaPausa = new Texture("Images/screens/pausa.jpg");
        texturaBtnRegresar = new Texture("Images/btns/btnMenuPrinc.png");
        texturaBtnJugar = new Texture("Images/btns/btnJugarPantallas.png");
    }

    private void crearCamara() {
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        escenaPausa.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }

    }

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
