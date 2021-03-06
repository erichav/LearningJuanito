package mx.itesm.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaDificultades extends Pantalla {

    private final LearningJuanito menu;

    //texturas
    private Texture texturaDificultades;
    private Texture texturaBtnPrincipiante;
    private Texture texturaBtnIntermedio;
    private Texture texturaBtnExperto;
    private Texture texturaBtnMenu;

    // Escenas
    private Stage escenaDificultades;

    private AssetManager manager;

    public PantallaDificultades(LearningJuanito menu) { this.menu=menu;
    this.manager = menu.getAssetManager();}

    @Override
    public void show() {
        crearCamara();
        batch = new SpriteBatch();
        cargarTexturas();
        crearObjetos();

        Gdx.input.setInputProcessor(escenaDificultades);
        Gdx.input.setCatchBackKey(true);
    }
    private void crearObjetos() {
        escenaDificultades = new Stage(vista,batch);
        Image imgFondo = new Image(texturaDificultades);
        escenaDificultades.addActor(imgFondo);

        //botonPrincipiante
        TextureRegionDrawable trdBtnPrincipiante = new TextureRegionDrawable
                (new TextureRegion(texturaBtnPrincipiante));
        ImageButton btnPrincipiante = new ImageButton(trdBtnPrincipiante);
        btnPrincipiante.setPosition(ANCHO*2/10-btnPrincipiante.getWidth()/2,2*ALTO/12-btnPrincipiante.getHeight()/2);
        escenaDificultades.addActor(btnPrincipiante);

        //accion del boton Principiante
        btnPrincipiante.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cargarMusica();
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_PRINCIPIANTE));
            }
        });

        //botonIntermedio
        TextureRegionDrawable trdBtnIntermedio = new TextureRegionDrawable
                (new TextureRegion(texturaBtnIntermedio));
        ImageButton btnIntermedio = new ImageButton(trdBtnIntermedio);
        btnIntermedio.setPosition(ANCHO*5/10-btnIntermedio.getWidth()/2,2*ALTO/12-btnIntermedio.getHeight()/2);
        escenaDificultades.addActor(btnIntermedio);

        //accion del boton Intermedio
        btnIntermedio.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cargarMusica();
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_INTERMEDIO));
            }
        });

        //botonExperto
        TextureRegionDrawable trdBtnExperto = new TextureRegionDrawable
                (new TextureRegion(texturaBtnExperto));
        ImageButton btnExperto = new ImageButton(trdBtnExperto);
        btnExperto.setPosition(ANCHO*8/10-btnExperto.getWidth()/2,2*ALTO/12-btnExperto.getHeight()/2);
        escenaDificultades.addActor(btnExperto);

        //accion del boton Experto
        btnExperto.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cargarMusica();
                menu.setScreen(new PantallaCargando(menu, Pantallas.NIVEL_EXPERTO));
            }
        });

        //boton Menu
        TextureRegionDrawable trdBtnMenu = new TextureRegionDrawable(new TextureRegion(texturaBtnMenu));
        ImageButton btnMenu = new ImageButton(trdBtnMenu);
        btnMenu.setPosition(ANCHO/2-btnMenu.getWidth()/2,5*ALTO/12+10-btnMenu.getHeight()/2);
        escenaDificultades.addActor(btnMenu);

        // accion btn menu
        btnMenu.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu));
            }
        });
    }

    public void cargarMusica(){
        menu.musicaFondo.stop();
        menu.musicaFondo = manager.get("Audio/Fondo.mp3");
        menu.musicaFondo.setVolume(0.75f);
        menu.musicaFondo.setLooping(true);
    }

    private void cargarTexturas() {
        texturaDificultades = new Texture("Images/screens/dificultades.jpg");
        texturaBtnPrincipiante = new Texture("Images/btns/btnPrincipiante.png");
        texturaBtnIntermedio = new Texture("Images/btns/btnIntermedio.png");
        texturaBtnExperto = new Texture("Images/btns/btnExperto.png");
        texturaBtnMenu = new Texture("Images/btns/btnRegresarMenu.png");
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
        escenaDificultades.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }
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
