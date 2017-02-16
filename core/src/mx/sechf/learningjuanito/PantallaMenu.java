package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
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
 * Created by Erick Chávez on 03/02/2017.
 */
public class PantallaMenu implements Screen{
    private static final float ANCHO = 1280;
    private static final float ALTO = 800;
    private final LearningJuanito menu;

    //camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //texturas
    private Texture texturaFondo;

    // texturas botones
    private Texture texturaBtnPlay;
    private Texture texturaBtnAcercaDe;
    private Texture texturaBtnOpciones;
    private Texture texturaBtnInstrucciones;
    private Texture texturaBtnSalir;
    private Texture texturaBtnPuntuaciones;

    // Sprite batch
    private SpriteBatch batch;

    // Escenas
    private Stage escenaMenu;


    public PantallaMenu(LearningJuanito menu) {
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
        escenaMenu = new Stage(vista,batch);
        Image imgFondo = new Image(texturaFondo);
        escenaMenu.addActor(imgFondo);

        //botonJugar
        TextureRegionDrawable trdBtnPlay = new TextureRegionDrawable(new TextureRegion(texturaBtnPlay));
        ImageButton btnPlay = new ImageButton(trdBtnPlay);
        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,5*ALTO/12-btnPlay.getHeight()/2);
        escenaMenu.addActor(btnPlay);

        //accion del boton jugar
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaJuego(menu ));
            }
        });

        //boton AcercaDe
        TextureRegionDrawable trdBtnAcercaDe = new TextureRegionDrawable(new TextureRegion(texturaBtnAcercaDe));
        ImageButton btnAcercaDe = new ImageButton(trdBtnAcercaDe);
        btnAcercaDe.setPosition(7*ANCHO/10-btnAcercaDe.getWidth()/2,7*ALTO/18-btnAcercaDe.getHeight()/2);
        escenaMenu.addActor(btnAcercaDe);

        //accion del boton acercade
        btnAcercaDe.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaAcercaDe(menu ));
            }
        });

        //boton Opciones
        TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
        ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
        btnOpciones.setPosition(7*ANCHO/8-btnOpciones.getWidth()/2,2*ALTO/15-btnOpciones.getHeight()/2);
        escenaMenu.addActor(btnOpciones);

        //accion del boton Opciones
        btnOpciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaOpciones(menu));
            }
        });

        //boton Intrucciones
        TextureRegionDrawable trdBtnInstrucciones = new TextureRegionDrawable(new TextureRegion(texturaBtnInstrucciones));
        ImageButton btnInstrucciones = new ImageButton(trdBtnInstrucciones);
        btnInstrucciones.setPosition(7*ANCHO/16,4*ALTO/19-btnInstrucciones.getHeight()/2);
        escenaMenu.addActor(btnInstrucciones);

        //accion del boton Instrucciones
        btnInstrucciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaInstrucciones(menu));
            }
        });

        //boton Puntaciones
        TextureRegionDrawable trdBtnPuntaciones = new TextureRegionDrawable(new TextureRegion(texturaBtnPuntuaciones));
        ImageButton btnPuntuaciones = new ImageButton(trdBtnPuntaciones);
        btnPuntuaciones.setPosition(1*ANCHO/13-btnPuntuaciones.getWidth()/2,15*ALTO/23-btnPuntuaciones.getHeight()/2);
        escenaMenu.addActor(btnPuntuaciones);

        //accion del boton puntuaciones
        btnPuntuaciones.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaPuntuaciones(menu));
            }
        });

        //boton Salir
        TextureRegionDrawable trdBtnSalir = new TextureRegionDrawable(new TextureRegion(texturaBtnSalir));
        ImageButton btnSalir = new ImageButton(trdBtnSalir);
        btnSalir.setPosition(1*ANCHO/8-btnSalir.getWidth()/2,3*ALTO/19-btnSalir.getHeight()/2);
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

    private void borrarPantalla() {
        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); //llena la pantalla del color elegido
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
