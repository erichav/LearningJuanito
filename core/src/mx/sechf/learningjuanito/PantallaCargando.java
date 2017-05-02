package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by roberto on 13/03/17.
 */

class PantallaCargando extends Pantalla
{
    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.05f;
    private Sprite spriteCargando;
    private float timerAnimacion = TIEMPO_ENTRE_FRAMES;

    // AssetManager
    private AssetManager manager;
    private LearningJuanito juego;
    private Pantallas siguientePantalla;
    private int avance; // % de carga
    private Texto texto;

    private Texture texturaCargando;

    public PantallaCargando(LearningJuanito juego, Pantallas siguientePantalla) {
        this.juego = juego;
        this.siguientePantalla = siguientePantalla;
    }

    @Override
    public void show() {
        texturaCargando = new Texture(Gdx.files.internal("Images/PantallaCargando/cargando.png"));
        spriteCargando = new Sprite(texturaCargando);
        spriteCargando.setPosition(ANCHO/2-spriteCargando.getWidth()/2,ALTO/2-spriteCargando.getHeight()/2);
        cargarRecursosSigPantalla();
        texto = new Texto();
    }

    // Carga los recursos de la siguiente pantalla
    private void cargarRecursosSigPantalla() {
        manager = juego.getAssetManager();
        avance = 0;
        switch (siguientePantalla) {
            case MENU:
                cargarRecursosMenu();
                break;
            case NIVEL_PRINCIPIANTE:
                cargarRecursosNivel();
                break;
            case NIVEL_INTERMEDIO:
                cargarRecursosNivel();
                break;
            case NIVEL_EXPERTO:
                cargarRecursosNivel();
                break;
        }
    }

    private void cargarRecursosNivel() {
        manager.load("Images/objects/Juanito/juanito.png", Texture.class);
        manager.load("Images/objects/Mama/mamaJuanito.png", Texture.class);
        manager.load("Images/PantallaJuego/vida.png", Texture.class);
        manager.load("Images/btns/btnContinuar.png", Texture.class);
        manager.load("Images/dialogos/dialo.png", Texture.class);
        manager.load("Images/dialogos/dialogoJuanito.png", Texture.class);
        manager.load("Images/dialogos/dialogoMama1.png", Texture.class);
        manager.load("Images/screens/pausa.jpg", Texture.class);
        manager.load("Images/btns/btnMenuPrinc.png", Texture.class);
        manager.load("Images/btns/btnJugarPausa.png", Texture.class);
        manager.load("Images/btns/btnOpcionesPausa.png", Texture.class);
        manager.load("Images/screens/gameOver.jpg", Texture.class);
        manager.load("Images/screens/ganaste.jpg", Texture.class);
        manager.load("Mapa/mapaNivel1.tmx", TiledMap.class);
        manager.load("Images/btns/btnPausa.png", Texture.class);
        manager.load("Audio/Slap2.mp3", Sound.class);
    }

    private void cargarRecursosMenu() {
    /*
    Carga los recursos del menu
    * */
        manager.load("Images/btns/btnRegresar.png", Texture.class);
        manager.load("Images/btns/btnSoundOn.png", Texture.class);
        manager.load("Images/btns/btnSoundOff.png", Texture.class);
        manager.load("Images/btns/btnEfectoOn.png", Texture.class);
        manager.load("Images/btns/btnEfectoOff.png", Texture.class);
        manager.load("Images/screens/opciones.jpg", Texture.class);
        manager.load("Audio/menuFondo.mp3", Music.class);
        manager.load("Audio/Fondo.mp3",Music.class);
    }

    @Override
    public void render(float delta) {
        borrarPantalla(0.5f, 0.5f, 0.5f);
        batch.setProjectionMatrix(camara.combined);
        batch.begin();
        spriteCargando.draw(batch);
        texto.mostrarMensaje(batch,avance+" %",ANCHO/2,ALTO/2);
        batch.end();
        // Actualizar
        timerAnimacion -= delta;
        if (timerAnimacion<=0) {
            timerAnimacion = TIEMPO_ENTRE_FRAMES;
            spriteCargando.rotate(45);
        }
        // Actualizar carga
        actualizarCargaRecursos();
    }

    private void actualizarCargaRecursos() {
        if (manager.update()) { // Terminó?
            switch (siguientePantalla) {
                case MENU:
                    juego.setScreen(new PantallaMenu(juego));   // 100% de carga
                    break;
                case NIVEL_PRINCIPIANTE:
                    juego.setScreen(new PantallaJuego(juego));   // 100% de carga
                    break;
                case NIVEL_INTERMEDIO:
                    juego.setScreen(new PantallaJuego(juego));   // 100% de carga
                    break;
                case NIVEL_EXPERTO:
                    juego.setScreen(new PantallaJuego(juego));   // 100% de carga
                    break;
            }
        }
        avance = (int)(manager.getProgress()*100);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        texturaCargando.dispose();
    }
}
