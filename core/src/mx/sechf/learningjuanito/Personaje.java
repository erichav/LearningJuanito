package mx.sechf.learningjuanito;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Erick Chávez on 28/02/2017.
 */

public class Personaje extends Objeto
{
    private float velocidad = 10; // Velocidad inicial de Juanito
    private int ANCHO_PERSONAJE, ALTO_PERSONAJE;

    private Animation<TextureRegion> spriteAnimado;         // Animación de Juanito caminando
    private float timerAnimacion, yInicial, tiempo;                          // Tiempo para cambiar frames de la animación

    private EstadoMovimiento estadoMovimiento = EstadoMovimiento.INICIANDO;
    private EstadoSalto estadoSalto = EstadoSalto.EN_PISO;

    // Recibe la imagen de Juanito con todos sus frames
    public Personaje(Texture textura, int w, int h, float x, float y) {
        this.ANCHO_PERSONAJE = w;
        this.ALTO_PERSONAJE = h;
        // Lee la textura como región
        TextureRegion texturaCompleta = new TextureRegion(textura);
        // La divide en 4 frames de 32x64
        TextureRegion[][] texturaPersonaje = texturaCompleta.split(w,h);
        // Crea la animación con tiempo de 0.25 segundos entre frames.

        spriteAnimado = new Animation(0.1f, texturaPersonaje[0][0], texturaPersonaje[0][1], texturaPersonaje[0][2], texturaPersonaje[0][3], texturaPersonaje[0][2], texturaPersonaje[0][1] );
        // Animación infinita
        spriteAnimado.setPlayMode(Animation.PlayMode.LOOP);
        // Inicia el timer que contará tiempo para saber qué frame se dibuja
        timerAnimacion = 0;
        // Crea el sprite con el personaje quieto
        sprite = new Sprite(texturaPersonaje[0][0]);    // QUIETO
        sprite.setPosition(x,y);    // Posición inicial
    }

    // Dibuja el personaje
    public void dibujar(SpriteBatch batch) {
        // Dibuja el personaje dependiendo del estadoMovimiento
        switch (estadoMovimiento) {
            case MOV_DERECHA:
            case INICIANDO:
                timerAnimacion += Gdx.graphics.getDeltaTime();
                // Frame que se dibujará
                TextureRegion region = spriteAnimado.getKeyFrame(timerAnimacion);
                batch.draw(region,sprite.getX(),sprite.getY());
                break;
            case QUIETO:
                sprite.draw(batch); // Dibuja el sprite estático
                break;
        }
    }

    // Actualiza el sprite, de acuerdo al estadoMovimiento y estadoSalto
    public void actualizar(TiledMap mapa) {
        switch (estadoMovimiento) {
            case MOV_DERECHA:
                verificaSalto();
                moverHorizontal(mapa);
                break;
            case INICIANDO:
                moverDerecha(mapa);
            case QUIETO:
                verificaSalto();
                break;
        }
    }

    private void moverDerecha(TiledMap mapa) {sprite.setX(sprite.getX()+velocidad);
    }

    private void verificaSalto() {
        if(estadoSalto == EstadoSalto.SUBIENDO)
        {
            sprite.setY((int)(yInicial+20*tiempo-(0.98*tiempo*tiempo)/2));
            tiempo++;
            if(sprite.getY() <= yInicial)
            {
                sprite.setY(yInicial);
                estadoSalto = EstadoSalto.EN_PISO;
            }
        }
    }


    // Mueve el personaje a la derecha/izquierda, prueba choques con paredes
    private void moverHorizontal(TiledMap mapa) {
        // Obtiene la primer capa del mapa (en este caso es la única)
        TiledMapTileLayer capa = (TiledMapTileLayer) mapa.getLayers().get(2);
        // Ejecutar movimiento horizontal
        float nuevaX = sprite.getX();
        // ¿Quiere ir a la Derecha?
        if ( estadoMovimiento== EstadoMovimiento.MOV_DERECHA) {
            velocidad = velocidad + 0.005f;
            // Obtiene el bloque del lado derecho. Asigna null si puede pasar.
            int x = (int) ((sprite.getX() + 32) / 32);   // Convierte coordenadas del mundo en coordenadas del mapa
            int y = (int) (sprite.getY() / 32);
            TiledMapTileLayer.Cell celdaDerecha = capa.getCell(x, y);
            if (celdaDerecha != null) {
                Object tipo = (String) celdaDerecha.getTile().getProperties().get("tipo");
                if (!"obstaculo".equals(tipo)) {
                    celdaDerecha = null;  // Puede pasar
                }
            }
            if ( celdaDerecha==null) {
                // Ejecutar movimiento horizontal
                nuevaX += velocidad;
                // Prueba que no salga del mundo por la derecha
                if (nuevaX <= PantallaJuego.ANCHOTOTAL - sprite.getWidth()) {
                    sprite.setX(nuevaX);
                }
                else
                {
                    // AQUÍ VA LE CÓDIGO CUANDO JUANITO TERMINA EL NIVEL
                }
            }
        }
    }
    // Revisa si toca un numero
    public boolean recolectarObjetos(TiledMap mapa) {
        // Revisar si toca una moneda (pies)
        TiledMapTileLayer capa = (TiledMapTileLayer)mapa.getLayers().get(1);
        int x = (int)(sprite.getX()/ ANCHO_PERSONAJE)+1;
        int y = (int)(sprite.getY()/ ALTO_PERSONAJE);
        TiledMapTileLayer.Cell celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "numero".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                //capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        x = (int)(sprite.getX()/ ANCHO_PERSONAJE)+1;
        y = (int)(sprite.getY()/ ALTO_PERSONAJE)+1;
        celda = capa.getCell(x,y);
        if (celda!=null ) {
            Object tipo = celda.getTile().getProperties().get("tipo");
            if ( "numero".equals(tipo) ) {
                capa.setCell(x,y,null);    // Borra la moneda del mapa
                //capa.setCell(x,y,capa.getCell(0,4)); // Cuadro azul en lugar de la moneda
                return true;
            }
        }
        return false;
    }

    // Accesor de estadoMovimiento
    public EstadoMovimiento getEstadoMovimiento() {
        return estadoMovimiento;
    }

    // Modificador de estadoMovimiento
    public void setEstadoMovimiento(EstadoMovimiento estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public void saltar() {
        if (estadoSalto!=EstadoSalto.SUBIENDO && estadoSalto!=EstadoSalto.BAJANDO) {
            // inicia
            estadoSalto = EstadoSalto.SUBIENDO;
            yInicial = sprite.getY();
            tiempo = 1;
        }
    }

    public boolean Colisiona(Personaje personaje) {
        if(sprite.getX() <= (personaje.sprite.getX()+personaje.sprite.getWidth()-60))
        {
            return true;
        }
        return false;
    }

    public enum EstadoMovimiento {
        INICIANDO,
        MOV_DERECHA,
        QUIETO
    }
    public enum EstadoSalto {
        SUBIENDO,
        BAJANDO,
        EN_PISO
    }
}
