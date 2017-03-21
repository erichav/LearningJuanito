package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.sql.Time;

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaJuego implements Screen {
    public static float ANCHO = 1280;
    public static float ALTO = 800;
    private final LearningJuanito menu;
    private EstadoJuego estadoJuego = EstadoJuego.INICIANDO;
    private float tiempo;

    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Juanito
    private Personaje Juanito;
    private Texture texturaJuanito;

    //Música
    private Music musicaFondo; //Sonidos largos

    //Vidas
    private int vidas = 3;
    private Array<Image> arrVidas;

    //Puntaje
    private int puntosJugador = 0;
    private Texto puntaje;

    //camara, vista
    private OrthographicCamera camara;
    private Viewport vista;

    //texturas
    private Texture texturaJuego;
    private Texture texturaBtnPausa;
    private Texture texturaMama;
    private Texture texturaVida;

    // Sprite batch
    private SpriteBatch batch;

    // Escenas
    private Stage escenaJuego;
    public PantallaJuego(LearningJuanito menu) { this.menu=menu; }

    @Override
    public void show() {
        tiempo =0;
        crearCamara();
        texturaJuanito = new Texture("juanitoSprite.png");
        Juanito = new Personaje(texturaJuanito,-50,64);
        cargarMapa();
        batch = new SpriteBatch();
        cargarTexturas();
        crearObjetos();


        Gdx.input.setInputProcessor(escenaJuego);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarMapa() {
        AssetManager manager = new AssetManager();
        manager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));
        manager.load("mapaNivel1.tmx", TiledMap.class);
        //Cargar Audios
        manager.load("Audio/Fondo.mp3",Music.class);
        manager.finishLoading();
        batch = new SpriteBatch();

            mapa = manager.get("mapaNivel1.tmx");
        musicaFondo = manager.get("Audio/Fondo.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.play();
        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camara);
    }

    private void crearObjetos() {
        escenaJuego = new Stage(vista,batch);
        Image imgFondo = new Image(texturaJuego);
        escenaJuego.addActor(imgFondo);
        puntaje = new Texto();
        //imagenMamá
        Image imgMama= new Image(texturaMama);
        imgMama.setPosition(ANCHO*12/100-imgMama.getWidth()/2,41*ALTO/120-imgMama.getHeight()/2);
        escenaJuego.addActor(imgMama);

        //imagenJuanito
        Image imgJuanito= new Image(texturaJuanito);
        imgJuanito.setPosition(ANCHO*4/10-imgJuanito.getWidth()/2,26*ALTO/120-imgJuanito.getHeight()/2);
        escenaJuego.addActor(imgJuanito);

        //imagenesVidas
        for(int x = 1;x<=vidas;x++)
        {
            Image imgVida = new Image(texturaVida);
            imgVida.setPosition(ANCHO*(100-5*x)/100-imgVida.getWidth()/2,105*ALTO/120-imgVida.getHeight()/2);
            escenaJuego.addActor(imgVida);
        }

        //botonPausa
        TextureRegionDrawable trdBtnPausa = new TextureRegionDrawable
                (new TextureRegion(texturaBtnPausa));
        ImageButton btnPausa = new ImageButton(trdBtnPausa);
        btnPausa.setPosition(ANCHO*9/10-btnPausa.getWidth()/2,15*ALTO/120-btnPausa.getHeight()/2);
        escenaJuego.addActor(btnPausa);

        //accion del boton Pausa
        btnPausa.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaPausa(menu ));
            }
        });
    }

    private void cargarTexturas() {
        texturaJuego = new Texture("Images/screens/juego.jpg");
        texturaBtnPausa = new Texture("Images/btns/btnPausa.png");
        texturaMama = new Texture("Images/objects/Mama.png");
        texturaVida = new Texture("Images/objects/cuadernito.png");
    }

    private void crearCamara() {
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }

    @Override
    public void render(float delta) {
        tiempo = tiempo + delta;
        borrarPantalla();
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);
        rendererMapa.render();
        batch.begin();
        Juanito.dibujar(batch);
        batch.end();
        if(estadoJuego == EstadoJuego.INICIANDO)
        {
            if (tiempo < 5)
            {
                return;
            }
            else
            {
                estadoJuego = EstadoJuego.CORRIENDO;
            }
        }
        else if (estadoJuego == EstadoJuego.CORRIENDO) // Actualizar a Juanito
        {
            puntosJugador = (int)((tiempo-5)*10);
            Juanito.actualizar(mapa);
            //escenaJuego.draw();
            if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
                menu.setScreen(new PantallaMenu(menu));
            }
            puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString(puntosJugador), ANCHO*85/100,118*ALTO/120);
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

    public enum EstadoJuego {
        INICIANDO,
        CORRIENDO,
        PAUSADO
    }
}
