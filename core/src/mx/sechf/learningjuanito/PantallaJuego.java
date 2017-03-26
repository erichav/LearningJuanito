package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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
public class PantallaJuego extends Pantalla {

    private final LearningJuanito menu;
    private EstadoJuego estadoJuego = EstadoJuego.INICIANDO;
    private float tiempo;
    private final float velocidad = 10;

    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Juanito
    private Personaje Juanito;
    private Texture texturaJuanito;
    //Juanito
    private Personaje Mama;
    private Texture texturaMama;

    //Música
    private Music musicaFondo; //Sonidos largos

    //Vidas
    private int vidas = 3;
    private Array<Image> arrVidas;

    //Puntaje
    private int puntosJugador = 0;
    private Texto puntaje;

    // AssetManager
    private AssetManager manager;

    // Procesador de eventos
    private final Procesador procesadorEntrada = new Procesador();

    // HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;
    // El HUD lo manejamos con una escena (opcional)
    private Stage escenaHUD;

    public PantallaJuego(LearningJuanito menu) { this.menu=menu; manager = menu.getAssetManager();}

    @Override
    public void show() {
        tiempo =0;
        crearCamara();
        cargarTexturas();
        cargarPersonajes();
        cargarMapa();
        cargarHUD();
        batch = new SpriteBatch();
        crearObjetos();

        Gdx.input.setInputProcessor(procesadorEntrada);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarHUD() {
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        // HUD
        escenaHUD = new Stage(vistaHUD);

        //Puntaje
        puntaje = new Texto();
        escenaHUD.addActor(puntaje);

        /*// Pausa
        Texture texturaPausa = manager.get("comun/btnPausa.png");
        TextureRegionDrawable trBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        ImageButton btnPausa = new ImageButton(trBtnPausa);
        btnPausa.setPosition(ANCHO-btnPausa.getWidth(), ALTO-btnPausa.getHeight());
        btnPausa.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Se pausa el juego
                estado = estado==EstadoJuego.PAUSADO?EstadoJuego.JUGANDO:EstadoJuego.PAUSADO;
                if (estado==EstadoJuego.PAUSADO) {
                    // Activar escenaPausa y pasarle el control
                    if (escenaPausa==null) {
                        escenaPausa = new EscenaPausa(vistaHUD, batch);
                    }
                    Gdx.input.setInputProcessor(escenaPausa);
                }
                return true;
            }
        });
        escenaHUD.addActor(btnPausa);*/
    }

    private void cargarPersonajes() {
        Juanito = new Personaje(texturaJuanito,90,180,-200,64);
        Mama = new Personaje(texturaMama,120,240,-200,64);
        Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
    }

    private void cargarMapa() {
        batch = new SpriteBatch();
        mapa = manager.get("mapaNivel1.tmx");
        musicaFondo = manager.get("Audio/Fondo.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.play();
        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camara);
    }

    private void crearObjetos() {
        /*//imagenesVidas
        for(int x = 1;x<=vidas;x++)
        {
            Image imgVida = new Image(texturaVida);
            imgVida.setPosition(ANCHO*(100-5*x)/100-imgVida.getWidth()/2,105*ALTO/120-imgVida.getHeight()/2);
            escenaJuego.addActor(imgVida);
        }*/

        /*//botonPausa
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
        });*/
    }

    private void cargarTexturas() {
        texturaJuanito = manager.get("Images/objects/Juanito/juanito.png");
        texturaMama = manager.get("Images/objects/Mama/mamaJuanito.png");
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
        actualizarCamara();
        batch.setProjectionMatrix(camara.combined);
        rendererMapa.setView(camara);
        rendererMapa.render();
        batch.begin();
        Juanito.dibujar(batch);
        Mama.dibujar(batch);
        batch.end();
        // HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
        if(estadoJuego == EstadoJuego.INICIANDO)
        {
            if (tiempo < 2)//Aquí se marca toda la animación de inicio, desde que aparece Juanito y su mamá hasta que lo empieza a perseguir
            {
                return;
            }
            else if (tiempo < 3)
            {
                Juanito.actualizar(mapa);
                return;
            }
            else if (tiempo < 3.5)
            {
                Mama.actualizar(mapa);
                return;
            }
            Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
            estadoJuego = EstadoJuego.CORRIENDO; //Cuando termine la animación de inicio, se cambia a CORRIENDO
        }
        else if (estadoJuego == EstadoJuego.CORRIENDO) // Actualizar a Juanito
        {
            puntosJugador = (int)((tiempo-6.5)*10);
            Juanito.actualizar(mapa);
            Mama.actualizar(mapa);
            batch.begin();
            puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString(puntosJugador), ANCHO*85/100,118*ALTO/120);
            batch.end();
        }
    }


    private void actualizarCamara() {
        /*float posX = Juanito.sprite.getX();
        // Si está en la parte 'media'
        if (posX>=ANCHO/2 && posX<=ANCHO*10-ANCHO/2) {
            // El personaje define el centro de la cámara
            camara.position.set((int)posX, camara.position.y, 0);
        } else if (posX>ANCHO*10-ANCHO/2) {    // Si está en la última mitad
            // La cámara se queda a media pantalla antes del fin del mundo  :)
            camara.position.set(ANCHO*10-ANCHO/2, camara.position.y, 0);
        } else if ( posX<ANCHO/2 ) { // La primera mitad
            camara.position.set(ANCHO/2, ALTO/2,0);
        }*/
        if(estadoJuego == EstadoJuego.CORRIENDO)
        {
            int nuevaX = (int)(camara.position.x+velocidad);
            if (nuevaX <= ANCHO*9.5)
            {
                camara.position.set(nuevaX,camara.position.y,0);
            }
        }
        camara.update();
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
        manager.unload("Images/objects/Juanito/juanito.png");
        manager.unload("Images/objects/Mama/mamaJuanito.png");
        manager.unload("mapaNivel1.tmx");
        manager.unload("Audio/Fondo.mp3");
    }

    public enum EstadoJuego {
        INICIANDO,
        CORRIENDO,
        PAUSADO
    }

    private class Procesador implements InputProcessor{
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                musicaFondo.stop();
                menu.setScreen(new PantallaMenu(menu));
            }
            return true;
        }

        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            Juanito.saltar();
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
