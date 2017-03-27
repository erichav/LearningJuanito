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

    // Preferencias
    Preferences sonido = app.getPreferences("Preferencias sonido");

    // Escenas
    private Stage escenaOpciones;

    //botones
    private ImageButton musicButton;
    private TextureRegionDrawable musicOn;
    private ImageButton efectButton;
    private TextureRegionDrawable efectOn;

    //musica
    private Music musica;

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
        cargarMusica();
    }

    private void cargarMusica() {
        musica = manager.get("Audio/Fondo.mp3");
        musica.setLooping(true);

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

        musicOn = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoOn));
        musicButton = new ImageButton(musicOn);
        musicButton.setPosition(ANCHO/2+180-musicButton.getWidth()/2,5*ALTO/12+110-musicButton.getHeight()/2);
        escenaOpciones.addActor(musicButton);

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(sonido.getBoolean("musicaOn")){
                    sonido.putBoolean("musicaOn", false);
                }
                else{
                    sonido.putBoolean("musicaOn", true);
                }
                sonido.flush();
                cambiarTexturasMusica();
            }
        });

        efectButton.remove();
        if(sonido.getBoolean("efectOn")){
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOn.png");
        }
        else{
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOff.png");
        }
        efectOn = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoOn));
        efectButton = new ImageButton(efectOn);
        efectButton.setPosition(ANCHO/2+180-efectButton.getWidth()/2,5*ALTO/12+110-efectButton.getHeight()/2);
        escenaOpciones.addActor(efectButton);

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(sonido.getBoolean("efectOn")){
                    sonido.putBoolean("efectOff", false);
                }
                else{
                    sonido.putBoolean("efectOn", true);
                }
                sonido.flush();
                cambiarTexturasEfectos();
            }
        });

        Gdx.input.setInputProcessor(escenaOpciones);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaOpciones = manager.get("Images/screens/opciones.jpg");
        texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
        texturaBtnJugar = manager.get("Images/btns/btnJugarPantallas.png");
        if(sonido.getBoolean("musicaOn")){
            texturaBtnSonidoOn = manager.get("Images/btns/btnSoundOn.png");
        }
        else{
            texturaBtnSonidoOn = manager.get("Images/btns/btnSoundOff.png");
        }
        if(sonido.getBoolean("efectoOn")){
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOn.png");
        }
        else{
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOff.png");
        }
    }

    private void cambiarTexturasMusica(){
        musicButton.remove();
        if(sonido.getBoolean("musicaOn")){
            texturaBtnSonidoOn = manager.get("Images/btns/btnSoundOn.png");
        }
        else{
            texturaBtnSonidoOn = manager.get("Images/btns/btnSoundOff.png");
        }
        musicOn = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoOn));
        musicButton = new ImageButton(musicOn);
        musicButton.setPosition(ANCHO/2+180-musicButton.getWidth()/2,5*ALTO/12+110-musicButton.getHeight()/2);
        escenaOpciones.addActor(musicButton);

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(sonido.getBoolean("musicaOn")){
                    sonido.putBoolean("musicaOn", false);
                }
                else{
                    sonido.putBoolean("musicaOn", true);
                }
                sonido.flush();
                cambiarTexturasMusica();
            }
        });
    }

    private void cambiarTexturasEfectos(){
        efectButton.remove();
        if(sonido.getBoolean("efectOn")){
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOn.png");
        }
        else{
            texturaBtnEfectoOn = manager.get("Images/btns/btnEfectoOff.png");
        }
        efectOn = new TextureRegionDrawable(new TextureRegion(texturaBtnSonidoOn));
        efectButton = new ImageButton(efectOn);
        efectButton.setPosition(ANCHO/2+180-efectButton.getWidth()/2,5*ALTO/12+110-efectButton.getHeight()/2);
        escenaOpciones.addActor(efectButton);

        musicButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(sonido.getBoolean("efectOn")){
                    sonido.putBoolean("efectOff", false);
                }
                else{
                    sonido.putBoolean("efectOn", true);
                }
                sonido.flush();
                cambiarTexturasEfectos();
            }
        });
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
        if(sonido.getBoolean("musicON")){
            musica.play();
        }
        else{
            musica.stop();
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
        /*manager.load("Images/screens/opciones.jpg", Texture.class);
        manager.load("Images/btns/btnSoundOff.png", Texture.class);
        manager.load("Images/btns/btnSoundOn.png", Texture.class);
        manager.load("Images/btns/btnEfectoOff.png", Texture.class);
        manager.load("Images/btns/btnEfectoOn.png", Texture.class);
        manager.load("Images/btns/btnMenuPrinc.png", Texture.class);
        manager.load("Images/btns/btnJugarPantallas.png", Texture.class);*/

    }
}
