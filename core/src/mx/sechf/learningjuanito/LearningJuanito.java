package mx.sechf.learningjuanito;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class LearningJuanito extends Game {
	SpriteBatch batch;
	private final AssetManager assetManager;
	public Music musicaFondo;
	private boolean musicOn = true;
	private boolean effectsOn = true;

	public LearningJuanito() {
		assetManager = new AssetManager();
	}

	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver())); // Le permitimos cargar mapas
		setScreen(new PantallaLogo(this)); // Lanzamos el logo del Tec.
	}
	public void turnEffectsOn()
	{
		this.effectsOn = !effectsOn;
	}
	public void turnMusicOn()
	{
		this.musicOn = !musicOn;
	}
	public boolean isMusicOn()
	{
		return musicOn;
	}
	public boolean isEffectsOn()
	{
		return effectsOn;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	} // Para que las demás pantallas puedan usar el AssetManager

	@Override
	public void dispose() {
		super.dispose();
		assetManager.clear();
	}
}
