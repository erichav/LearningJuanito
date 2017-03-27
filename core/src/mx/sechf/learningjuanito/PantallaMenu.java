package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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
 * Created by Erick Chávez on 03/02/2017.
 */
public class PantallaMenu extends Pantalla {

    private final LearningJuanito menu;

    //texturas
    private Texture texturaFondo;

    // texturas botones
    private Texture texturaBtnPlay;
    private Texture texturaBtnAcercaDe;
    private Texture texturaBtnOpciones;
    private Texture texturaBtnInstrucciones;
    private Texture texturaBtnSalir;
    private Texture texturaBtnPuntuaciones;

    // Escenas
    private Stage escenaMenu;

    // AssetManager
    private AssetManager manager;

    // Música
    private Music musicaFondo;

    public PantallaMenu(LearningJuanito menu) {
        this.menu=menu;
        manager = menu.getAssetManager();
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
        cargarMusica();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaMenu = new Stage(vista,batch);
        Image imgFondo = new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);

        //botonJugar
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnPlay));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,5*ALTO/12+5-btnPlay.getHeight()/2);
        escenaMenu.addActor(btnPlay);

        //accion del boton jugar
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaDificultades(menu ));
                    musicaFondo.stop();
            }
        });

        //boton AcercaDe
        TextureRegionDrawable trdBtnAcercaDe = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDe));
        ImageButton btnAcercaDe = new ImageButton(trdBtnAcercaDe);
        btnAcercaDe.setPosition(12*ANCHO/13-20-btnAcercaDe.getWidth()/2,15*ALTO/23-20-btnAcercaDe.getHeight()/2);
        escenaMenu.addActor(btnAcercaDe);

        //accion del boton acercade
        btnAcercaDe.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaAcercaDe(menu ));
                musicaFondo.stop();
            }
        });

        //boton Opciones
        TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
        ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
        btnOpciones.setPosition(7*ANCHO/8-45-btnOpciones.getWidth()/2,ALTO/19+90-btnOpciones.getHeight()/2);
        escenaMenu.addActor(btnOpciones);

        //accion del boton Opciones
        btnOpciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaOpciones(menu));
                musicaFondo.stop();
            }
        });

        //boton Intrucciones
        TextureRegionDrawable trdBtnInstrucciones = new TextureRegionDrawable(new TextureRegion(texturaBtnInstrucciones));
        ImageButton btnInstrucciones = new ImageButton(trdBtnInstrucciones);
        btnInstrucciones.setPosition(ANCHO/2-btnInstrucciones.getWidth()/2,ALTO/2-270-btnInstrucciones.getHeight()/2);
        escenaMenu.addActor(btnInstrucciones);

        //accion del boton Instrucciones
        btnInstrucciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaInstrucciones(menu));
                musicaFondo.stop();
            }
        });

        //boton Puntaciones
        TextureRegionDrawable trdBtnPuntaciones = new TextureRegionDrawable(new TextureRegion(texturaBtnPuntuaciones));
        ImageButton btnPuntuaciones = new ImageButton(trdBtnPuntaciones);
        btnPuntuaciones.setPosition(ANCHO/13-btnPuntuaciones.getWidth()/2,15*ALTO/23-40-btnPuntuaciones.getHeight()/2);
        escenaMenu.addActor(btnPuntuaciones);

        //accion del boton puntuaciones
        btnPuntuaciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaPuntuaciones(menu));
                musicaFondo.stop();
            }
        });

        //boton Salir
        TextureRegionDrawable trdBtnSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
        ImageButton btnSalir = new ImageButton(trdBtnSalir);
        btnSalir.setPosition(ANCHO/8+45-btnSalir.getWidth()/2,ALTO/19+90-btnSalir.getHeight()/2);
        escenaMenu.addActor(btnSalir);

        //accion del boton Salir
        btnSalir.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        Gdx.input.setInputProcessor(escenaMenu);
        Gdx.input.setCatchBackKey(false);
    }

    private void cargarTexturas() {
        //pantallas
        texturaFondo = new Texture("Images/screens/principal.jpg");

        //btns
        texturaBtnPlay = new Texture("Images/btns/btnPlay.png");
        texturaBtnAcercaDe = new Texture("Images/btns/btnAcercaDe.png");
        texturaBtnOpciones = new Texture("Images/btns/btnOpciones.png");
        texturaBtnInstrucciones = new Texture("Images/btns/btnInstrucciones.png");
        texturaBtnPuntuaciones = new Texture("Images/btns/btnPuntuaciones.png");
        texturaBtnSalir = new Texture("Images/btns/btnSalir.png");

    }

    public void cargarMusica(){
        musicaFondo = manager.get("Audio/menuFondo.mp3");
        musicaFondo.setVolume(0.75f);
        musicaFondo.setLooping(true);
        musicaFondo.play();
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
        escenaMenu.draw();
        // Teclado

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
