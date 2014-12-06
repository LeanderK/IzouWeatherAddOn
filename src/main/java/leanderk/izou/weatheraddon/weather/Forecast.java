package leanderk.izou.weatheraddon.weather;

/**
 * The weather forecast for a specific day. wrapped with useful functions.
 */
public class Forecast extends com.github.fedy2.weather.data.Forecast{
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
        if(code <= 4) {
            return -3;
//        5	mixed rain and snow
//        6	mixed rain and sleet
//        7	mixed snow and sleet
//        8	freezing drizzle
        } else if(code <= 8) {
            return -2;
//        9	drizzle
        } else if(code <= 9) {
            return -1;
//        freezing rain
//        11	showers
//        12	showers
//        13	snow flurries
//        14	light snow showers
//        15	blowing snow
        } else if(code <= 15) {
            return -3;
        // 16 snow
        } else if(code <= 16) {
            return 0;
//        17	hail
//        18	sleet
        } else if(code <= 18) {
            return -3;
        //19	dust
        } else if(code <= 19) {
            return -2;
//        20	foggy
//        21	haze
        } else if(code <= 21) {
            return -2;
//        22	smoky
//        23	blustery
        } else if(code <= 23) {
            return -3;
//        24	windy
        } else if(code <= 24) {
            return 0;
        //25 cold
        } else if(code <= 25) {
            return -1;
//        26	cloudy
//        27	mostly cloudy (night)
//        28	mostly cloudy (day)
//        29	partly cloudy (night)
//        30	partly cloudy (day)
//        31	clear (night)
        } else if(code <= 31) {
            return 0;
        //32 sunny
        } else if(code <= 32) {
            return 3;
        //33 fair (night)
        } else if(code <= 33) {
            return 0;
        //34 fair (day)
        } else if(code <= 34) {
            return 2;
        //35 mixed rain and hail
        } else if(code <= 35) {
            return -3;
        //36 hot
        } else if(code <= 36) {
            return 2;
//        37	isolated thunderstorms
        } else if(code <= 37) {
            return -2;
//        38	scattered thunderstorms
//        39	scattered thunderstorms
//        40	scattered showers
        } else if(code <= 40) {
            return -1;
        //41 heavy snow
        } else if(code <= 41) {
            return -2;
        //42 scattered snow showers
        } else if(code <= 42) {
            return -1;
        //43 heavy snow
        } else if(code <= 43) {
            return -2;
        //43 heavy snow
        } else if(code <= 43) {
            return -2;
        //44  partly cloudy
        } else if(code <= 44) {
            return 1;
//            45  thundershowers
//            46	snow showers
//            47	isolated thundershowers
        } else if(code <= 44) {
            return -2;
        } else {
            return 0;
        }
    }
}
