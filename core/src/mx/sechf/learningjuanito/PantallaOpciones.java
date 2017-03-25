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
public class PantallaOpciones extends Pantalla {

    private final LearningJuanito menu;

    //texturas
    private Texture texturaOpciones;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;
    private Texture texturaBtnSonidoOn;
    private Texture texturaBtnSonidoOff;

    // Escenas
    private Stage escenaOpciones;

    public PantallaOpciones(LearningJuanito menu) {
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

        //boton Musica on
        TextureRegionDrawable trdBtnMusicOn = new TextureRegionDrawable
                (new TextureRegion(texturaBtnSonidoOn));
        ImageButton btnMusicOn = new ImageButton(trdBtnMusicOn);
        btnMusicOn.setPosition(ANCHO/2+180-btnMusicOn.getWidth()/2,5*ALTO/12+110-btnMusicOn.getHeight()/2);
        escenaOpciones.addActor(btnMusicOn);

        //accion del boton Musica On
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });


        //boton Efectos on
        TextureRegionDrawable trdBtnEfectOn = new TextureRegionDrawable
                (new TextureRegion(texturaBtnSonidoOn));
        ImageButton btnEfectoOn = new ImageButton(trdBtnEfectOn);
        btnEfectoOn.setPosition(ANCHO/2+180-btnEfectoOn.getWidth()/2,4*ALTO/12-btnEfectoOn.getHeight()/2);
        escenaOpciones.addActor(btnEfectoOn);

        //accion del boton Musica On
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
        texturaBtnSonidoOff = new Texture("Images/btns/btnSoundOff.png");
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
