import intellimate.izou.addon.AddOn;
import intellimate.izou.main.Main;
import leanderk.izou.weatheraddon.WeatherAddon;

import java.util.LinkedList;

/**
 * Use this class to debug
 */
public class Debug {
    public static void main(String[] args) {
        LinkedList<AddOn> addOns = new LinkedList<>();
        addOns.add(new WeatherAddon());
        /*
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
        System.out.println(channel.toString());*/
        Main main = new Main(addOns);
        /*
        Resource resource = new Resource(WeatherContentGenerator.RESOURCE_ID);
        main.getResourceManager().generatedResource(resource,
                resourceList -> resourceList
                                .stream()
                                .map(Resource::getResource));
                                */
    }
}
