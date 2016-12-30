package xyz.garywzh.simpleweather.model;

/**
 * Created by garywzh on 2016/7/19.
 */
public class LocationInfo {

    /**
     * status : 0 message : query ok result : {"title":"海淀西大街","location":{"lng":116.306687,"lat":39.984231},"address_components":{"province":"北京市","city":"北京市","district":"海淀区","street":"海淀西大街","street_number":"74号"},"similarity":1,"deviation":304.125198,"reliability":9}
     */

    public int status;
    public String message;
    /**
     * title : 海淀西大街 location : {"lng":116.306687,"lat":39.984231} address_components :
     * {"province":"北京市","city":"北京市","district":"海淀区","street":"海淀西大街","street_number":"74号"}
     * similarity : 1 deviation : 304.125198 reliability : 9
     */

    public ResultBean result;

    public static class ResultBean {

        public String title;
        /**
         * lng : 116.306687 lat : 39.984231
         */

        public LocationBean location;
        /**
         * province : 北京市 city : 北京市 district : 海淀区 street : 海淀西大街 street_number : 74号
         */

        public AddressComponentsBean address_components;
        public double similarity;
        public double deviation;
        public int reliability;

        public static class LocationBean {

            public double lng;
            public double lat;
        }

        public static class AddressComponentsBean {

            public String province;
            public String city;
            public String district;
            public String street;
            public String street_number;
        }
    }
}
