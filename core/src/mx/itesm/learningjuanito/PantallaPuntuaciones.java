package mx.itesm.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
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
public class PantallaPuntuaciones extends Pantalla {

    private final LearningJuanito menu;

    //texturas
    private Texture texturaPuntuaciones;
    private Texture texturaBtnRegresar;
    private Texture texturaBtnBorrar;

    // Escenas
    private Stage escenaPuntuaciones;

    //Puntajes
    private Texto puntajes = new Texto();
    private Texto nombres = new Texto();
    private String cadenaPuntajes;
    private String cadenaNombres;

    public PantallaPuntuaciones(LearningJuanito menu) {
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
        escenaPuntuaciones = new Stage(vista,batch);
        Image imgFondo = new Image(texturaPuntuaciones);
        escenaPuntuaciones.addActor(imgFondo);
        cargarTodo();

        //botonRegresar
        TextureRegionDrawable trdBtnRegresar = new TextureRegionDrawable
                (new TextureRegion(texturaBtnRegresar));
        ImageButton btnRegresar = new ImageButton(trdBtnRegresar);
        btnRegresar.setPosition(ANCHO/10+50-btnRegresar.getWidth()/2,2*ALTO/12-btnRegresar.getHeight()/2);
        escenaPuntuaciones.addActor(btnRegresar);

        //accion del boton Regresar
        btnRegresar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setScreen(new PantallaMenu(menu ));
            }
        });

        //boton Borrar
        TextureRegionDrawable trdBtnBorrar= new TextureRegionDrawable
                (new TextureRegion(texturaBtnBorrar));
        ImageButton btnBorrar = new ImageButton(trdBtnBorrar);
        btnBorrar.setPosition(9*ANCHO/10-70-btnBorrar.getWidth()/2,2*ALTO/12-btnBorrar.getHeight()/2);
        escenaPuntuaciones.addActor(btnBorrar);

        //accion del boton borrar
        btnBorrar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                borrarPuntuaciones();
                cargarTodo();
            }
        });

        Gdx.input.setInputProcessor(escenaPuntuaciones);
        Gdx.input.setCatchBackKey(true);
    }

    private void borrarPuntuaciones() {
        Preferences preferences = Gdx.app.getPreferences("marcador");
        preferences.putInteger("puntaje4",0);
        preferences.putString("nombre4",null);
        preferences.putInteger("puntaje3",0);
        preferences.putString("nombre3",null);
        preferences.putInteger("puntaje2",0);
        preferences.putString("nombre2",null);
        preferences.putInteger("puntaje1",0);
        preferences.putString("nombre1",null);
        preferences.flush();
    }

    private void cargarTodo() {
        cadenaNombres = cargarNombres();
        cadenaPuntajes = cargarPuntajes();
        puntajes.setColor(Color.BLACK);
        nombres.setColor(Color.BLACK);
        escenaPuntuaciones.addActor(puntajes);
        escenaPuntuaciones.addActor(nombres);
    }

    private String cargarNombres() {
        String s = "";
        s+=Gdx.app.getPreferences("marcador").getString("nombre1","Vacio")+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getString("nombre2","Vacio")+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getString("nombre3","Vacio")+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getString("nombre4","Vacio");
        return s;
    }
    private String cargarPuntajes() {
        String s = "";
        s+=Gdx.app.getPreferences("marcador").getInteger("puntaje1",0)+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getInteger("puntaje2",0)+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getInteger("puntaje3",0)+"\n\n";
        s+=Gdx.app.getPreferences("marcador").getInteger("puntaje4",0);
        return s;
    }

    private void cargarTexturas() {
        texturaPuntuaciones = new Texture("Images/screens/puntuaciones.jpg");
        texturaBtnRegresar = new Texture("Images/btns/btnMenuPrinc.png");
        texturaBtnBorrar = new Texture("Images/btns/btnBorrarPuntajes.png");
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
        escenaPuntuaciones.draw();
        batch.begin();
        nombres.mostrarMensaje(batch,cadenaNombres,ANCHO*13/40,ALTO*2/3);
        puntajes.mostrarMensaje(batch,cadenaPuntajes,ANCHO*25/40,ALTO*2/3);
        batch.end();
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
