package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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

    //Escena Opciones
    private boolean showOptions = false;
    private EscenaOpciones escenaOpciones;

    // AssetManager
    private AssetManager manager;

    // Música
    public PantallaMenu(LearningJuanito menu) {
        this.menu=menu;
        manager = menu.getAssetManager();
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
        btnPlay.setPosition(ANCHO/2-btnPlay.getWidth()/2,5*ALTO/12+5-btnPlay.getHeight()/2);
        escenaMenu.addActor(btnPlay);

        //accion del boton jugar
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    menu.setScreen(new PantallaDificultades(menu ));
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
                if(escenaOpciones==null)
                {
                    escenaOpciones = new EscenaOpciones(vista,batch);
                }
                Gdx.input.setInputProcessor(escenaOpciones);
                showOptions = true;
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

    private void crearCamara() {
        camara = new OrthographicCamera(ANCHO,ALTO);
        camara.position.set(ANCHO/2,ALTO/2,0);
        camara.update();
        vista = new StretchViewport(ANCHO,ALTO,camara);
    }

    private class EscenaOpciones extends Stage
    {
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
                    showOptions = false;
                    Gdx.input.setInputProcessor(escenaMenu);
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
                    cargarMusica();
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

    private void cargarMusica() {
        if(menu.isMusicOn())
        {
            menu.musicaFondo.play();
        }
        else
        {
            menu.musicaFondo.pause();
        }
    }

    @Override
    public void render(float delta) {
        borrarPantalla();
        if(showOptions)
        {
            escenaOpciones.draw();
        }
        else
        {
            escenaMenu.draw();
        }
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
