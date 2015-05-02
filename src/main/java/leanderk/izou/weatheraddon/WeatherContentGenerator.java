package leanderk.izou.weatheraddon;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import leanderk.izou.weatheraddon.weather.WeatherChannel;
import org.intellimate.izou.events.EventModel;
import org.intellimate.izou.identification.IdentificationManager;
import org.intellimate.izou.resource.ResourceModel;
import org.intellimate.izou.sdk.Context;
import org.intellimate.izou.sdk.contentgenerator.ContentGenerator;
import org.intellimate.izou.sdk.contentgenerator.EventListener;
import org.intellimate.izou.sdk.events.CommonEvents;
import org.intellimate.izou.sdk.resource.Resource;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generates the Weather-Information.
 * It uses Yahoo-Weather internally.
 */
public class WeatherContentGenerator extends ContentGenerator {
    public static final String ID = WeatherContentGenerator.class.getCanonicalName();
    public static final String RESOURCE_ID = WeatherContentGenerator.class.getCanonicalName() + ".WeatherInfo";

    private YahooWeatherService service = null;
    private final WOEIDFinder woeidFinder;
    private String woeid = null;

    public WeatherContentGenerator(Context context) {
        super(ID, context);
        woeidFinder = new WOEIDFinder(context);
        try {
            service = new YahooWeatherService();
        } catch (JAXBException e) {
            error("Error while trying to create YahooWeatherService", e);
        }
    }

    @Override
    public List<? extends EventListener> getTriggeredEvents() {
        List<EventListener> eventListeners = new ArrayList<>();
        CommonEvents.Response.fullResponseListener(this).ifPresent(eventListeners::add);
        EventListener.createEventListener(
                WeatherAddon.EVENT_WEATHER_FORECAST,
                "fetches the weather forecast for the next couple dates",
                "weather_forecast",
                this
        ).ifPresent(eventListeners::add);
        EventListener.createEventListener(
                WeatherAddon.EVENT_WEATHER_FULL,
                "fetches the weather-data for today and the next couple dates",
                "weather_full",
                this
        ).ifPresent(eventListeners::add);
        EventListener.createEventListener(
                WeatherAddon.EVENT_WEATHER_TODAY,
                "fetches the weather for today",
                "weather_today",
                this
        ).ifPresent(eventListeners::add);
        return eventListeners;
    }

    /**
     * This method is called to register what resources the object provides.
     * just pass a List of Resources without Data in it.
     *
     * @return a List containing the resources the object provides
     */
    @Override
    public List<? extends Resource> getTriggeredResources() {
        return optionalToList(createResource(RESOURCE_ID));
    }

    @Override
    public List<? extends Resource> triggered(List<? extends ResourceModel> list, Optional<EventModel> optional) {
        if(woeid == null) woeid = woeidFinder.getWOEIDLocation();
        //maybe unable to obtain needed information's
        if(woeid == null || service == null) return null;
        Channel channel;
        try {
            channel = service.getForecast(woeid, DegreeUnit.CELSIUS);
        } catch (JAXBException | IOException e) {
            error("Error while trying to create YahooWeatherService", e);
            return null;
        }
        return optionalToList(createResource(RESOURCE_ID, new WeatherChannel(channel)));
    }


}
