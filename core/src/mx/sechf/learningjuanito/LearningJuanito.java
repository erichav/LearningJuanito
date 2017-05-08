package mx.sechf.learningjuanito;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
	private boolean musicOn;
	private boolean effectsOn;
	private Preferences preferences;

	public LearningJuanito() {
		assetManager = new AssetManager();
	}

	@Override
	public void create () {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver())); // Le permitimos cargar mapas
		revisarSonido();
		setScreen(new PantallaLogo(this)); // Lanzamos el logo del Tec.
	}

	private void revisarSonido() {
		preferences = Gdx.app.getPreferences("marcador");
		musicOn = preferences.getBoolean("Musica",true);
		effectsOn = preferences.getBoolean("Efectos",true);
	}

	public void turnEffectsOn()
	{
		effectsOn = !effectsOn;
		preferences.putBoolean("Efectos",effectsOn);
		preferences.flush();
	}
	public void turnMusicOn()
	{
		musicOn = !musicOn;
		preferences.putBoolean("Musica",musicOn);
		preferences.flush();
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
