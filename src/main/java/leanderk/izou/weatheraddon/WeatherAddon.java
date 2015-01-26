package leanderk.izou.weatheraddon;

import intellimate.izou.activator.Activator;
import intellimate.izou.addon.AddOn;
import intellimate.izou.contentgenerator.ContentGenerator;
import intellimate.izou.events.EventsController;
import intellimate.izou.output.OutputExtension;
import intellimate.izou.output.OutputPlugin;
import ro.fortsoft.pf4j.Extension;

/**
 * This AddOn fetches Weather-Data.
 * It will use the location specified with the leanderk.izou.personalinformation
 */
@SuppressWarnings("ALL")
@Extension
public class WeatherAddon extends AddOn{
    public static final String ID = WeatherAddon.class.getCanonicalName();
    public static final String EVENT_WEATHER_FULL = WeatherAddon.class.getCanonicalName() + ".FullWeather";
    public static final String EVENT_WEATHER_TODAY = WeatherAddon.class.getCanonicalName() + ".FullWeather";
    public static final String EVENT_WEATHER_FORECAST = WeatherAddon.class.getCanonicalName() + ".FullWeather";

    public WeatherAddon() {
        super(ID);
    }

    @Override
    public void prepare() {
    }

    @Override
    public Activator[] registerActivator() {
        return null;
    }

    @Override
    public ContentGenerator[] registerContentGenerator() {
        ContentGenerator[] contentGenerators = new ContentGenerator[1];
        contentGenerators[0] = new WeatherContentGenerator(getContext());
        return contentGenerators;
    }

    @Override
    public EventsController[] registerEventController() {
        return null;
    }

    @Override
    public OutputPlugin[] registerOutputPlugin() {
        return null;
    }

    @Override
    public OutputExtension[] registerOutputExtension() {
        OutputExtension[] outputExtensions = new OutputExtension[1];
        outputExtensions[0] = new WeatherTTSOutputExtension(getContext());
        return outputExtensions;
    }

    @Override
    public String getID() {
        return ID;
    }
}
