package leanderk.izou.weatheraddon.weather;

import com.github.fedy2.weather.data.Channel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents Information about the Weather.
 * This class wraps com.github.fedy2.weather.data.Channel, to make it easier to use.
 */
public class WeatherChannel{

    private com.github.fedy2.weather.data.Channel channel;

    public WeatherChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * returns the forecast for today
     * @return an instance of forecast describing the remaining day
     */
    public Forecast getForecastForToday() {
        return new Forecast(channel.getItem().getForecasts().get(0));
    }

    /**
     * returns all the forecasts for today & following days
     * @return a list of forecasts
     */
    public List<Forecast> getForecasts() {
        return channel.getItem().getForecasts().stream()
                .map(Forecast::new)
                .collect(Collectors.toList());
    }

    public Channel getChannel() {
        return channel;
    }
}
