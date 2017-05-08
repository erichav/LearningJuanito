package mx.itesm.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by roberto on 13/03/17.
 */

class PantallaCargando extends Pantalla
{
    // Animación cargando
    private static final float TIEMPO_ENTRE_FRAMES = 0.15f;
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
        texto.setColor(Color.BLACK);
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
                cargarRecursosNivel1();
                break;
            case NIVEL_INTERMEDIO:
                cargarRecursosNivel2();
                break;
            case NIVEL_EXPERTO:
                cargarRecursosNivel3();
                break;
        }
    }

    private void cargarRecursosNivel1() {
        manager.load("Images/personajes/Juanito/Juanito0.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito1.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito2.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama0.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama1.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama2.png", Texture.class);
        manager.load("Images/PantallaJuego/vida.png", Texture.class);
        manager.load("Images/btns/btnContinuar.png", Texture.class);
        manager.load("Images/dialogos/dialo.png", Texture.class);
        manager.load("Images/dialogos/dialogoJuanito.png", Texture.class);
        manager.load("Images/dialogos/dialogoMama1.png", Texture.class);
        manager.load("Images/dialogos/finalPierde.png", Texture.class);
        manager.load("Images/dialogos/finalGanaJuanito.png", Texture.class);
        manager.load("Images/dialogos/finalGanaMama.png", Texture.class);
        manager.load("Images/screens/pausa.jpg", Texture.class);
        manager.load("Images/screens/chanclazo.jpg", Texture.class);
        manager.load("Images/btns/btnMenuPrinc.png", Texture.class);
        manager.load("Images/btns/btnOpcionesPausa.png", Texture.class);
        manager.load("Images/screens/gameOver.jpg", Texture.class);
        manager.load("Images/screens/ganaste.jpg", Texture.class);
        manager.load("Mapa/mapaNivel1.tmx", TiledMap.class);
        manager.load("Images/btns/btnPausa.png", Texture.class);
        manager.load("Images/PantallaJuego/mas100.png", Texture.class);
        manager.load("Images/PantallaJuego/menos50.png", Texture.class);
        manager.load("Audio/Slap.mp3", Sound.class);
        manager.load("Audio/Correcto.wav", Sound.class);
        manager.load("Audio/Incorrecto.mp3", Sound.class);
    }
    private void cargarRecursosNivel2() {
        manager.load("Images/personajes/Juanito/Juanito0.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito1.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito2.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama0.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama1.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama2.png", Texture.class);
        manager.load("Images/PantallaJuego/vida.png", Texture.class);
        manager.load("Images/btns/btnContinuar.png", Texture.class);
        manager.load("Images/dialogos/dialo.png", Texture.class);
        manager.load("Images/dialogos/dialogoJuanito.png", Texture.class);
        manager.load("Images/dialogos/dialogoMama1.png", Texture.class);
        manager.load("Images/dialogos/finalPierde.png", Texture.class);
        manager.load("Images/dialogos/finalGanaJuanito.png", Texture.class);
        manager.load("Images/dialogos/finalGanaMama.png", Texture.class);
        manager.load("Images/screens/pausa.jpg", Texture.class);
        manager.load("Images/btns/btnMenuPrinc.png", Texture.class);
        manager.load("Images/btns/btnOpcionesPausa.png", Texture.class);
        manager.load("Images/screens/gameOver.jpg", Texture.class);
        manager.load("Images/screens/ganaste.jpg", Texture.class);
        manager.load("Mapa/mapaNivel2.tmx", TiledMap.class);
        manager.load("Images/btns/btnPausa.png", Texture.class);
        manager.load("Images/PantallaJuego/mas200.png", Texture.class);
        manager.load("Images/PantallaJuego/menos100.png", Texture.class);
        manager.load("Audio/Slap.mp3", Sound.class);
        manager.load("Audio/Correcto.wav", Sound.class);
        manager.load("Audio/Incorrecto.mp3", Sound.class);
    }

    private void cargarRecursosNivel3(){
        manager.load("Images/personajes/Juanito/Juanito0.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito1.png", Texture.class);
        manager.load("Images/personajes/Juanito/Juanito2.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama0.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama1.png", Texture.class);
        manager.load("Images/personajes/Mama/Mama2.png", Texture.class);
        manager.load("Images/PantallaJuego/vida.png", Texture.class);
        manager.load("Images/btns/btnContinuar.png", Texture.class);
        manager.load("Images/dialogos/dialo.png", Texture.class);
        manager.load("Images/dialogos/dialogoJuanito.png", Texture.class);
        manager.load("Images/dialogos/dialogoMama1.png", Texture.class);
        manager.load("Images/dialogos/finalPierde.png", Texture.class);
        manager.load("Images/dialogos/finalGanaJuanito.png", Texture.class);
        manager.load("Images/dialogos/finalGanaMama.png", Texture.class);
        manager.load("Images/screens/pausa.jpg", Texture.class);
        manager.load("Images/btns/btnMenuPrinc.png", Texture.class);
        manager.load("Images/btns/btnOpcionesPausa.png", Texture.class);
        manager.load("Images/screens/gameOver.jpg", Texture.class);
        manager.load("Images/screens/ganaste.jpg", Texture.class);
        manager.load("Mapa/mapaNivel3.tmx", TiledMap.class);
        manager.load("Images/btns/btnPausa.png", Texture.class);
        manager.load("Images/PantallaJuego/mas300.png", Texture.class);
        manager.load("Images/PantallaJuego/menos150.png", Texture.class);
        manager.load("Audio/Slap.mp3", Sound.class);
        manager.load("Audio/Correcto.wav", Sound.class);
        manager.load("Audio/Incorrecto.mp3", Sound.class);
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
        borrarPantalla(1.0f, 1.0f, 1.0f);
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
                    juego.setScreen(new PantallaJuegoNivel2(juego));   // 100% de carga
                    break;
                case NIVEL_EXPERTO:
                    juego.setScreen(new PantallaJuegoNivel3(juego));   // 100% de carga
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
