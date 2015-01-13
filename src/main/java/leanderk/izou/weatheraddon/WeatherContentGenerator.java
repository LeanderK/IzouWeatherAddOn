package leanderk.izou.weatheraddon;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import intellimate.izou.contentgenerator.ContentGenerator;
import intellimate.izou.events.Event;
import intellimate.izou.resource.Resource;
import intellimate.izou.system.Context;
import intellimate.izou.system.IdentificationManager;
import leanderk.izou.weatheraddon.weather.WeatherChannel;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generates the Weather-Information.
 * It uses Yahoo-Weather internally.
 */
public class WeatherContentGenerator extends ContentGenerator{
    public static final String ID = WeatherContentGenerator.class.getCanonicalName();
    public static final String RESOURCE_ID = WeatherContentGenerator.class.getCanonicalName() + ".WeatherInfo";

    private YahooWeatherService service = null;
    private final IdentificationManager identificationManager = IdentificationManager.getInstance();
    private final Context context;
    private final WOEIDFinder woeidFinder;
    private String woeid = null;

    public WeatherContentGenerator(Context context) {
        super(ID, context);
        this.context = context;
        woeidFinder = new WOEIDFinder(context);
        try {
            service = new YahooWeatherService();
        } catch (JAXBException e) {
            context.logger.getLogger().error("Error while trying to create YahooWeatherService", e);
        }
    }

    @Override
    public List<Resource> announceResources() {
        return identificationManager.getIdentification(this)
                .map(id -> new Resource<>(RESOURCE_ID, id))
                .orElse(new Resource<>(RESOURCE_ID))
                .map(Arrays::asList);
    }

    @Override
    public List<String> announceEvents() {
        return Arrays.asList(Event.FULL_WELCOME_EVENT,
                WeatherAddon.EVENT_WEATHER_FORECAST,
                WeatherAddon.EVENT_WEATHER_FULL,
                WeatherAddon.EVENT_WEATHER_TODAY);
    }

    @Override
    public List<Resource> provideResource(List<Resource> list, Optional<Event> optional) {
        if(woeid == null) woeid = woeidFinder.getWOEIDLocation();
        //maybe unable to obtain needed information's
        if(woeid == null || service == null) return null;
        Channel channel;
        try {
            channel = service.getForecast(woeid, DegreeUnit.CELSIUS);
        } catch (JAXBException | IOException e) {
            context.logger.getLogger().error("Error while trying to create YahooWeatherService", e);
            return null;
        }

        return identificationManager.getIdentification(this)
                .map(id -> new Resource<WeatherChannel>(RESOURCE_ID, id))
                .orElseThrow(() -> new RuntimeException("unable to create resource"))
                .setResource(new WeatherChannel(channel))
                .map(Arrays::asList);
    }


}