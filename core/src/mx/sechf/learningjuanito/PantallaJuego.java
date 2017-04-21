package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
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

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaJuego extends Pantalla {

    private final LearningJuanito menu;
    public EstadoJuego estadoJuego = EstadoJuego.INICIANDO;
    public Minijuego minijuego = Minijuego.INSTRUCCIONES;
    private String instruccionMinijuego = "NO DEJES QUE TE ATRAPE!";
    private float tiempo;
    private float tiempoMinijuego = 5;
    private float tiempoInstrucciones = 3;
    private int ordenItems;
    private int siguienteJuego = (int) (Math.random() * 4);
    private float velocidad = 10;
    private float posicionMama;
    private float separacion; //La separación original entre Juanito y su mamá.
    private int posicionObstaculo=0;
    public static final float ANCHOTOTAL = ANCHO*50;

    //Juego terminado
    private EscenaGameOver escenaGameOver;
    private EscenaGanaste escenaGanaste;
    private Texto puntajeFinal = new Texto();

    //Pausa
    private EscenaPausa escenaPausa;

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

    // Efecto de Sonido
    private Sound cachetada;

    //Vidas
    private int vidas = 3;
    private Texture texturaVidas;

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
    private Texto mensajeMinijuego = new Texto();
    Image imgRectangulo;
    ImageButton btnPausa;

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
        crearObjetos();
        //escenaHUD.getActors().get(escenaHUD.getActors().indexOf(imgRectangulo,false)).remove();
        dibujarVidas();
        escenaHUD.addActor(btnPausa);
        escenaHUD.addActor(imgRectangulo);

        //Puntaje
        puntaje = new Texto();
        escenaHUD.addActor(puntaje);

        //Instrucciones de Minijuegos
        escenaHUD.addActor(mensajeMinijuego);


    }

    private void crearObjetos() {
        // Crear rectángulo transparente
        Pixmap pixmap = new Pixmap((int)(ANCHO*0.5f), (int)(ALTO*0.1f), Pixmap.Format.RGBA8888 );
        pixmap.setColor( 0.1f, 0.1f, 0.1f, 0.65f );
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture texturaRectangulo = new Texture( pixmap );
        pixmap.dispose();
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
            Texture texturabtnContinuar = manager.get("Images/btns/btnContinuar.png");
            TextureRegionDrawable trdContinuar = new TextureRegionDrawable(
                    new TextureRegion(texturabtnContinuar));
            ImageButton btnContinuar = new ImageButton(trdContinuar);
            btnContinuar.setPosition(ANCHO/2-btnContinuar.getWidth()/2, ALTO/5);
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
                    musicaFondo.play();
                }
            });
            this.addActor(btnContinuar);
        }
    }

    private class EscenaPausa extends Stage
    {
        public EscenaPausa(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaPausa;
            Texture texturaBtnRegresar;
            Texture texturaBtnJugar;
            Texture texturaBtnOpciones;
            texturaPausa = manager.get("Images/screens/pausa.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            texturaBtnJugar = manager.get("Images/btns/btnJugarPausa.png");
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
                    menu.setScreen(new PantallaCargando(menu,Pantallas.MENU));
                    musicaFondo.stop();
                }
            });
            // Botón Jugar
            TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable(new TextureRegion(texturaBtnJugar));
            ImageButton btnJugar = new ImageButton(trdBtnJugar);
            btnJugar.setPosition(ANCHO/2-30-btnJugar.getWidth()/2,2*ALTO/12+90-btnJugar.getHeight()/2);
            this.addActor(btnJugar);

            // Acción del botón jugar
            btnJugar.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Continuar el juego
                    estadoJuego = EstadoJuego.CORRIENDO;
                    Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.MOV_DERECHA);
                    musicaFondo.play();
                    // Regresa el control a la pantalla
                    Gdx.input.setInputProcessor(procesadorEntrada);
                }
            });

            // Botón Opciones
            TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
            ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
            btnOpciones.setPosition(85*ANCHO/100-btnOpciones.getWidth()/2,2*ALTO/12-btnOpciones.getHeight()/2);
            this.addActor(btnOpciones);

            /*// Acción del botón opciones
            btnOpciones.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaOpciones(menu));
                }
            });*/
        }
    }
    private class EscenaGameOver extends Stage
    {
        public EscenaGameOver(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaGameOver;
            Texture texturaBtnRegresar;
            Texture texturaBtnJugar;
            Texture texturaBtnOpciones;
            texturaGameOver = manager.get("Images/screens/gameOver.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            texturaBtnOpciones = manager.get("Images/btns/btnOpcionesPausa.png");
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
                    menu.setScreen(new PantallaCargando(menu,Pantallas.MENU));
                    musicaFondo.stop();
                }
            });
            this.addActor(puntajeFinal);

            // Botón Opciones
            TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
            ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
            btnOpciones.setPosition(85*ANCHO/100-btnOpciones.getWidth()/2,2*ALTO/12-btnOpciones.getHeight()/2);
            this.addActor(btnOpciones);

            // Acción del botón opciones
            btnOpciones.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaOpciones(menu));
                }
            });
        }
    }
    private class EscenaGanaste extends Stage
    {
        public EscenaGanaste(Viewport vista, SpriteBatch batch) {
            super(vista, batch);
            Texture texturaGanaste;
            Texture texturaBtnRegresar;
            Texture texturaBtnJugar;
            Texture texturaBtnOpciones;
            texturaGanaste = manager.get("Images/screens/ganaste.jpg");
            texturaBtnRegresar = manager.get("Images/btns/btnMenuPrinc.png");
            texturaBtnOpciones = manager.get("Images/btns/btnOpcionesPausa.png");
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
                    menu.setScreen(new PantallaCargando(menu,Pantallas.MENU));
                    musicaFondo.stop();
                }
            });
            this.addActor(puntajeFinal);

            // Botón Opciones
            TextureRegionDrawable trdBtnOpciones = new TextureRegionDrawable(new TextureRegion(texturaBtnOpciones));
            ImageButton btnOpciones = new ImageButton(trdBtnOpciones);
            btnOpciones.setPosition(85*ANCHO/100-btnOpciones.getWidth()/2,2*ALTO/12-btnOpciones.getHeight()/2);
            this.addActor(btnOpciones);

            // Acción del botón opciones
            btnOpciones.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaOpciones(menu));
                }
            });
        }
    }

    private void cargarPersonajes() {
        Juanito = new Personaje(texturaJuanito,90,180,-200,64);
        Mama = new Personaje(texturaMama,120,240,-200,64);
    }

    private void cargarMapa() {
        batch = new SpriteBatch();
        mapa = manager.get("Mapa/mapaNivel1.tmx");
        musicaFondo = manager.get("Audio/Fondo.mp3");
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.35f);
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
        switch(estadoJuego)
        {
            case CORRIENDO:
                int posXJuanito = (int) ((Juanito.sprite.getX() + 32) / 32);
                int posYJuanito = (int) (Juanito.sprite.getY() / 32);
                velocidad = velocidad + 0.001f;
                puntosJugador = puntosJugador + delta;
                Juanito.actualizar(mapa);
                Mama.actualizar(mapa);
                Mama.checaSalto(mapa);
                if(Juanito.recolectarItems(mapa))
                {
                    if(ordenItems == 1)
                    {
                        if(posYJuanito >= 4){
                            puntosJugador+=10;
                        }
                        else{
                            puntosJugador-=10;
                            if(puntosJugador<0)
                            {
                                puntosJugador = 0;
                            }
                        }
                    }
                    else
                    {
                        if(posYJuanito >= 4){
                            puntosJugador-=10;
                            if(puntosJugador<0)
                            {
                                puntosJugador = 0;
                            }
                        }
                        else{
                            puntosJugador+=10;
                        }
                    }
                }
                switch (minijuego)
                {
                    case INSTRUCCIONES:
                        // HAY QUE MOSTRAR LAS INSTRUCCIONES
                        switch (siguienteJuego) {
                            case 0:
                                instruccionMinijuego = "SALTA LOS OBSTACULOS!";
                                break;
                            case 1:
                                instruccionMinijuego = "ATRAPA LOS PARES!";
                                break;
                            case 2:
                                instruccionMinijuego = "ATRAPA LOS NONES!";
                                break;
                            case 3:
                                instruccionMinijuego = "ATRAPA LOS MULTIPLOS DE 3!";
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
                        if(Math.random()>0.5&&posicionObstaculo<posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                posicionObstaculo = posXJuanito+40;
                                generaObstaculo((int)((Math.random()*10)%4),posicionObstaculo,2);
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case PARES:
                        if(posicionObstaculo<posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                ordenItems = (int)(Math.random()*2)+1;
                                posicionObstaculo = posXJuanito+40;
                                int par = generaMultiplo(2);
                                int impar =  generaNoMultiplo(2);
                                if(ordenItems == 1)
                                {
                                    generaItem(par,posicionObstaculo,8);
                                    generaItem(impar,posicionObstaculo,1);
                                }
                                else
                                {
                                    generaItem(impar,posicionObstaculo,8);
                                    generaItem(par,posicionObstaculo,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case NONES:
                        if(posicionObstaculo<posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                ordenItems = (int)(Math.random()*2)+1;
                                posicionObstaculo = posXJuanito+40;
                                int par = generaMultiplo(2);
                                int impar =  generaNoMultiplo(2);
                                if(ordenItems == 1)
                                {
                                    generaItem(impar,posicionObstaculo,8);
                                    generaItem(par,posicionObstaculo,1);
                                }
                                else
                                {
                                    generaItem(par,posicionObstaculo,8);
                                    generaItem(impar,posicionObstaculo,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                    case MULTIPLOSDETRES:
                        if(posicionObstaculo<posXJuanito)
                        {
                            if(tiempoMinijuego==0)
                            {
                                mostrarInstrucciones();
                            }
                            else
                            {
                                ordenItems = (int)(Math.random()*2)+1;
                                posicionObstaculo = posXJuanito+40;
                                int multiplo = generaMultiplo(3);
                                int nomultiplo = generaNoMultiplo(3);
                                if(ordenItems == 1)
                                {
                                    generaItem(multiplo,posicionObstaculo,8);
                                    generaItem(nomultiplo,posicionObstaculo,1);
                                }
                                else
                                {
                                    generaItem(nomultiplo,posicionObstaculo,8);
                                    generaItem(multiplo,posicionObstaculo,1);
                                }
                                tiempoMinijuego--;
                            }
                        }
                        break;
                }
                actualizarCamara();
                colision();
                batch.begin();
                puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString((int)(puntosJugador*10)), ANCHO*85/100,118*ALTO/120);
                mensajeMinijuego.mostrarMensaje(batch, instruccionMinijuego,ANCHO/2,4*ALTO/5);
                batch.end();
                break;
            case INICIANDO:
                batch.begin();
                mensajeMinijuego.mostrarMensaje(batch, instruccionMinijuego,ANCHO/2,4*ALTO/5);
                batch.end();
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
                break;
            case ALCANZADO:
                if (escenaAlcanzado==null) {
                    escenaAlcanzado = new EscenaAlcanzado(vistaHUD, batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaAlcanzado);
                escenaAlcanzado.draw();
                batch.begin();
                chanclazo.mostrarMensaje(batch, "            CHANCLAZO\n\nHas perdido una vida", ANCHO/2,ALTO*2/3);
                batch.end();
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
                if (escenaGameOver==null) {
                    escenaGameOver = new EscenaGameOver(vistaHUD, batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaGameOver);
                escenaGameOver.draw();
                batch.begin();
                puntajeFinal.mostrarMensaje(batch, "Puntaje Final: " + Integer.toString((int)(puntosJugador*10)), ANCHO/2,ALTO/3);
                batch.end();
                break;
            case TERMINADO:
                if (escenaGanaste==null) {
                    escenaGanaste = new EscenaGanaste(vistaHUD, batch);
                    actualizarCamara();
                }
                Gdx.input.setInputProcessor(escenaGanaste);
                escenaGanaste.draw();
                batch.begin();
                puntajeFinal.mostrarMensaje(batch, "Puntaje Final: " + Integer.toString((int)(puntosJugador*10)), ANCHO/2,ALTO/3);
                batch.end();
                break;
        }
    }

    private void mostrarInstrucciones() {
        tiempoInstrucciones = 6;
        escenaHUD.addActor(imgRectangulo);
        siguienteJuego = (int) (Math.random() * 4);
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

    private int generaMultiplo(int x) {
        int num;
        do{
            num = (int)(Math.random()*9)+1;
        }while(num%x!=0);
        return num;
    }
    private int generaNoMultiplo(int x) {
        int num;
        do{
            num = (int)(Math.random()*9)+1;
        }while(num%x==0);
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
                musicaFondo.stop();

                // Efecto de sonido (cachetada)
                cachetada = manager.get("Audio/Slap2.mp3");
                cachetada.play();
            } else {
                gameOver();
            }

        }
    }

    private void generaItem(int num, int posX, int posY) // Generará un obstáculo de tipo "tipo" en la posición posX
    {
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(3);
        TiledMapTileLayer obstaculos = (TiledMapTileLayer) mapa.getLayers().get(0);
        for(int y=0;y<=3;y++)
        {
            capa.setCell(posX,posY+y, obstaculos.getCell(24+(2*num),25+y));
            capa.setCell(posX+1,posY+y, obstaculos.getCell(25+(2*num),25+y));
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
        musicaFondo.stop();
    }
    private void ganaste() {
        estadoJuego = EstadoJuego.TERMINADO;
        Juanito.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Mama.setEstadoMovimiento(Personaje.EstadoMovimiento.QUIETO);
        Juanito.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        Mama.setEstadoSalto(Personaje.EstadoSalto.EN_PISO);
        eliminarObjetos();
        musicaFondo.stop();
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
        manager.unload("Images/screens/pausa.jpg");
        manager.unload("Images/btns/btnMenuPrinc.png");
        manager.unload("Images/btns/btnJugarPausa.png");
        manager.unload("Images/btns/btnOpcionesPausa.png");
        manager.unload("Images/screens/gameOver.jpg");
        manager.unload("Images/screens/ganaste.jpg");
        manager.unload("Mapa/mapaNivel1.tmx");
        manager.unload("Audio/Fondo.mp3");
        manager.unload("Audio/Slap2.mp3");
    }

    public enum EstadoJuego {
        INICIANDO,
        CORRIENDO,
        PAUSADO,
        ALCANZADO,
        PERDIDO, TERMINADO
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
            if(v.x<200 && v.y < 200){
                if(!(estadoJuego == EstadoJuego.INICIANDO))
                {
                    estadoJuego = EstadoJuego.PAUSADO;
                    musicaFondo.pause();
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