import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import intellimate.izou.addon.AddOn;
import intellimate.izou.main.Main;
import intellimate.izou.resource.Resource;
import leanderk.izou.weatheraddon.WeatherContentGenerator;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Use this class to debug
 */
public class Debug {
    public static void main(String[] args) {
        LinkedList<AddOn> addOns = new LinkedList<>();
        //addOns.add(new WeatherAddon());
        YahooWeatherService service = null;
        try {
            service = new YahooWeatherService();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        Channel channel = null;
        try {
            channel = service.getForecast("2502265", DegreeUnit.CELSIUS);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(channel.toString());
        Main main = new Main(addOns, true);
        Resource resource = new Resource(WeatherContentGenerator.RESOURCE_ID);
        main.getResourceManager().generatedResource(resource,
                resourceList -> resourceList
                                .stream()
                                .map(Resource::getResource));
    }
}
