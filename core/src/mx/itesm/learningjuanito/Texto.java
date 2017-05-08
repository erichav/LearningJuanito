package mx.itesm.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by ethan on 03/02/2017.
 */

public class Texto extends Actor {
    private BitmapFont font;
    private Color color;

    public Texto(){
        font = new BitmapFont(Gdx.files.internal("Fonts/fuentePuntaje.fnt"));
        color = Color.WHITE;
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y){
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.setColor(color);
        font.draw(batch, glyp, x-anchoTexto/2, y);
    }
    public void setColor(Color color)
    {
        this.color = color;
    }
}
