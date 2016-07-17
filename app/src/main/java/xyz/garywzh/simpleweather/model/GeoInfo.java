package xyz.garywzh.simpleweather.model;

/**
 * Created by garywzh on 2016/7/15.
 */
public class GeoInfo {

    /**
     * status : 0
     * message : query ok
     * request_id : 6159592582202107272
     * result : {"location":{"lat":40,"lng":116.36},"address":"北京市海淀区清华东路","formatted_addresses":{"recommend":"海淀区石油大院内学院路20号院西北450米","rough":"海淀区石油大院内"},"address_component":{"nation":"中国","province":"北京市","city":"北京市","district":"海淀区","street":"清华东路","street_number":""},"ad_info":{"adcode":"110108","name":"中国,北京市,北京市,海淀区","location":{"lat":40,"lng":116.360001},"nation":"中国","province":"北京市","city":"北京市","district":"海淀区"},"address_reference":{"crossroad":{"title":"清华东路/志新西路(路口)","location":{"lat":40.001339,"lng":116.364731},"_distance":424.5,"_dir_desc":"西"},"street":{"title":"清华东路","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"},"famous_area":{"title":"学院路","location":{"lat":39.995811,"lng":116.363731},"_distance":0,"_dir_desc":"内"},"street_number":{"title":"","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"},"town":{"title":"学院路街道","location":{"lat":40,"lng":116.360001},"_distance":0,"_dir_desc":"内"},"landmark_l2":{"title":"学院路20号院(东区)","location":{"lat":39.997028,"lng":116.364014},"_distance":476,"_dir_desc":"西北"}}}
     */

    public int status;
    public String message;
    public String request_id;
    /**
     * location : {"lat":40,"lng":116.36}
     * address : 北京市海淀区清华东路
     * formatted_addresses : {"recommend":"海淀区石油大院内学院路20号院西北450米","rough":"海淀区石油大院内"}
     * address_component : {"nation":"中国","province":"北京市","city":"北京市","district":"海淀区","street":"清华东路","street_number":""}
     * ad_info : {"adcode":"110108","name":"中国,北京市,北京市,海淀区","location":{"lat":40,"lng":116.360001},"nation":"中国","province":"北京市","city":"北京市","district":"海淀区"}
     * address_reference : {"crossroad":{"title":"清华东路/志新西路(路口)","location":{"lat":40.001339,"lng":116.364731},"_distance":424.5,"_dir_desc":"西"},"street":{"title":"清华东路","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"},"famous_area":{"title":"学院路","location":{"lat":39.995811,"lng":116.363731},"_distance":0,"_dir_desc":"内"},"street_number":{"title":"","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"},"town":{"title":"学院路街道","location":{"lat":40,"lng":116.360001},"_distance":0,"_dir_desc":"内"},"landmark_l2":{"title":"学院路20号院(东区)","location":{"lat":39.997028,"lng":116.364014},"_distance":476,"_dir_desc":"西北"}}
     */

    public ResultBean result;

    public static class ResultBean {
        /**
         * lat : 40
         * lng : 116.36
         */

        public LocationBean location;
        public String address;
        /**
         * recommend : 海淀区石油大院内学院路20号院西北450米
         * rough : 海淀区石油大院内
         */

        public FormattedAddressesBean formatted_addresses;
        /**
         * nation : 中国
         * province : 北京市
         * city : 北京市
         * district : 海淀区
         * street : 清华东路
         * street_number :
         */

        public AddressComponentBean address_component;
        /**
         * adcode : 110108
         * name : 中国,北京市,北京市,海淀区
         * location : {"lat":40,"lng":116.360001}
         * nation : 中国
         * province : 北京市
         * city : 北京市
         * district : 海淀区
         */

        public AdInfoBean ad_info;
        /**
         * crossroad : {"title":"清华东路/志新西路(路口)","location":{"lat":40.001339,"lng":116.364731},"_distance":424.5,"_dir_desc":"西"}
         * street : {"title":"清华东路","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"}
         * famous_area : {"title":"学院路","location":{"lat":39.995811,"lng":116.363731},"_distance":0,"_dir_desc":"内"}
         * street_number : {"title":"","location":{"lat":40.001167,"lng":116.359962},"_distance":124.2,"_dir_desc":"南"}
         * town : {"title":"学院路街道","location":{"lat":40,"lng":116.360001},"_distance":0,"_dir_desc":"内"}
         * landmark_l2 : {"title":"学院路20号院(东区)","location":{"lat":39.997028,"lng":116.364014},"_distance":476,"_dir_desc":"西北"}
         */

        public AddressReferenceBean address_reference;

        public static class LocationBean {
            public double lat;
            public double lng;
        }

        public static class FormattedAddressesBean {
            public String recommend;
            public String rough;
        }

        public static class AddressComponentBean {
            public String nation;
            public String province;
            public String city;
            public String district;
            public String street;
            public String street_number;
        }

        public static class AdInfoBean {
            public String adcode;
            public String name;
            /**
             * lat : 40
             * lng : 116.360001
             */

            public LocationBean location;
            public String nation;
            public String province;
            public String city;
            public String district;

            public static class LocationBean {
                public double lat;
                public double lng;
            }
        }

        public static class AddressReferenceBean {
            /**
             * title : 清华东路/志新西路(路口)
             * location : {"lat":40.001339,"lng":116.364731}
             * _distance : 424.5
             * _dir_desc : 西
             */

            public CrossroadBean crossroad;
            /**
             * title : 清华东路
             * location : {"lat":40.001167,"lng":116.359962}
             * _distance : 124.2
             * _dir_desc : 南
             */

            public StreetBean street;
            /**
             * title : 学院路
             * location : {"lat":39.995811,"lng":116.363731}
             * _distance : 0
             * _dir_desc : 内
             */

            public FamousAreaBean famous_area;
            /**
             * title :
             * location : {"lat":40.001167,"lng":116.359962}
             * _distance : 124.2
             * _dir_desc : 南
             */

            public StreetNumberBean street_number;
            /**
             * title : 学院路街道
             * location : {"lat":40,"lng":116.360001}
             * _distance : 0
             * _dir_desc : 内
             */

            public TownBean town;
            /**
             * title : 学院路20号院(东区)
             * location : {"lat":39.997028,"lng":116.364014}
             * _distance : 476
             * _dir_desc : 西北
             */

            public LandmarkL2Bean landmark_l2;

            public static class CrossroadBean {
                public String title;
                /**
                 * lat : 40.001339
                 * lng : 116.364731
                 */

                public LocationBean location;
                public double _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }

            public static class StreetBean {
                public String title;
                /**
                 * lat : 40.001167
                 * lng : 116.359962
                 */

                public LocationBean location;
                public double _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }

            public static class FamousAreaBean {
                public String title;
                /**
                 * lat : 39.995811
                 * lng : 116.363731
                 */

                public LocationBean location;
                public int _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }

            public static class StreetNumberBean {
                public String title;
                /**
                 * lat : 40.001167
                 * lng : 116.359962
                 */

                public LocationBean location;
                public double _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }

            public static class TownBean {
                public String title;
                /**
                 * lat : 40
                 * lng : 116.360001
                 */

                public LocationBean location;
                public int _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }

            public static class LandmarkL2Bean {
                public String title;
                /**
                 * lat : 39.997028
                 * lng : 116.364014
                 */

                public LocationBean location;
                public int _distance;
                public String _dir_desc;

                public static class LocationBean {
                    public double lat;
                    public double lng;
                }
            }
        }
    }
}
