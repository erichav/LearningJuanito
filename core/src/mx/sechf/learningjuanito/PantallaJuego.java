package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Erick Chávez on 15/02/2017.
 */
public class PantallaJuego implements Screen {
    private static final float ANCHO = 1280;
    private static final float ALTO = 800;
    private final LearningJuanito menu;

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
    private Texture texturaJuanito;
    private Texture texturaVida;

    // Sprite batch
    private SpriteBatch batch;

    // Escenas
    private Stage escenaJuego;
    public PantallaJuego(LearningJuanito menu) { this.menu=menu; }

    @Override
    public void show() {
        crearCamara();
        batch = new SpriteBatch();
        cargarTexturas();
        crearObjetos();

        Gdx.input.setInputProcessor(escenaJuego);
        Gdx.input.setCatchBackKey(true);
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
        texturaJuanito = new Texture("Images/objects/Juanito.png");
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
        borrarPantalla();
        escenaJuego.draw();
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            menu.setScreen(new PantallaMenu(menu));
        }
        puntaje.mostrarMensaje(batch, "Puntaje: " + Integer.toString(puntosJugador), ANCHO*85/100,118*ALTO/120);
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
}
