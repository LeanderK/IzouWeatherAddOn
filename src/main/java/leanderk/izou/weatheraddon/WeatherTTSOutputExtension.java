package leanderk.izou.weatheraddon;

import intellimate.izou.addon.PropertiesContainer;
import intellimate.izou.events.Event;
import intellimate.izou.system.Context;
import leanderk.izou.tts.outputextension.TTSData;
import leanderk.izou.tts.outputextension.TTSOutputExtension;
import leanderk.izou.tts.outputplugin.TTSOutputPlugin;

/**
 * Created by LeanderK on 05/12/14.
 */
public class WeatherTTSOutputExtension extends TTSOutputExtension{
    public static String ID = WeatherTTSOutputExtension.class.getCanonicalName();
    /**
     * creates a new outputExtension with a new id
     *
     * @param propertiesContainer the PropertiesContainer used for generating Sentences
     * @param context the context used
     */
    public WeatherTTSOutputExtension(PropertiesContainer propertiesContainer, Context context) {
        super(ID, propertiesContainer, context);
        setPluginId(TTSOutputPlugin.ID);
        addResourceIdToWishList(Event.FULL_WELCOME_EVENT);
    }

    /**
     * override this class to generate the TTSData.
     * it will be called, when canGenerate returns true for the locale
     *
     * @param event the Event which triggered the generation
     * @return an instance of TTSData, which will then be consumed by the TTSOutputPlugin
     */
    @Override
    public TTSData generateSentence(Event event) {
        return null;
    }

    /**
     * checks if the TTSOutputExtension can generate TTSData fot the locale
     *
     * @param locale the locale of the request
     * @return true if able to generate, false if not
     */
    @Override
    public boolean canGenerateForLanguage(String locale) {
        return false;
    }
}
