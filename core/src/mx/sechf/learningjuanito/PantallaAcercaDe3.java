package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
 * Created by Erick Chávez on 03/05/2017.
 */

public class PantallaAcercaDe3 extends Pantalla {
    private final LearningJuanito menu;

    //texturas
    private Texture texturaAcercaDe3;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnJugar;
    private Texture texturaBtnPrev;
    private Texture texturaBtnNext;

    // Escenas
    private Stage escenaAcercaDe3;

    public PantallaAcercaDe3(LearningJuanito menu) {
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
        escenaAcercaDe3 = new Stage(vista,batch);
        Image imgFondo = new Image(texturaAcercaDe3);
        escenaAcercaDe3.addActor(imgFondo);

        //botonRegresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO/10+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaAcercaDe3.addActor(btnRegresar);

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
        btnJugar.setPosition(9*ANCHO/10-70-btnJugar.getWidth()/2,2*ALTO/12-btnJugar.getHeight()/2);
        escenaAcercaDe3.addActor(btnJugar);

        //accion del boton jugar
        btnJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaDificultades(menu));
            }
        });

        //boton Prev
        TextureRegionDrawable trdBtnPrev = new TextureRegionDrawable
                (new TextureRegion(texturaBtnPrev));
        ImageButton btnPrev = new ImageButton(trdBtnPrev);
        btnPrev.setPosition(ANCHO/2-560-btnPrev.getWidth()/2,2*ALTO/12+280-btnPrev.getHeight()/2);
        escenaAcercaDe3.addActor(btnPrev);

        //accion del boton Prev
        btnPrev.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaAcercaDe2(menu));
            }
        });

        //boton Next
        TextureRegionDrawable trdBtnNext = new TextureRegionDrawable
                (new TextureRegion(texturaBtnNext));
        ImageButton btnNext = new ImageButton(trdBtnNext);
        btnNext.setPosition(ANCHO/2+560-btnNext.getWidth()/2,2*ALTO/12+280-btnNext.getHeight()/2);
        escenaAcercaDe3.addActor(btnNext);

        //accion del boton jugar
        btnPrev.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaAcercaDe2(menu));
            }
        });

        Gdx.input.setInputProcessor(escenaAcercaDe3);
        Gdx.input.setCatchBackKey(true);
    }

    private void cargarTexturas() {
        texturaAcercaDe3 = new Texture("Images/screens/acercade3.jpg");
        texturaBtnRegresar = new Texture("Images/btns/btnMenuPrinc.png");
        texturaBtnJugar = new Texture("Images/btns/btnJugarPantallas.png");
        texturaBtnPrev = new Texture ("Images/btns/btnIzqAcercaDeAzul.png");
        texturaBtnNext = new Texture("Images/btns/btnDerechaAcercaDeGris.png");
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
        escenaAcercaDe3.draw();
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
