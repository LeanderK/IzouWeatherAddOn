package leanderk.izou.weatheraddon.weather;

/**
 * The weather forecast for a specific day. wrapped with useful functions.
 */
public class Forecast{

    private com.github.fedy2.weather.data.Forecast forecast;

    public Forecast(com.github.fedy2.weather.data.Forecast forecast) {
        this.forecast = forecast;
    }

    /**
     * a rating of the weather of the day (not temperature).
     * This is a subjective rating from the developer.
     * @return an integer from -3 to 3
     */
    public int getRating() {
//        0	tornado
//        1	tropical storm
//        2	hurricane
//        3	severe thunderstorms
//        4	thunderstorms
        if(forecast.getCode() <= 4) {
            return -3;
//        5	mixed rain and snow
//        6	mixed rain and sleet
//        7	mixed snow and sleet
//        8	freezing drizzle
        } else if(forecast.getCode() <= 8) {
            return -2;
//        9	drizzle
        } else if(forecast.getCode() <= 9) {
            return -1;
//        freezing rain
//        11	showers
//        12	showers
//        13	snow flurries
//        14	light snow showers
//        15	blowing snow
        } else if(forecast.getCode() <= 15) {
            return -3;
        // 16 snow
        } else if(forecast.getCode() <= 16) {
            return 0;
//        17	hail
//        18	sleet
        } else if(forecast.getCode() <= 18) {
            return -3;
        //19	dust
        } else if(forecast.getCode() <= 19) {
            return -2;
//        20	foggy
//        21	haze
        } else if(forecast.getCode() <= 21) {
            return -2;
//        22	smoky
//        23	blustery
        } else if(forecast.getCode() <= 23) {
            return -3;
//        24	windy
        } else if(forecast.getCode() <= 24) {
            return 0;
        //25 cold
        } else if(forecast.getCode() <= 25) {
            return -1;
//        26	cloudy
//        27	mostly cloudy (night)
//        28	mostly cloudy (day)
//        29	partly cloudy (night)
//        30	partly cloudy (day)
//        31	clear (night)
        } else if(forecast.getCode() <= 31) {
            return 0;
        //32 sunny
        } else if(forecast.getCode() <= 32) {
            return 3;
        //33 fair (night)
        } else if(forecast.getCode() <= 33) {
            return 0;
        //34 fair (day)
        } else if(forecast.getCode() <= 34) {
            return 2;
        //35 mixed rain and hail
        } else if(forecast.getCode() <= 35) {
            return -3;
        //36 hot
        } else if(forecast.getCode() <= 36) {
            return 2;
//        37	isolated thunderstorms
        } else if(forecast.getCode() <= 37) {
            return -2;
//        38	scattered thunderstorms
//        39	scattered thunderstorms
//        40	scattered showers
        } else if(forecast.getCode() <= 40) {
            return -1;
        //41 heavy snow
        } else if(forecast.getCode() <= 41) {
            return -2;
        //42 scattered snow showers
        } else if(forecast.getCode() <= 42) {
            return -1;
        //43 heavy snow
        } else if(forecast.getCode() <= 43) {
            return -2;
        //43 heavy snow
        } else if(forecast.getCode() <= 43) {
            return -2;
        //44  partly cloudy
        } else if(forecast.getCode() <= 44) {
            return 1;
//            45  thundershowers
//            46	snow showers
//            47	isolated thundershowers
        } else if(forecast.getCode() <= 44) {
            return -2;
        } else {
            return 0;
        }
    }

    public com.github.fedy2.weather.data.Forecast getForecast() {
        return forecast;
    }

    public int getCode() {
        return forecast.getCode();
    }
}
