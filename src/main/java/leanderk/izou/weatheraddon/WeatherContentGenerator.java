package leanderk.izou.weatheraddon;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import intellimate.izou.contentgenerator.ContentGenerator;
import intellimate.izou.events.Event;
import intellimate.izou.resource.Resource;
import intellimate.izou.system.Context;
import intellimate.izou.system.Identification;
import intellimate.izou.system.IdentificationManager;

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

    YahooWeatherService service = null;
    private IdentificationManager identificationManager = IdentificationManager.getInstance();
    private Context context;
    private WOEIDFinder woeidFinder;
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
        Optional<Identification> identification = identificationManager.getIdentification(this);
        Resource<String> resource = new Resource<>(RESOURCE_ID);
        identification.ifPresent(resource::setProvider);
        return Arrays.asList(resource);
    }

    @Override
    public List<String> announceEvents() {
        return Arrays.asList(Event.FULL_WELCOME_EVENT);
    }

    @Override
    public List<Resource> provideResource(List<Resource> list, Optional<Event> optional) {
        if(woeid == null) woeid = woeidFinder.getWOEIDLocation();
        //maybe unable to obtain needed information's
        if(woeid == null || service == null) return null;
        Channel channel = null;
        try {
            channel = service.getForecast("2502265", DegreeUnit.CELSIUS);
        } catch (JAXBException | IOException e) {
            context.logger.getLogger().error("Error while trying to create YahooWeatherService", e);
        }
        System.out.println(channel.getDescription());
        return null;
    }
}
