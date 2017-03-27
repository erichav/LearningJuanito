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
import com.badlogic.gdx.graphics.Pixmap;
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
    private float velocidad = 10;
    private float separacion; //La separación original entre Juanito y su mamá.
    public static final float ANCHOTOTAL = ANCHO*2;

    //Alcanzado
    private EscenaAlcanzado escenaAlcanzado;
    Texto chanclazo = new Texto();

    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Juanito
    private Personaje Juanito;
    private Texture texturaJuanito;

    //Mama
    private Personaje Mama;
    private Texture texturaMama;

    //Música
    private Music musicaFondo; //Sonidos largos

    //Vidas
    private int vidas = 3;
    private Texture texturaVidas;
    private Array<Image> arrVidas;

    //Puntaje
    private float puntosJugador = 0;
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

        //Vidas
        for(int x = 1;x<=vidas;x++)
        {
            Image imgVida = new Image(texturaVidas);
            imgVida.setPosition(ANCHO*(100-5*x)/100-imgVida.getWidth()/2,105*ALTO/120-imgVida.getHeight()/2);
            escenaHUD.addActor(imgVida);
        }


        /*// ALCANZADO
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
    private class EscenaAlcanzado extends Stage
    {
        public EscenaAlcanzado(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            // Crear rectángulo transparente
            Pixmap pixmap = new Pixmap((int)(ANCHO*0.6f), (int)(ALTO*0.7f), Pixmap.Format.RGBA8888 );
            pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.65f );
            pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
            Texture texturaRectangulo = new Texture( pixmap );
            pixmap.dispose();
            Image imgRectangulo = new Image(texturaRectangulo);
            imgRectangulo.setPosition((ANCHO-pixmap.getWidth())/2, (ALTO-pixmap.getHeight())/2);
            this.addActor(imgRectangulo);

            // Crea el texto
            this.addActor(chanclazo);

            // Continuar
            Texture texturabtnReintentar = manager.get("Images/btns/btnContinuar.png");
            TextureRegionDrawable trdReintentar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnReintentar));
            ImageButton btnReintentar = new ImageButton(trdReintentar);
            btnReintentar.setPosition(ANCHO/2-btnReintentar.getWidth()/2, ALTO/4);
            btnReintentar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estadoJuego = EstadoJuego.CORRIENDO;
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(procesadorEntrada);
                }
            });
            this.addActor(btnReintentar);
        }
    }

    private void cargarPersonajes() {
        Juanito = new Personaje(texturaJuanito,90,180,-200,64);
        Mama = new Personaje(texturaMama,120,240,-200,64);
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


    private void cargarTexturas() {
        texturaJuanito = manager.get("Images/objects/Juanito/juanito.png");
        texturaMama = manager.get("Images/objects/Mama/mamaJuanito.png");
        texturaVidas = manager.get("Images/PantallaJuego/vida.png");
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
        Mama.dibujar(batch);
        batch.end();
        // HUD
        batch.setProjectionMatrix(camaraHUD.combined);
        escenaHUD.draw();
        if(estadoJuego == EstadoJuego.INICIANDO)
        {//Aquí se marca toda la animación de inicio, desde que aparece Juanito y su mamá hasta que lo empieza a perseguir
            if (tiempo < 2)
            {
                return;
            }
            else if (tiempo < 3.2)
            {
                Juanito.actualizar(mapa);
                return;
            }
            else if (tiempo < 3.55)
            {
                Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                Mama.actualizar(mapa);
                return;
            }
            separacion = Juanito.sprite.getX() - Mama.sprite.getX();
            Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
            Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
            estadoJuego = EstadoJuego.CORRIENDO; //Cuando termine la animación de inicio, se cambia a CORRIENDO
        }
        else if (estadoJuego == EstadoJuego.CORRIENDO) // Actualizar a Juanito
        {
            velocidad = velocidad + 0.005f;
            puntosJugador = puntosJugador + delta;
            Juanito.actualizar(mapa);
            Mama.actualizar(mapa);
            actualizarCamara();
            colision();
            batch.begin();
            puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString((int)(puntosJugador*10)), ANCHO*85/100,118*ALTO/120);
            batch.end();
        }
        else if (estadoJuego == EstadoJuego.ALCANZADO)
        {
            escenaAlcanzado.draw();
            batch.begin();
            chanclazo.mostrarMensaje(batch, "            CHANCLAZO\nHas perdido una vida", ANCHO/2,ALTO*2/3);
            batch.end();
        }

    }

    private void colision() {
        if(Juanito.Colisiona(Mama))
        {
            estadoJuego = EstadoJuego.ALCANZADO;
            Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            reacomodarPersonajes();
            if (escenaAlcanzado==null) {
                    escenaAlcanzado = new EscenaAlcanzado(vistaHUD, batch);
                actualizarCamara();
            }
                Gdx.input.setInputProcessor(escenaAlcanzado);
        }
    }

    private void reacomodarPersonajes() {
        //Pone los personajes en su posición original con respecto a la cámara
        Juanito.sprite.setX(Mama.sprite.getX()+separacion);
    }


    private void actualizarCamara() {
        if(estadoJuego == EstadoJuego.CORRIENDO)
        {
            float nuevaX = camara.position.x+velocidad;
            if (nuevaX <= (ANCHOTOTAL-(ANCHO*0.5)))
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
        manager.unload("Images/PantallaJuego/vida.png");
        manager.unload("Images/btns/btnContinuar.png");
        manager.unload("mapaNivel1.tmx");
        manager.unload("Audio/Fondo.mp3");
    }

    public enum EstadoJuego {
        INICIANDO,
        CORRIENDO,
        PAUSADO,
        ALCANZADO
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
            if(estadoJuego == EstadoJuego.CORRIENDO)
            {
                Juanito.saltar();
            }
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
