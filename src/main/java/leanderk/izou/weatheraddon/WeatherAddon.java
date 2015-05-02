package leanderk.izou.weatheraddon;

import org.intellimate.izou.sdk.activator.Activator;
import org.intellimate.izou.sdk.addon.AddOn;
import org.intellimate.izou.sdk.contentgenerator.ContentGenerator;
import org.intellimate.izou.sdk.events.EventsController;
import org.intellimate.izou.sdk.output.OutputExtensionArgument;
import org.intellimate.izou.sdk.output.OutputPlugin;
import ro.fortsoft.pf4j.Extension;

/**
 * This AddOn fetches Weather-Data.
 * It will use the location specified with the leanderk.izou.personalinformation
 */
@SuppressWarnings("ALL")
@Extension
public class WeatherAddon extends AddOn {
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
    public OutputExtensionArgument[] registerOutputExtension() {
        OutputExtensionArgument[] outputExtensions = new OutputExtensionArgument[1];
        outputExtensions[0] = new WeatherTTSOutputExtension(getContext());
        return outputExtensions;
    }
}
