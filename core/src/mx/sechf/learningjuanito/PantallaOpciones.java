package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaOpciones extends Pantalla {

    private final LearningJuanito menu;
    private AssetManager manager;

    //texturas
    private Texture texturaOpciones;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;
    private Texture texturaBtnSonidoOn;
    private Texture texturaBtnEfectoOn;

    // Escenas
    private Stage escenaOpciones;

    public PantallaOpciones(LearningJuanito menu) {
        this.app = app;
        this.menu=menu;
        this.manager = menu.getAssetManager();
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaOpciones = new Stage(vista,batch);
        Image imgFondo = new Image(texturaOpciones);
        escenaOpciones.addActor(imgFondo);

        //botonRegresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO/10+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaOpciones.addActor(btnRegresar);

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
        btnJugar.setPosition(9*ANCHO/10-70-btnJugar.getWidth()/2,2*ALTO/12-btnJugar.getHeight()/2);
        escenaOpciones.addActor(btnJugar);

        //accion del boton jugar
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaDificultades(menu));
            }
        });


        //boton Musica
        TextureRegionDrawable trdBtnMusic = new TextureRegionDrawable
                (new TextureRegion(texturaBtnSonidoOn));
        ImageButton btnMusic = new ImageButton(trdBtnMusic);
        btnMusic.setPosition(ANCHO/2+220-btnMusic.getWidth()/2,ALTO/2+60-btnMusic.getHeight()/2);
        escenaOpciones.addActor(btnMusic);

        //accion del boton musica
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        //boton Efecto
        TextureRegionDrawable trdBtnEfecto = new TextureRegionDrawable
                (new TextureRegion(texturaBtnEfectoOn));
        ImageButton btnEfect = new ImageButton(trdBtnEfecto);
        btnEfect.setPosition(ANCHO/2+220-btnEfect.getWidth()/2,ALTO/2-130-btnEfect.getHeight()/2);
        escenaOpciones.addActor(btnEfect);

        //accion del boton musica
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });

        Gdx.input.setInputProcessor(escenaOpciones);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaOpciones = new Texture("Images/screens/opciones.jpg");
        texturaBtnRegresar = new Texture("Images/btns/btnMenuPrinc.png");
        texturaBtnJugar = new Texture("Images/btns/btnJugarPantallas.png");
        texturaBtnSonidoOn = new Texture("Images/btns/btnSoundOn.png");
        texturaBtnEfectoOn = new Texture("Images/btns/btnEfectoOn.png");
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
        escenaOpciones.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }
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
