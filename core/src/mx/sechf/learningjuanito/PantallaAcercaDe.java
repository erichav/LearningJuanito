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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Erick Chávez on 03/02/2017.
 */
public class PantallaAcercaDe extends Pantalla {

    private final LearningJuanito menu;

    //texturas
    private Texture texturaAcercaDe;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;

    // Escenas
    private Stage escenaAcercaDe;

    public PantallaAcercaDe(LearningJuanito menu) {
        this.menu=menu;
    }

    @Override
    public void show() {
        crearCamara();
        cargarTexturas();
        crearObjetos();
    }

    private void crearObjetos() {
        batch = new SpriteBatch();
        escenaAcercaDe = new Stage(vista,batch);
        Image imgFondo = new Image(texturaAcercaDe);
        escenaAcercaDe.addActor(imgFondo);

        //botonRegresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO/10-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaAcercaDe.addActor(btnRegresar);

        //accion del boton Regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu ));
            }
        });

        //boton Jugar
        TextureRegionDrawable trdBtnJugar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnJugar));
        ImageButton btnJugar = new ImageButton(trdBtnJugar);
        btnJugar.setPosition(9*ANCHO/10-btnJugar.getWidth()/2,2*ALTO/12-btnJugar.getHeight()/2);
        escenaAcercaDe.addActor(btnJugar);

        //accion del boton jugar
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaDificultades(menu));
            }
        });

        Gdx.input.setInputProcessor(escenaAcercaDe);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaAcercaDe = new Texture("Images/screens/acercade.jpg");
        texturaBtnRegresar = new Texture("Images/btns/btnMenuPrinc.png");
        texturaBtnJugar = new Texture("Images/btns/btnJugarPantallas.png");
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
        escenaAcercaDe.draw();
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
