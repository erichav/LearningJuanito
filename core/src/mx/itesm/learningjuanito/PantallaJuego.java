package mx.itesm.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaJuego extends Pantalla {

    private final LearningJuanito menu;
    public EstadoJuego estadoJuego = EstadoJuego.INICIANDO;
    public Minijuego minijuego = Minijuego.INSTRUCCIONES;
    private String instruccionMinijuego = "NO DEJES QUE TE ATRAPE!";
    private boolean recolectando = true;
    private float tiempo;
    private float tiempoFinal=5;
    private float tiempoGanador=5;
    private float tiempoMinijuego = 5;
    private float tiempoInstrucciones = 4;
    private int ordenItems, numero1, numero2;;
    private int siguienteJuego = 0;
    private int posXJuanito;
    private int posYJuanito;
    private float velocidad = 10;
    private float posicionMama;
    private float separacion; //La separación original entre Juanito y su mamá.
    private int posicionObjeto =0;
    public static final float ANCHOTOTAL = ANCHO*50;

    //Juego terminado
    private EscenaGameOver escenaGameOver;
    private EscenaGanaste escenaGanaste;
    private Texto puntajeFinal = new Texto();

    //Pausa
    private EscenaPausa escenaPausa;
    private EscenaOpciones escenaOpciones;

    //Alcanzado
    private EscenaAlcanzado escenaAlcanzado;

    //Mapa
    private TiledMap mapa;
    private OrthogonalTiledMapRenderer rendererMapa;

    //Juanito
    private Personaje Juanito;
    private Texture texturaJuanitoquieto;
    private Texture texturaJuanito1;
    private Texture texturaJuanito2;

    //Mama
    private Personaje Mama;
    private Texture texturaMamaquieta;
    private Texture texturaMama1;
    private Texture texturaMama2;

    // Efecto de Sonido
    private Sound cachetada;

    // Retroalimentación de items
    private Texture texturaRespuestaCorrecta;
    private Texture texturaRespuestaIncorrecta;
    private Sound sonidoRespuestaCorrecta;
    private Sound sonidoRespuestaIncorrecta;
    private float tiempoRetroalimentacion = 0;
    private boolean retroalimenta = false;
    private Image retroalimentacion;

    //Vidas
    private int vidas = 3;
    private Texture texturaVidas;

    //Puntaje
    private float puntosJugador = 0;
    private Texto puntaje;

    // Diálogos
    private Texture texturadialo;
    private Texture texturadialogoJuanito;
    private Texture texturadialogoMama;
    private Texture texturaFinalPierde;
    private Texture texturaFinalJuanito;
    private Texture texturaFinalMama;
    private  int contadorDialogo=0;

    // AssetManager
    private AssetManager manager;

    // Procesador de eventos
    private final Procesador procesadorEntrada = new Procesador();

    // HUD
    private OrthographicCamera camaraHUD;
    private Viewport vistaHUD;

    // El HUD lo manejamos con una escena (opcional)
    private Stage escenaHUD;
    private Texto mensajeMinijuego = new Texto();
    Image imgRectangulo;
    ImageButton btnPausa;

    public PantallaJuego(LearningJuanito menu) { this.menu=menu; manager = menu.getAssetManager();}

    @Override
    public void show() {
        tiempo =0;
        crearCamara();
        cargarTexturas();
        cargarSonidos();
        cargarPersonajes();
        playMusic();
        cargarMapa();
        cargarHUD();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(procesadorEntrada);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarSonidos() {
        cachetada = manager.get("Audio/Slap.mp3",Sound.class);
        sonidoRespuestaCorrecta=manager.get("Audio/Correcto.wav",Sound.class);
        sonidoRespuestaIncorrecta=manager.get("Audio/Incorrecto.mp3",Sound.class);
    }

    private void playMusic() {
        if(menu.isMusicOn())
        {
            menu.musicaFondo.play();
        }
        else
        {
            menu.musicaFondo.pause();
        }
    }

    private void cargarHUD() {
        camaraHUD = new OrthographicCamera(ANCHO,ALTO);
        camaraHUD.position.set(ANCHO/2, ALTO/2, 0);
        camaraHUD.update();
        vistaHUD = new StretchViewport(ANCHO, ALTO, camaraHUD);

        // HUD
        escenaHUD = new Stage(vistaHUD);
        crearObjetos();
        //escenaHUD.getActors().get(escenaHUD.getActors().indexOf(imgRectangulo,false)).remove();
        escenaHUD.addActor(btnPausa);
        escenaHUD.addActor(imgRectangulo);

        //Puntaje
        puntaje = new Texto();
        escenaHUD.addActor(puntaje);

        //Instrucciones de Minijuegos
        escenaHUD.addActor(mensajeMinijuego);


    }

    public void cargarMusica(){
        menu.musicaFondo.stop();
        menu.musicaFondo = manager.get("Audio/menuFondo.mp3");
        menu.musicaFondo.setVolume(0.75f);
        menu.musicaFondo.setLooping(true);
        if(menu.isMusicOn())
        {
            menu.musicaFondo.play();
        }
        else
        {
            menu.musicaFondo.pause();
        }
    }

    private void crearObjetos() {
        // Crear rectángulo transparente
        Pixmap pixmap = new Pixmap((int)(ANCHO*0.5f), (int)(ALTO*0.1f), Pixmap.Format.RGBA8888 );
        pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.65f );
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture texturaRectangulo = new Texture( pixmap );
        pixmap.dispose();
        retroalimentacion = new Image(texturaRespuestaCorrecta);
        retroalimentacion.setPosition(ANCHO/2,ALTO/2);
        imgRectangulo = new Image(texturaRectangulo);
        imgRectangulo.setPosition((ANCHO-pixmap.getWidth())/2, ((ALTO*31/20)-pixmap.getHeight())/2);

        // Boton Pausa
        Texture texturaPausa = manager.get("Images/btns/btnPausa.png");
        TextureRegionDrawable trBtnPausa = new TextureRegionDrawable(new TextureRegion(texturaPausa));
        btnPausa = new ImageButton(trBtnPausa);
        btnPausa.setPosition(0, ALTO-btnPausa.getHeight());
    }

    private void dibujarVidas() {
        for(int x = 1;x<=vidas;x++)
        {
            Image imgVida = new Image(texturaVidas);
            imgVida.setPosition(ANCHO*(100-5*x)/100-imgVida.getWidth()/2,105*ALTO/120-imgVida.getHeight()/2);
            escenaHUD.addActor(imgVida);
        }
    }

    private void dibujardialogo(){
        Image imgDialogo;
        switch(contadorDialogo){
            case 1:
                imgDialogo = new Image(texturadialogoMama);
                imgDialogo .setPosition(15*ANCHO/50-imgDialogo.getWidth()/2,70*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            case 2:
                escenaHUD.clear();
                //crearRectangulo();
                imgDialogo = new Image(texturadialogoJuanito);
                imgDialogo .setPosition(22*ANCHO/50-imgDialogo.getWidth()/2,40*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            case 3:
                escenaHUD.clear();
                //crearRectangulo();
                imgDialogo = new Image(texturadialo);
                imgDialogo .setPosition(9*ANCHO/50-imgDialogo.getWidth()/2,43*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            case 4:
                //escenaHUD.clear();
                //crearRectangulo();
                imgDialogo = new Image(texturaFinalPierde);
                imgDialogo .setPosition(9*ANCHO/50-imgDialogo.getWidth()/2,43*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            case 5:
                escenaHUD.clear();
                //crearRectangulo();
                imgDialogo = new Image(texturaFinalMama);
                imgDialogo .setPosition(10*ANCHO/50-imgDialogo.getWidth()/2,62*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            case 6:
                //escenaHUD.clear();
                //crearRectangulo();
                imgDialogo = new Image(texturaFinalJuanito);
                imgDialogo .setPosition(23*ANCHO/50-imgDialogo.getWidth()/2,40*ALTO/100-imgDialogo.getHeight()/2);
                escenaHUD.addActor(imgDialogo );
                break;
            default:break;
        }
    }

    private class EscenaAlcanzado extends Stage
    {
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                estadoJuego = EstadoJuego.CORRIENDO;
                Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                // Regresa el control a la pantalla
                Gdx.input.setInputProcessor(procesadorEntrada);
                // Reiniciar música juego
                if(menu.isMusicOn())
                {
                    menu.musicaFondo.play();
                }
            }
            return true;
        }

        public EscenaAlcanzado(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaAlcanzado;
            Texture texturaBtnContinuar;
            texturaAlcanzado = manager.get("Images/screens/chanclazo.jpg");
            texturaBtnContinuar = manager.get("Images/btns/btnContinuar.png");
            Image imgFondo = new Image(texturaAlcanzado);
            this.addActor(imgFondo);
            // Botón Continuar
            TextureRegionDrawable trdBtnContinuar = new TextureRegionDrawable(new TextureRegion(texturaBtnContinuar));
            ImageButton btnContinuar = new ImageButton(trdBtnContinuar);
            btnContinuar.setPosition(ANCHO/2-btnContinuar.getWidth()/2,ALTO/3-btnContinuar.getHeight()/2);
            this.addActor(btnContinuar);

            // Acción del botón continuar
            btnContinuar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estadoJuego = EstadoJuego.CORRIENDO;
                    Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(procesadorEntrada);
                    // Reiniciar música juego
                    if(menu.isMusicOn())
                    {
                        menu.musicaFondo.play();
                    }
                }
            });
        }
    }

    private class EscenaPausa extends Stage
    {
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                estadoJuego = EstadoJuego.CORRIENDO;
                Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                if(menu.isMusicOn())
                {
                    menu.musicaFondo.play();
                }
                // Regresa el control a la pantalla
                Gdx.input.setInputProcessor(procesadorEntrada);
            }
            return true;
        }

        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaPausa;
            Texture texturaBtnRegresar;
            Texture texturaBtnContinuar;
            Texture texturaBtnOpciones;
            texturaPausa = manager.get("Images/screens/pausa.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            texturaBtnContinuar = manager.get("Images/btns/btnContinuar.png");
            texturaBtnOpciones = manager.get("Images/btns/btnOpcionesPausa.png");
            Image imgFondo = new Image(texturaPausa);
            this.addActor(imgFondo);
            // Botón Regresar
            TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
            btnRegresar.setPosition(ANCHO*11/100+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
            this.addActor(btnRegresar);

            // Acción del botón Regresar
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    cargarMusica();
                    menu.setScreen(new PantallaMenu(menu));
                }
            });
            // Botón Continuar
            TextureRegionDrawable trdBtnContinuar = new TextureRegionDrawable(new TextureRegion(texturaBtnContinuar));
            ImageButton btnContinuar = new ImageButton(trdBtnContinuar);
            btnContinuar.setPosition(ANCHO/2-30-btnContinuar.getWidth()/2,2*ALTO/12+90-btnContinuar.getHeight()/2);
            this.addActor(btnContinuar);

            // Acción del botón continuar
            btnContinuar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estadoJuego = EstadoJuego.CORRIENDO;
                    Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    if(menu.isMusicOn())
                    {
                        menu.musicaFondo.play();
                    }
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(procesadorEntrada);
                }
            });

            // Botón Opciones
            TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
            ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
            btnOpciones.setPosition(85*ANCHO/100-btnOpciones.getWidth()/2,2*ALTO/12-btnOpciones.getHeight()/2);
            this.addActor(btnOpciones);

            // Acción del botón opciones
            btnOpciones.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    estadoJuego = EstadoJuego.OPCIONES;
                }
            });
        }
    }

    private class EscenaOpciones extends Stage
    {
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                estadoJuego = EstadoJuego.PAUSADO;
            }
            return true;
        }

        public EscenaOpciones(Viewport vista, final SpriteBatch batch) {
            super(vista, batch);
            Texture texturaOpciones;
            Texture texturaBtnRegresar;
            Texture texturaBtnMusica;
            Texture texturaBtnMusicaChecked;
            Texture texturaBtnEfecto;
            Texture texturaBtnEfectoChecked;
            texturaOpciones = manager.get("Images/screens/opciones.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnRegresar.png");
            texturaBtnMusica = manager.get("Images/btns/btnSoundOn.png");
            texturaBtnMusicaChecked = manager.get("Images/btns/btnSoundOff.png");
            texturaBtnEfecto = manager.get("Images/btns/btnEfectoOn.png");
            texturaBtnEfectoChecked = manager.get("Images/btns/btnEfectoOff.png");
            Image imgFondo = new Image(texturaOpciones);
            this.addActor(imgFondo);
            // Botón Regresar
            TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
            btnRegresar.setPosition(ANCHO*11/100+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
            this.addActor(btnRegresar);

            // Acción del botón Regresar
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    estadoJuego = EstadoJuego.PAUSADO;
                }
            });
            //boton Musica
            TextureRegionDrawable trdBtnMusic = new TextureRegionDrawable
                    (new TextureRegion(texturaBtnMusica));
            TextureRegionDrawable trdBtnMusicChecked = new TextureRegionDrawable
                    (new TextureRegion(texturaBtnMusicaChecked));
            final ImageButton btnMusic = new ImageButton(trdBtnMusic, trdBtnMusic, trdBtnMusicChecked);
            btnMusic.setPosition(ANCHO/2+220-btnMusic.getWidth()/2,ALTO/2+60-btnMusic.getHeight()/2);
            if(!menu.isMusicOn())
            {
                btnMusic.setChecked(true);
            }
            this.addActor(btnMusic);

            //accion del boton musica
            final ClickListener btnMusicListener = new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menu.turnMusicOn();
                }
            };
            btnMusic.addListener(btnMusicListener);

            //boton Efecto
            TextureRegionDrawable trdBtnEfecto = new TextureRegionDrawable
                    (new TextureRegion(texturaBtnEfecto));
            TextureRegionDrawable trdBtnEfectoChecked = new TextureRegionDrawable
                    (new TextureRegion(texturaBtnEfectoChecked));
            final ImageButton btnEfect = new ImageButton(trdBtnEfecto, trdBtnEfecto, trdBtnEfectoChecked);
            btnEfect.setPosition(ANCHO/2+220-btnEfect.getWidth()/2,ALTO/2-130-btnEfect.getHeight()/2);
            if(!menu.isEffectsOn())
            {
                btnEfect.setChecked(true);
            }
            this.addActor(btnEfect);

            //accion del boton musica
            btnEfect.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menu.turnEffectsOn();
                }
            });
        }
    }

    private class EscenaGameOver extends Stage
    {
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                cargarMusica();
                menu.setScreen(new PantallaMenu(menu));
            }
            return true;
        }

        public EscenaGameOver(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaGameOver;
            Texture texturaBtnRegresar;
            texturaGameOver = manager.get("Images/screens/gameOver.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            Image imgFondo = new Image(texturaGameOver);
            this.addActor(imgFondo);
            // Botón Regresar
            TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
            btnRegresar.setPosition(ANCHO*11/100+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
            this.addActor(btnRegresar);

            // Acción del botón Regresar
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    cargarMusica();
                    menu.setScreen(new PantallaMenu(menu));
                }
            });
            this.addActor(puntajeFinal);
        }
    }

    private class EscenaGanaste extends Stage
    {
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                cargarMusica();
                menu.setScreen(new PantallaMenu(menu));
            }
            return true;
        }

        public EscenaGanaste(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaGanaste;
            Texture texturaBtnRegresar;
            texturaGanaste = manager.get("Images/screens/ganaste.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            Image imgFondo = new Image(texturaGanaste);
            this.addActor(imgFondo);
            // Botón Regresar
            TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable(new TextureRegion(texturaBtnRegresar));
            ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
            btnRegresar.setPosition(ANCHO*11/100+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
            this.addActor(btnRegresar);

            // Acción del botón Regresar
            btnRegresar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    cargarMusica();
                    menu.setScreen(new PantallaMenu(menu));
                }
            });
            this.addActor(puntajeFinal);
            if(Gdx.app.getPreferences("marcador").getInteger("puntaje4",0)<(puntosJugador*10))
            {
                Input.TextInputListener listener = new Input.TextInputListener() {
                    @Override
                    public void input(String text) {// Guarda el mejor marcador con el nombre del jugador
                        Preferences preferences = Gdx.app.getPreferences("marcador");
                        // Obtengo los puntajes actuales
                        Marcador puntaje[] = new Marcador[4];
                        puntaje[0]= new Marcador(text,(int)(puntosJugador*10));
                        puntaje[1]= new Marcador(preferences.getString("nombre3","Vacio"),preferences.getInteger("puntaje3",0));
                        puntaje[2]= new Marcador(preferences.getString("nombre2","Vacio"),preferences.getInteger("puntaje2",0));
                        puntaje[3]= new Marcador(preferences.getString("nombre1","Vacio"),preferences.getInteger("puntaje1",0));
                        ordenaBurbuja(puntaje);
                        preferences.putInteger("puntaje4",puntaje[0].getPuntaje());
                        preferences.putString("nombre4",puntaje[0].getNombre());
                        preferences.putInteger("puntaje3",puntaje[1].getPuntaje());
                        preferences.putString("nombre3",puntaje[1].getNombre());
                        preferences.putInteger("puntaje2",puntaje[2].getPuntaje());
                        preferences.putString("nombre2",puntaje[2].getNombre());
                        preferences.putInteger("puntaje1",puntaje[3].getPuntaje());
                        preferences.putString("nombre1",puntaje[3].getNombre());
                        preferences.flush();
                    }

                    @Override
                    public void canceled() {

                    }
                };
                Gdx.input.getTextInput(listener, "Nuevo record, ingresa tu nombre:", "", "");
            }
        }
    }

    class Marcador{
        int puntaje;
        String nombre;
        public Marcador()
        {
            this.puntaje=0;
            this.nombre = "";
        }
        public Marcador(String nombre,int puntaje)
        {
            this.puntaje = puntaje;
            this.nombre = nombre;
        }
        public java.lang.String getNombre()
        {
            return this.nombre;
        }
        public int getPuntaje()
        {
            return this.puntaje;
        }
        public void setNombre(String nombre)
        {
            this.nombre = nombre;
        }
        public void setPuntaje(int puntaje)
        {
            this.puntaje=puntaje;
        }
    }

    private Marcador[] ordenaBurbuja(Marcador[] puntaje) {
        int N=puntaje.length;
        int i, j, temp;
        java.lang.String nombreTemp;
        for(i=N-1;i>0;i--)
        {
            for(j=0;j<i;j++)
            {
                if(puntaje[j].getPuntaje()>puntaje[j+1].getPuntaje())
                {
                    temp=puntaje[j].getPuntaje();
                    nombreTemp=puntaje[j].getNombre();
                    puntaje[j].setPuntaje(puntaje[j+1].getPuntaje());
                    puntaje[j].setNombre(puntaje[j+1].getNombre());
                    puntaje[j+1].setPuntaje(temp);
                    puntaje[j+1].setNombre(nombreTemp);
                }
            }
        }
        return puntaje;
    }

    private void cargarPersonajes() {
        Juanito = new Personaje(texturaJuanitoquieto,texturaJuanito1,texturaJuanito2,-200,32);
        Mama = new Personaje(texturaMamaquieta,texturaMama1,texturaMama2,-200,32);
    }

    private void cargarMapa() {
        batch = new SpriteBatch();
        mapa = manager.get("Mapa/mapaNivel1.tmx");
        rendererMapa = new OrthogonalTiledMapRenderer(mapa, batch);
        rendererMapa.setView(camara);
        eliminarObjetos();
    }

    private void cargarTexturas() {
        texturaJuanitoquieto = manager.get("Images/personajes/Juanito/Juanito0.png");
        texturaJuanito1 = manager.get("Images/personajes/Juanito/Juanito1.png");
        texturaJuanito2 = manager.get("Images/personajes/Juanito/Juanito2.png");
        texturaMamaquieta = manager.get("Images/personajes/Mama/Mama0.png");
        texturaMama1 = manager.get("Images/personajes/Mama/Mama1.png");
        texturaMama2 = manager.get("Images/personajes/Mama/Mama2.png");
        texturaVidas = manager.get("Images/PantallaJuego/vida.png");
        texturadialo = manager.get("Images/dialogos/dialo.png");
        texturadialogoJuanito = manager.get("Images/dialogos/dialogoJuanito.png");
        texturadialogoMama = manager.get("Images/dialogos/dialogoMama1.png");
        texturaFinalPierde = manager.get("Images/dialogos/finalPierde.png");
        texturaFinalJuanito=manager.get("Images/dialogos/finalGanaJuanito.png");
        texturaFinalMama=manager.get("Images/dialogos/finalGanaMama.png");
        texturaRespuestaCorrecta = manager.get("Images/PantallaJuego/mas100.png");
        texturaRespuestaIncorrecta = manager.get("Images/PantallaJuego/menos50.png");
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
        switch(estadoJuego)
        {
            case CORRIENDO:
                posXJuanito = (int) ((Juanito.sprite.getX() + 32) / 32);
                posYJuanito = (int) (Juanito.sprite.getY() / 32);
                velocidad = velocidad + 0.001f;
                puntosJugador = puntosJugador + delta;
                Juanito.actualizar(mapa);
                Mama.actualizar(mapa);
                Mama.checaSalto(mapa);
                if(recolectando && Juanito.recolectarItems(mapa))
                {
                    recolectando = false;
                    if(ordenItems == 1)
                    {
                        if(posYJuanito >= 4){
                            puntosJugador+=10;
                            eliminarNumeroSuperior();
                            if(menu.isEffectsOn())
                            {
                                sonidoRespuestaCorrecta.setVolume(sonidoRespuestaCorrecta.play(),0.5f);
                            }
                            retroalimentar();
                            retroalimentacion.setDrawable(new TextureRegionDrawable(new TextureRegion(texturaRespuestaCorrecta)));
                            escenaHUD.addActor(retroalimentacion);
                        }
                        else{
                            puntosJugador-=5;
                            if(puntosJugador<0)
                            {
                                puntosJugador = 0;
                            }
                            eliminarNumeroInferior();if(menu.isEffectsOn())
                            {
                                sonidoRespuestaIncorrecta.setVolume(sonidoRespuestaIncorrecta.play(),0.5f);
                            }
                            retroalimentar();
                            retroalimentacion.setDrawable(new TextureRegionDrawable(new TextureRegion(texturaRespuestaIncorrecta)));
                            escenaHUD.addActor(retroalimentacion);
                        }
                    }
                    else
                    {
                        if(posYJuanito >= 4){
                            puntosJugador-=5;
                            if(puntosJugador<0)
                            {
                                puntosJugador = 0;
                            }
                            eliminarNumeroSuperior();if(menu.isEffectsOn())
                            {
                                sonidoRespuestaIncorrecta.setVolume(sonidoRespuestaIncorrecta.play(),0.5f);
                            }
                            retroalimentar();
                            retroalimentacion.setDrawable(new TextureRegionDrawable(new TextureRegion(texturaRespuestaIncorrecta)));
                            escenaHUD.addActor(retroalimentacion);
                        }
                        else{
                            puntosJugador+=10;
                            eliminarNumeroInferior();
                            if(menu.isEffectsOn())
                            {
                                sonidoRespuestaCorrecta.setVolume(sonidoRespuestaCorrecta.play(),0.5f);
                            }
                            retroalimentar();
                            retroalimentacion.setDrawable(new TextureRegionDrawable(new TextureRegion(texturaRespuestaCorrecta)));
                            escenaHUD.addActor(retroalimentacion);
                        }
                    }
                }
                if(posXJuanito+20==posicionObjeto)
                {
                    recolectando = true;
                }
                switch (minijuego)
                {
                    case INSTRUCCIONES:
                        // HAY QUE MOSTRAR LAS INSTRUCCIONES
                        switch (siguienteJuego) {
                            case 0:
                                instruccionMinijuego = "¡SALTA LOS OBSTACULOS!";
                                break;
                            case 1:
                                instruccionMinijuego = "¡ATRAPA LOS PARES!";
                                break;
                            case 2:
                                instruccionMinijuego = "¡ATRAPA LOS NONES!";
                                break;
                            case 3:
                                instruccionMinijuego = "¡ATRAPA LOS MULTIPLOS DE 3!";
                                break;
                        }
                        // TERMINA CAMBIO INSTRUCCIONES
                        if(tiempoInstrucciones<=0) {
                            tiempoMinijuego = 5;
                            instruccionMinijuego = "";
                            escenaHUD.getActors().get(escenaHUD.getActors().indexOf(imgRectangulo,false)).remove();
                            cambiaMinijuego(siguienteJuego);
                        }
                        tiempoInstrucciones-=delta;
                        break;
                    case OBSTACULOS:
                        if(Math.random()>0.5&& posicionObjeto <posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                posicionObjeto = posXJuanito+40;
                                generaObstaculo((int)((Math.random()*10)%4), posicionObjeto,1);
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case PARES:
                        if(posicionObjeto <posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                Random random = new Random();
                                ordenItems = random.nextInt(2);
                                posicionObjeto = posXJuanito+40;
                                numero1 = generaNumeroEntre(1,50)*2;
                                numero2 =  (generaNumeroEntre(1,50)*2)-1;
                                if(ordenItems == 1)
                                {
                                    generaItem(numero1, posicionObjeto,8);
                                    generaItem(numero2, posicionObjeto,1);
                                }
                                else
                                {
                                    generaItem(numero2, posicionObjeto,8);
                                    generaItem(numero1, posicionObjeto,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case NONES:
                        if(posicionObjeto <posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                Random random = new Random();
                                ordenItems = random.nextInt(2);
                                posicionObjeto = posXJuanito+40;
                                numero1 = generaNumeroEntre(1,50)*2;
                                numero2 =  (generaNumeroEntre(1,50)*2)-1;
                                if(ordenItems == 1)
                                {
                                    generaItem(numero2, posicionObjeto,8);
                                    generaItem(numero1, posicionObjeto,1);
                                }
                                else
                                {
                                    generaItem(numero1, posicionObjeto,8);
                                    generaItem(numero2, posicionObjeto,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case MULTIPLOSDETRES:
                        if(posicionObjeto <posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                Random random = new Random();
                                ordenItems = random.nextInt(2);
                                posicionObjeto = posXJuanito+40;
                                numero1 = generaNumeroEntre(1,34)*3;
                                numero2 = (generaNumeroEntre(1,34)*3)-1;
                                if(ordenItems == 1)
                                {
                                    generaItem(numero1, posicionObjeto,8);
                                    generaItem(numero2, posicionObjeto,1);
                                }
                                else
                                {
                                    generaItem(numero1, posicionObjeto,8);
                                    generaItem(numero2, posicionObjeto,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                }
                actualizarCamara();
                if(retroalimenta)
                {
                    tiempoRetroalimentacion-=delta;
                    if(tiempoRetroalimentacion<=0)
                    {
                        escenaHUD.getActors().get(escenaHUD.getActors().indexOf(retroalimentacion,false)).remove();
                        retroalimenta=false;
                    }
                }
                colision();
                batch.begin();
                puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString((int)(puntosJugador*10)), ANCHO*85/100,118*ALTO/120);
                mensajeMinijuego.mostrarMensaje(batch, instruccionMinijuego,ANCHO/2,4*ALTO/5);
                batch.end();
                break;
            case INICIANDO:
                escenaHUD.clear();
                if (tiempo < 2)
                {
                    contadorDialogo=1;
                    dibujardialogo();
                    return;
                }
                else if (tiempo < 3.2)
                {
                    //instruccionMinijuego = "PERO YO QUIERO JUGAR CON MI CONSOLA";
                    Juanito.actualizar(mapa);
                    return;
                }
                else if (tiempo < 3.55)
                {
                    Mama.actualizar(mapa);
                    Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                    return;
                }else if(tiempo<5){
                    contadorDialogo=2;
                    dibujardialogo();
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                    return;}
                else if(tiempo<8)
                {
                    //instruccionMinijuego = "¡SI TE ALCANZO,TE VOY A METER UN CHANCLAZO!";
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                    contadorDialogo=3;
                    dibujardialogo();
                    return;
                }
                //escenaHUD.clear();
                dibujarVidas();
                crearObjetos();
                escenaHUD.addActor(imgRectangulo);
                escenaHUD.addActor(btnPausa);
                batch.begin();
                mensajeMinijuego.mostrarMensaje(batch, instruccionMinijuego,ANCHO/2,4*ALTO/5);
                batch.end();
                posicionMama = camara.position.x-Mama.sprite.getX();
                separacion = Juanito.sprite.getX() - Mama.sprite.getX();
                Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                estadoJuego = EstadoJuego.CORRIENDO; //Cuando termine la animación de inicio, se cambia a CORRIENDO
                break;
            case ALCANZADO:
                if (escenaAlcanzado==null) {
                    escenaAlcanzado = new EscenaAlcanzado(vistaHUD, batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaAlcanzado);
                escenaAlcanzado.draw();
                break;
            case OPCIONES:
                if(escenaOpciones==null)
                {
                    escenaOpciones = new EscenaOpciones(vistaHUD,batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaOpciones);
                escenaOpciones.draw();
                break;
            case PAUSADO:
                if (escenaPausa==null) {
                    escenaPausa = new EscenaPausa(vistaHUD, batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaPausa);
                escenaPausa.draw();
                break;
            case PERDIDO:
                if(tiempoFinal<=0) {
                    //menu.musicaFondo.stop();
                    if (escenaGameOver == null) {
                        escenaGameOver = new EscenaGameOver(vistaHUD, batch);
                        actualizarCamara();
                    }
                    Gdx.input.setInputProcessor(escenaGameOver);
                    escenaGameOver.draw();
                    batch.begin();
                    puntajeFinal.mostrarMensaje(batch, "Puntaje Final: " + Integer.toString((int) (puntosJugador * 10)), ANCHO / 2, ALTO / 3);
                    batch.end();
                    break;
                }else{
                    if(tiempoFinal>=4){
                        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                        Juanito.actualizar(mapa);
                    }else if(tiempoFinal>=2){
                        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                        contadorDialogo=4;
                        dibujardialogo();
                        Juanito.actualizar(mapa);
                    }else if(tiempoFinal>=1){
                        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                        Juanito.actualizar(mapa);
                    }
                    tiempoFinal-=delta;
                }
                break;
            case TERMINADO:
                if(tiempoGanador<=0) {
                    if (escenaGanaste == null) {
                        escenaGanaste = new EscenaGanaste(vistaHUD, batch);
                        actualizarCamara();
                    }
                    Gdx.input.setInputProcessor(escenaGanaste);
                    escenaGanaste.draw();
                    batch.begin();
                    puntajeFinal.mostrarMensaje(batch, "Puntaje Final: " + Integer.toString((int) (puntosJugador * 10)), ANCHO / 2, ALTO / 3);
                    batch.end();
                    break;
                }else{
                    if(tiempoGanador>=4.7){//Sobre 7-8
                        Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
                        Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
                        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                        Juanito.actualizar(mapa);
                    }else if(tiempoGanador>=4){
                        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                        Juanito.actualizar(mapa);
                    }else{
                        //contadorDialogo=6;
                        //dibujardialogo();
                    }
                    tiempoGanador-=delta;
                    break;
                }
        }
    }

    private void retroalimentar() {
        tiempoRetroalimentacion = 0.7f;
        retroalimenta = true;
    }

    private void mostrarInstrucciones() {
        tiempoInstrucciones = 4;
        escenaHUD.addActor(imgRectangulo);
        int juegoAnt = siguienteJuego;
        Random random = new Random();
        do {
            siguienteJuego = random.nextInt(4);
        }while(siguienteJuego==juegoAnt);
        minijuego = Minijuego.INSTRUCCIONES;
    }

    private void cambiaMinijuego(int siguiente) {
        tiempoMinijuego = 5;
        switch (siguiente) {
            case 0:
                minijuego = Minijuego.OBSTACULOS;
                break;
            case 1:
                minijuego = Minijuego.PARES;
                break;
            case 2:
                minijuego = Minijuego.NONES;
                break;
            case 3:
                minijuego = Minijuego.MULTIPLOSDETRES;
                break;
        }
    }

    private int generaNumeroEntre(int min, int max) {
        Random random = new Random();
        int num = random.nextInt(max - min) + min;
        return num;
    }

    private void colision() {
        if(Juanito.Colisiona(Mama))
        {
            if(vidas>0)
            {
                vidas--;
                escenaHUD.clear();
                //Vidas
                dibujarVidas();
                escenaHUD.addActor(btnPausa);
                estadoJuego = EstadoJuego.ALCANZADO;
                Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
                Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
                Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
                eliminarObjetos();
                reacomodarPersonajes();
                menu.musicaFondo.stop();

                // Efecto de sonido (cachetada)
                if(menu.isEffectsOn())
                {
                    cachetada.play();
                }
            } else {
                gameOver();
            }

        }
    }

    private void generaItem(int num, int posX, int posY) // Generará un obstáculo de tipo "tipo" en la posición posX
    {
        if(num<10)
        {
            TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(3);
            TiledMapTileLayer items = (TiledMapTileLayer) mapa.getLayers().get(0);
            for(int y=0;y<=3;y++)
            {
                capa.setCell(posX,posY+y, items.getCell(24+(2*num),25+y));
                capa.setCell(posX+1,posY+y, items.getCell(25+(2*num),25+y));
            }
        }
        else
        {
            int izquierda = num/10;
            generaItem(izquierda,posX,posY);
            generaItem(num-(izquierda*10),posX+2,posY);
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
                capa.setCell(posX,posY+2, obstaculos.getCell(0,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(1,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(1,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(1,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(2,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(2,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(2,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(3,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(3,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(3,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(4,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(4,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(4,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(5,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(5,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(5,27));
                //Séptima columna
                capa.setCell(posX+6,posY, obstaculos.getCell(6,25));
                capa.setCell(posX+6,posY+1, obstaculos.getCell(6,26));
                capa.setCell(posX+6,posY+2, obstaculos.getCell(6,27));
                break;
            case 1: //SILLON 1
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(7,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(7,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(7,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(8,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(8,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(8,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(9,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(9,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(9,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(10,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(10,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(10,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(11,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(11,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(11,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(12,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(12,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(12,27));
                //Séptima columna
                capa.setCell(posX+6,posY, obstaculos.getCell(13,25));
                capa.setCell(posX+6,posY+1, obstaculos.getCell(13,26));
                capa.setCell(posX+6,posY+2, obstaculos.getCell(13,27));
                break;
            case 2: // SILLON 2
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(14,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(14,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(14,27));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(15,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(15,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(15,27));
                //Tercera columna
                capa.setCell(posX+2,posY, obstaculos.getCell(16,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(16,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(16,27));
                //Cuarta columna
                capa.setCell(posX+3,posY, obstaculos.getCell(17,25));
                capa.setCell(posX+3,posY+1, obstaculos.getCell(17,26));
                capa.setCell(posX+3,posY+2, obstaculos.getCell(17,27));
                //Quinta columna
                capa.setCell(posX+4,posY, obstaculos.getCell(18,25));
                capa.setCell(posX+4,posY+1, obstaculos.getCell(18,26));
                capa.setCell(posX+4,posY+2, obstaculos.getCell(18,27));
                //Sexta columna
                capa.setCell(posX+5,posY, obstaculos.getCell(19,25));
                capa.setCell(posX+5,posY+1, obstaculos.getCell(19,26));
                capa.setCell(posX+5,posY+2, obstaculos.getCell(19,27));
                //Séptima columna
                capa.setCell(posX+6,posY, obstaculos.getCell(20,25));
                capa.setCell(posX+6,posY+1, obstaculos.getCell(20,26));
                capa.setCell(posX+6,posY+2, obstaculos.getCell(20,27));
                break;
            case 3: // SILLA
                //Primera columna
                capa.setCell(posX,posY, obstaculos.getCell(21,25));
                capa.setCell(posX,posY+1, obstaculos.getCell(21,26));
                capa.setCell(posX,posY+2, obstaculos.getCell(21,27));
                capa.setCell(posX,posY+3, obstaculos.getCell(21,28));
                capa.setCell(posX,posY+4, obstaculos.getCell(21,29));
                //Segunda columna
                capa.setCell(posX+1,posY, obstaculos.getCell(22,25));
                capa.setCell(posX+1,posY+1, obstaculos.getCell(22,26));
                capa.setCell(posX+1,posY+2, obstaculos.getCell(22,27));
                capa.setCell(posX+1,posY+3, obstaculos.getCell(22,28));
                capa.setCell(posX+1,posY+4, obstaculos.getCell(22,29));
                //Segunda columna
                capa.setCell(posX+2,posY, obstaculos.getCell(23,25));
                capa.setCell(posX+2,posY+1, obstaculos.getCell(23,26));
                capa.setCell(posX+2,posY+2, obstaculos.getCell(23,27));
                capa.setCell(posX+2,posY+3, obstaculos.getCell(23,28));
                capa.setCell(posX+2,posY+4, obstaculos.getCell(23,29));
                break;
        }
    }

    private void eliminarObjetos() {
        TiledMapTileLayer obstaculos = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer items = (TiledMapTileLayer) mapa.getLayers().get(3);
        for (int cordX = 0; cordX <= 2000; cordX++) {
            for (int cordY = 0; cordY <= 25; cordY++) {
                obstaculos.setCell(cordX,cordY, null);
                items.setCell(cordX,cordY, null);
            }
        }
    }

    //ELIMINAR NUMEROS
    private void eliminarNumeroSuperior() {
        TiledMapTileLayer obstaculos = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer items = (TiledMapTileLayer) mapa.getLayers().get(3);
        for (int cx = 0; cx <= 2000; cx++)
        {
            for (int cy = 8; cy <= 15; cy++)
            {
                obstaculos.setCell(cx,cy, null);
                items.setCell(cx,cy, null);
            }
        }
    }

    private void eliminarNumeroInferior() {
        TiledMapTileLayer obstaculos = (TiledMapTileLayer) mapa.getLayers().get(2);
        TiledMapTileLayer items = (TiledMapTileLayer) mapa.getLayers().get(3);
        for (int cx = 0; cx <= 2000; cx++)
        {
            for (int cy = 0; cy <= 7; cy++)
            {
                obstaculos.setCell(cx,cy, null);
                items.setCell(cx,cy, null);
            }
        }
    }

    private void reacomodarPersonajes() {
        //Pone los personajes en su posición original con respecto a la cámara
        Mama.sprite.setY(32);
        Juanito.sprite.setY(32);
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
            else
            {
                ganaste();
            }
        }
        camara.update();
    }

    private void gameOver() {
        estadoJuego = EstadoJuego.PERDIDO;
        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        eliminarObjetos();
        menu.musicaFondo.stop();
        }

    private void ganaste() {
        estadoJuego = EstadoJuego.TERMINADO;
        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        eliminarObjetos();
        menu.musicaFondo.stop();
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
        manager.unload("Images/personajes/Juanito/Juanito0.png");
        manager.unload("Images/personajes/Juanito/Juanito1.png");
        manager.unload("Images/personajes/Juanito/Juanito2.png");
        manager.unload("Images/personajes/Mama/Mama0.png");
        manager.unload("Images/personajes/Mama/Mama1.png");
        manager.unload("Images/personajes/Mama/Mama2.png");
        manager.unload("Images/PantallaJuego/vida.png");
        manager.unload("Images/btns/btnContinuar.png");
        manager.unload("Images/dialogos/dialo.png");
        manager.unload("Images/dialogos/dialogoJuanito.png");
        manager.unload("Images/dialogos/dialogoMama1.png");
        manager.unload("Images/dialogos/finalPierde.png");
        manager.unload("Images/dialogos/finalGanaJuanito.png");
        manager.unload("Images/dialogos/finalGanaMama.png");
        manager.unload("Images/screens/pausa.jpg");
        manager.unload("Images/screens/chanclazo.jpg");
        manager.unload("Images/btns/btnMenuPrinc.png");
        manager.unload("Images/btns/btnOpcionesPausa.png");
        manager.unload("Images/screens/gameOver.jpg");
        manager.unload("Images/screens/ganaste.jpg");
        manager.unload("Mapa/mapaNivel1.tmx");
        manager.unload("Images/btns/btnPausa.png");
        manager.unload("Images/PantallaJuego/mas100.png");
        manager.unload("Images/PantallaJuego/menos50.png");
        manager.unload("Audio/Slap.mp3");
        manager.unload("Audio/Correcto.wav");
        manager.unload("Audio/Incorrecto.mp3");
    }

    public enum EstadoJuego {
        INICIANDO,
        CORRIENDO,
        PAUSADO,
        ALCANZADO,
        PERDIDO,
        OPCIONES,
        TERMINADO
    }

    public enum Minijuego {
        OBSTACULOS,
        PARES,
        NONES,
        MULTIPLOSDETRES,
        INSTRUCCIONES
    }

    private class Procesador implements InputProcessor{
        @Override
        public boolean keyDown(int keycode) {
            if(keycode == Input.Keys.BACK)
            {
                cargarMusica();
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
            if(v.x<200 && v.y < 200){
                if(!(estadoJuego == EstadoJuego.INICIANDO))
                {
                    estadoJuego = EstadoJuego.PAUSADO;
                    menu.musicaFondo.pause();
                }
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