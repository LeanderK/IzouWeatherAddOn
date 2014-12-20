package leanderk.izou.weatheraddon;

import com.github.fedy2.weather.data.Condition;
import intellimate.izou.addon.PropertiesContainer;
import intellimate.izou.events.Event;
import intellimate.izou.resource.Resource;
import intellimate.izou.system.Context;
import leanderk.izou.tts.outputextension.TTSData;
import leanderk.izou.tts.outputextension.TTSOutputExtension;
import leanderk.izou.tts.outputplugin.TTSOutputPlugin;
import leanderk.izou.weatheraddon.weather.Forecast;
import leanderk.izou.weatheraddon.weather.WeatherChannel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by LeanderK on 05/12/14.
 */
public class WeatherTTSOutputExtension extends TTSOutputExtension{
    public static String ID = WeatherTTSOutputExtension.class.getCanonicalName();
    private static final String TTS_CURRENT_WEATHER = "currentWeather-";
    private static final String TTS_OVERVIEW_Rating = "overviewRating-";
    private static final String TTS_OVERVIEW_CODE = "overviewCode-";
    private static final String TTS_DESCRIPTION_WITHOUT_DATE = "withoutDateDescription-";
    private static final String TTS_TODAY = "today";
    private static final String TTS_TOMORROW = "tomorrow";
    private static final String TTS_TEMPERATURE = "temperature";
    private static final String TTS_TEMPERATURE_PART = "temperaturePart";
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
        constructMessage(words, resource.getResource(), event);
        TTSData ttsData =  TTSData.createTTSData(words.toString(), getLocale(), 0, ID);
        return ttsData;
    }

    /**
     * constructs a Message.
     * This method will call other methods providing smaller parts.
     * @param words a StringBuilder instance
     * @param event the event
     */
    private void constructMessage(StringBuilder words, WeatherChannel weatherChannel, Event event) {
        if (event.containsDescriptor(WeatherAddon.EVENT_WEATHER_FULL)
                        || (event.containsDescriptor(WeatherAddon.EVENT_WEATHER_FORECAST)
                                                && event.containsDescriptor(WeatherAddon.EVENT_WEATHER_TODAY))) {
            createCurrentWeather(words, weatherChannel);
            createWeatherForecasts(words, weatherChannel);
        } else if (event.containsDescriptor(WeatherAddon.EVENT_WEATHER_FORECAST)) {
            createWeatherForecasts(words, weatherChannel);
        } else if (event.containsDescriptor(WeatherAddon.EVENT_WEATHER_TODAY)
                        || (event.getID().equals(Event.RESPONSE)
                                                    && event.containsDescriptor(Event.FULL_WELCOME_EVENT))) {
            createCurrentWeather(words, weatherChannel);
            createWeatherIntroductionForToday(words, weatherChannel);
        } else {
            createCurrentWeather(words, weatherChannel);
            createWeatherIntroductionForToday(words, weatherChannel);
        }
    }

    public void createCurrentWeather(StringBuilder words, WeatherChannel weatherChannel) {
        Condition condition = weatherChannel.getChannel().getItem().getCondition();
        String unit = weatherChannel.getChannel().getUnits().getTemperature().name();
        HashMap<String, String> data = new HashMap<>();
        data.put("Temp", String.valueOf(condition.getTemp()));
        data.put("Unit", unit);
        words.append(getWords(TTS_CURRENT_WEATHER + condition.getCode(), data));
    }

    public void createWeatherIntroductionForToday(StringBuilder words, WeatherChannel weatherChannel) {
        String today = getWords(TTS_TODAY, null);
        HashMap<String, String> dataToday = new HashMap<>();
        dataToday.put("Day", today);
        createWeatherForecast(words, weatherChannel, weatherChannel.getForecastForToday(), dataToday);
    }

    public void createWeatherForecasts(StringBuilder words, WeatherChannel weatherChannel) {
        List<com.github.fedy2.weather.data.Forecast> forecasts = weatherChannel.getChannel().getItem().getForecasts();
        LocalDate todayDate = LocalDate.now();

        for (com.github.fedy2.weather.data.Forecast forecast : forecasts) {
            LocalDate forecastDate = forecast.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if(todayDate.isEqual(forecastDate)) {
                String today = getWords(TTS_TODAY, null);
                HashMap<String, String> data = new HashMap<>();
                data.put("Day", today);
                createWeatherForecast(words, weatherChannel, new Forecast(forecast), data);
            } else if(todayDate.plusDays(1).isEqual(forecastDate)) {
                String tomorrow = getWords(TTS_TOMORROW, null);
                HashMap<String, String> data = new HashMap<>();
                data.put("Day", tomorrow);
                createWeatherForecast(words, weatherChannel, new Forecast(forecast), data);
            } else {
                String day = forecastDate.getDayOfWeek().toString().toLowerCase();
                HashMap<String, String> data = new HashMap<>();
                data.put("Day", day);
                createWeatherForecast(words, weatherChannel, new Forecast(forecast), data);
            }
        }
    }

    public void createWeatherForecast(StringBuilder words, WeatherChannel channel,
                                      Forecast forecast, HashMap<String, String> data) {
        String high = String.valueOf(forecast.getForecast().getHigh());
        String low = String.valueOf(forecast.getForecast().getLow());
        String unit = channel.getChannel().getUnits().getTemperature().name();
        data.put("High", high);
        data.put("Low", low);
        data.put("Unit", getWords(unit, null));
        if(forecast.getRating() == 3 || forecast.getRating() <= -2) {
            words.append(getWords(TTS_OVERVIEW_Rating + forecast.getRating(), data));
            words.append(" ");
            String description = getWords(TTS_DESCRIPTION_WITHOUT_DATE + forecast.getCode(), null);
            if (!description.isEmpty() && (description.lastIndexOf(".") == (description.length() - 1))) {
                description = description.substring(0, description.length() -1);
            }
            words.append(description);
            words.append(" ");
            words.append(getWords(TTS_TEMPERATURE_PART, data));
        } else {
            words.append(getWords(TTS_OVERVIEW_CODE + forecast.getCode(), data));
            words.append(" ");
            words.append(getWords(TTS_TEMPERATURE, data));
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
