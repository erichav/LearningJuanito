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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
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
    public EstadoJuego estadoJuego = EstadoJuego.INICIANDO;
    private float tiempo;
    private float velocidad = 10;
    private float posicionMama;
    private float separacion; //La separación original entre Juanito y su mamá.
    private int posicionObstaculo=0;
    public static final float ANCHOTOTAL = ANCHO*50;

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

    private PantallaPausa panPausa;

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


        //ALCANZADO
        Texture texturaPausa = manager.get("Images/btns/btnPausa.png");
        TextureRegionDrawable trBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        ImageButton btnPausa = new ImageButton(trBtnPausa);
        btnPausa.setPosition(0, ALTO-btnPausa.getHeight());
        escenaHUD.addActor(btnPausa);
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
                    Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
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
        if(estadoJuego==EstadoJuego.PAUSADO){
            generaPantallaPausa();
        }
        else if(estadoJuego == EstadoJuego.INICIANDO) {//Aquí se marca toda la animación de inicio, desde que aparece Juanito y su mamá hasta que lo empieza a perseguir
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
            posicionMama = camara.position.x-Mama.sprite.getX();
            separacion = Juanito.sprite.getX() - Mama.sprite.getX();
            Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
            Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
            estadoJuego = EstadoJuego.CORRIENDO; //Cuando termine la animación de inicio, se cambia a CORRIENDO
        } else if (estadoJuego == EstadoJuego.CORRIENDO){ // Actualizar a Juanito{
            int posXJuanito = (int) ((Juanito.sprite.getX() + 32) / 32);
            velocidad = velocidad + 0.005f;
            puntosJugador = puntosJugador + delta;
            Juanito.actualizar(mapa);
            Mama.actualizar(mapa);
            Mama.checaSalto(mapa);
            if(Math.random()>0.5&&posicionObstaculo<posXJuanito)
            {
                posicionObstaculo = posXJuanito+40;
                generaObstaculo((int)((Math.random()*10)%4),posicionObstaculo,2);
            }
            actualizarCamara();
            colision();
            batch.begin();
            puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString((int)(puntosJugador*10)), ANCHO*85/100,118*ALTO/120);
            batch.end();
        } else if (estadoJuego == EstadoJuego.ALCANZADO) {
            escenaAlcanzado.draw();
            batch.begin();
            chanclazo.mostrarMensaje(batch, "            CHANCLAZO\nHas perdido una vida", ANCHO/2,ALTO*2/3);
            batch.end();
        }

    }

    private void generaPantallaPausa() {
        if(this.panPausa == null) {
            this.panPausa = new PantallaPausa(menu, this);
        }
        menu.setScreen(panPausa);
        //Gdx.input.setInputProcessor(panPausa);
    }

    private void colision() {
        if(Juanito.Colisiona(Mama))
        {
            estadoJuego = EstadoJuego.ALCANZADO;
            Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
            Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
            Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
            eliminarObjetos();
            reacomodarPersonajes();
            if (escenaAlcanzado==null) {
                    escenaAlcanzado = new EscenaAlcanzado(vistaHUD, batch);
                actualizarCamara();
            }
                Gdx.input.setInputProcessor(escenaAlcanzado);
        }
    }
    private void generaObstaculo(int tipo, int posX, int posY) // Generará un obstáculo de tipo "tipo" en la posición posX
    {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer obstaculos = (TiledMapTileLayer) mapa.getLayers().get(0);
        switch (tipo){
            case 0: // MESA
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(0,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(0,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(0,26));
                capa.setCell(posX,posY+3, obstaculos.getCell(0,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(1,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(1,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(1,26));
                capa.setCell(posX+1,posY+3, obstaculos.getCell(1,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(2,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(2,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(2,26));
                capa.setCell(posX+2,posY+3, obstaculos.getCell(2,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(3,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(3,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(3,26));
                capa.setCell(posX+3,posY+3, obstaculos.getCell(3,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(4,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(4,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(4,26));
                capa.setCell(posX+4,posY+3, obstaculos.getCell(4,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(5,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(5,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(5,26));
                capa.setCell(posX+5,posY+3, obstaculos.getCell(5,27));
                break;
            case 1: //SILLON 1
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(6,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(6,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(6,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(7,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(7,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(7,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(8,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(8,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(8,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(9,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(9,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(9,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(10,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(10,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(10,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(11,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(11,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(11,27));
                break;
            case 2: // SILLON 2
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(12,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(12,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(12,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(13,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(13,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(13,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(14,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(14,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(14,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(15,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(15,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(15,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(16,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(16,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(16,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(17,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(17,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(17,27));
                break;
            case 3: // SILLA
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(18,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(18,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(18,27));
                capa.setCell(posX,posY+3, obstaculos.getCell(18,28));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(19,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(19,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(19,27));
                capa.setCell(posX+1,posY+3, obstaculos.getCell(19,28));
                break;
        }
    }

    private void eliminarObjetos() {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        for (int cordX = 0; cordX <= 2000; cordX++) {
            for (int cordY = 0; cordY <= 25; cordY++) {
                capa.setCell(cordX,cordY, null);
            }
        }
    }

    private void reacomodarPersonajes() {
        //Pone los personajes en su posición original con respecto a la cámara
        Mama.sprite.setY(64);
        Juanito.sprite.setY(64);
        Mama.sprite.setX(camara.position.x-posicionMama);
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
        ALCANZADO,
        TERMINADO
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
            Vector3 v = new Vector3();
            v.set(screenX, screenY, 0);
            Gdx.app.log("x: " + v.x, "y: " + v.y);
            if(v.x<65 && v.y < 53){
                estadoJuego = EstadoJuego.PAUSADO;
            }else if(estadoJuego == EstadoJuego.CORRIENDO) {
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