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
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaInstrucciones implements Screen {
    private static final float ANCHO = 1280;
    private static final float ALTO = 800;
    private final LearningJuanito menu;

    //camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //texturas
    private Texture texturaInstrucciones;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;

    // Sprite batch
    private SpriteBatch batch;

    // Escenas
    private Stage escenaInstrucciones;

    public PantallaInstrucciones(LearningJuanito menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaInstrucciones = new Stage(vista,batch);
        Image imgFondo = new Image(texturaInstrucciones);
        escenaInstrucciones.addActor(imgFondo);

        //botonRegresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO/10-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaInstrucciones.addActor(btnRegresar);

        //accion del boton Regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu ));
            }
        });

        //boton Jugar
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(9*ANCHO/10-btnJugar.getWidth()/2,2*ALTO/12-btnJugar.getHeight()/2);
        escenaInstrucciones.addActor(btnJugar);

        //accion del boton jugar
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaDificultades(menu));
            }
        });

        Gdx.input.setInputProcessor(escenaInstrucciones);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaInstrucciones = new Texture("Images/screens/instrucciones.jpg");
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
        escenaInstrucciones.draw();
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
