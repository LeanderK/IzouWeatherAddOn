package leanderk.izou.weatheraddon;

import intellimate.izou.addon.PropertiesContainer;
import intellimate.izou.events.Event;
import intellimate.izou.resource.Resource;
import intellimate.izou.system.Context;
import leanderk.izou.tts.outputextension.TTSData;
import leanderk.izou.tts.outputextension.TTSOutputExtension;
import leanderk.izou.tts.outputplugin.TTSOutputPlugin;
import leanderk.izou.weatheraddon.weather.WeatherChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by LeanderK on 05/12/14.
 */
public class WeatherTTSOutputExtension extends TTSOutputExtension{
    public static String ID = WeatherTTSOutputExtension.class.getCanonicalName();
    public static final String TTS_OVERVIEW_Rating = "overviewRating-";
    public static final String TTS_OVERVIEW_CODE = "overviewCode-";
    public static final String TTS_DESCRIPTION_WITHOUT_DATE = "withoutDateDescription-";
    public static final String TTS_TODAY = "today";
    /**
     * creates a new outputExtension with a new id
     *
     * @param propertiesContainer the PropertiesContainer used for generating Sentences
     * @param context the context used
     */
    public WeatherTTSOutputExtension(PropertiesContainer propertiesContainer, Context context) {
        super(ID, propertiesContainer, context);
        setPluginId(TTSOutputPlugin.ID);
        addResourceIdToWishList(WeatherContentGenerator.RESOURCE_ID);
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
        List<Resource> resources = event.getListResourceContainer()
                .provideResource(WeatherContentGenerator.RESOURCE_ID);
        if(resources.isEmpty()) return null;
        Resource<WeatherChannel> resource = resources.get(0);
        StringBuilder words = new StringBuilder();
        constructMessage(words, resource.getResource());
        TTSData ttsData =  TTSData.createTTSData(words.toString(), getLocale(), 0, ID);
        return ttsData;
    }

    /**
     * constructs a Message.
     * This method will call other methods providing smaller parts.
     * @param words a StringBuilder instance
     */
    private void constructMessage(StringBuilder words, WeatherChannel weatherChannel) {
        String today = getWords(TTS_TODAY, null);
        HashMap<String, String> data = new HashMap<>();
        data.put("Day", today);
        addWeatherIntroduction(words, weatherChannel, data);
    }

    public void addWeatherIntroduction(StringBuilder words, WeatherChannel weatherChannel, HashMap<String, String> data) {
        leanderk.izou.weatheraddon.weather.Forecast forecast = weatherChannel.getForecastForToday();
        if(weatherChannel.getForecastForToday().getRating() == 3 ||
                weatherChannel.getForecastForToday().getRating() == -3) {
            words.append(getWords(TTS_OVERVIEW_Rating + forecast.getRating(), data));
            words.append(getWords(TTS_DESCRIPTION_WITHOUT_DATE + forecast.getCode(), null));
        } else {
            String overview = getWords(TTS_OVERVIEW_CODE + forecast.getCode(), data);
            words.append(overview);
        }
    }

    /**
     * checks if the TTSOutputExtension can generate TTSData fot the locale
     *
     * @param locale the locale of the request
     * @return true if able to generate, false if not
     */
    @Override
    public boolean canGenerateForLanguage(String locale) {
        //support for german and english
        if(locale.equals(new Locale("de").getLanguage())) {
            return true;
        } else if (locale.equals(new Locale("en").getLanguage())) {
            return true;
        }
        return false;
    }
}
