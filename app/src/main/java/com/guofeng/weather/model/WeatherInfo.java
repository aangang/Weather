package com.guofeng.weather.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GUOFENG on 2016/10/12.
 * 天气信息 Beans 类
 */

public class WeatherInfo implements Serializable {


    /**
     * basic : {"city":"大连","cnty":"中国","id":"CN101070201","lat":"38.944000","lon":"121.576000","update":{"loc":"2015-07-15 10:43","utc":"2015-07-15 02:46:14"}}
     * status : ok
     * aqi : {"city":{"aqi":"71","co":"1","no2":"75","o3":"101","pm10":"89","pm25":"44","qlty":"良","so2":"27"}}
     * alarms : [{"level":"橙色","stat":"预警中","title":"辽宁省大连市气象台发布高温橙色预警","txt":"大连市气象台2015年07月14日13时31分发布高温橙色预警信号:预计14日下午至傍晚，旅顺口区局部最高气温将达到37℃以上,请注意防范。 ","type":"高温"}]
     * now : {"cond":{"code":"100","txt":"晴"},"fl":"33","hum":"28","pcpn":"0","pres":"1005","tmp":"32","vis":"10","wind":{"deg":"350","dir":"东北风","sc":"4-5","spd":"11"}}
     * daily_forecast : [{"date":"2015-07-15","astro":{"sr":"04:40","ss":"19:19"},"cond":{"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"},"hum":"48","pcpn":"0.0","pop":"0","pres":"1005","tmp":{"max":"33","min":"24"},"vis":"10","wind":{"deg":"192","dir":"东南风","sc":"4-5","spd":"11"}},{"date":"2015-07-16","astro":{"sr":"04:40","ss":"19:18"},"cond":{"code_d":"104","code_n":"104","txt_d":"阴","txt_n":"阴"},"hum":"82","pcpn":"2.7","pop":"82","pres":"1008","tmp":{"max":"27","min":"23"},"vis":"10","wind":{"deg":"116","dir":"东南风","sc":"4-5","spd":"11"}},{"date":"2015-07-17","astro":{"sr":"04:41","ss":"19:17"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"hum":"70","pcpn":"0.1","pop":"11","pres":"1006","tmp":{"max":"28","min":"22"},"vis":"10","wind":{"deg":"172","dir":"西风","sc":"4-5","spd":"11"}}]
     * hourly_forecast : [{"date":"2015-07-15 10:00","hum":"51","pop":"0","pres":"1006","tmp":"32","wind":{"deg":"127","dir":"东南风","sc":"微风","spd":"4"}},{"date":"2015-07-15 13:00","hum":"49","pop":"0","pres":"1005","tmp":"34","wind":{"deg":"179","dir":"南风","sc":"微风","spd":"7"}},{"date":"2015-07-15 16:00","hum":"54","pop":"0","pres":"1005","tmp":"31","wind":{"deg":"216","dir":"西南风","sc":"微风","spd":"6"}},{"date":"2015-07-15 19:00","hum":"62","pop":"0","pres":"1005","tmp":"29","wind":{"deg":"192","dir":"西南风","sc":"微风","spd":"4"}},{"date":"2015-07-15 22:00","hum":"62","pop":"0","pres":"1006","tmp":"26","wind":{"deg":"154","dir":"东南风","sc":"微风","spd":"10"}}]
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"},"cw":{"brf":"较不宜","txt":"较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"},"drsg":{"brf":"炎热","txt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"},"flu":{"brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"},"sport":{"brf":"较适宜","txt":"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。"},"trav":{"brf":"适宜","txt":"天气较好，是个好天气哦。稍热但是风大，能缓解炎热的感觉，适宜旅游，可不要错过机会呦！"},"uv":{"brf":"强","txt":"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"}}
     */

    private List<HeWeatherdataserviceBean> HeWeatherdataservice;

    public List<HeWeatherdataserviceBean> getHeWeatherdataservice() {
        return HeWeatherdataservice;
    }

    public void setHeWeatherdataservice(List<HeWeatherdataserviceBean> HeWeatherdataservice) {
        this.HeWeatherdataservice = HeWeatherdataservice;
    }

    public static class HeWeatherdataserviceBean implements Serializable{
        /**
         * city : 大连
         * cnty : 中国
         * id : CN101070201
         * lat : 38.944000
         * lon : 121.576000
         * update : {"loc":"2015-07-15 10:43","utc":"2015-07-15 02:46:14"}
         */

        private BasicBean basic;
        private String status;
        /**
         * city : {"aqi":"71","co":"1","no2":"75","o3":"101","pm10":"89","pm25":"44","qlty":"良","so2":"27"}
         */

        private AqiBean aqi;
        /**
         * cond : {"code":"100","txt":"晴"}
         * fl : 33
         * hum : 28
         * pcpn : 0
         * pres : 1005
         * tmp : 32
         * vis : 10
         * wind : {"deg":"350","dir":"东北风","sc":"4-5","spd":"11"}
         */

        private NowBean now;
        /**
         * comf : {"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"}
         * cw : {"brf":"较不宜","txt":"较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"}
         * drsg : {"brf":"炎热","txt":"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。"}
         * flu : {"brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"}
         * sport : {"brf":"较适宜","txt":"天气较好，但风力较大，推荐您进行室内运动，若在户外运动请注意防风。"}
         * trav : {"brf":"适宜","txt":"天气较好，是个好天气哦。稍热但是风大，能缓解炎热的感觉，适宜旅游，可不要错过机会呦！"}
         * uv : {"brf":"强","txt":"紫外线辐射强，建议涂擦SPF20左右、PA++的防晒护肤品。避免在10点至14点暴露于日光下。"}
         */

        private SuggestionBean suggestion;
        /**
         * level : 橙色
         * stat : 预警中
         * title : 辽宁省大连市气象台发布高温橙色预警
         * txt : 大连市气象台2015年07月14日13时31分发布高温橙色预警信号:预计14日下午至傍晚，旅顺口区局部最高气温将达到37℃以上,请注意防范。
         * type : 高温
         */

        private List<AlarmsBean> alarms;
        /**
         * date : 2015-07-15
         * astro : {"sr":"04:40","ss":"19:19"}
         * cond : {"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"}
         * hum : 48
         * pcpn : 0.0
         * pop : 0
         * pres : 1005
         * tmp : {"max":"33","min":"24"}
         * vis : 10
         * wind : {"deg":"192","dir":"东南风","sc":"4-5","spd":"11"}
         */

        private List<DailyForecastBean> daily_forecast;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public AqiBean getAqi() {
            return aqi;
        }

        public void setAqi(AqiBean aqi) {
            this.aqi = aqi;
        }

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public SuggestionBean getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(SuggestionBean suggestion) {
            this.suggestion = suggestion;
        }

        public List<AlarmsBean> getAlarms() {
            return alarms;
        }

        public void setAlarms(List<AlarmsBean> alarms) {
            this.alarms = alarms;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public static class BasicBean implements Serializable{
            private String city;
            private String cnty;
            private String id;
            /**
             * loc : 2015-07-15 10:43
             * utc : 2015-07-15 02:46:14
             */

            private UpdateBean update;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public UpdateBean getUpdate() {
                return update;
            }

            public void setUpdate(UpdateBean update) {
                this.update = update;
            }

            public static class UpdateBean implements Serializable{
                private String loc;

                public String getLoc() {
                    return loc;
                }

                public void setLoc(String loc) {
                    this.loc = loc;
                }
            }
        }

        public static class AqiBean implements Serializable {
            /**
             * aqi : 71
             * co : 1
             * no2 : 75
             * o3 : 101
             * pm10 : 89
             * pm25 : 44
             * qlty : 良
             * so2 : 27
             */

            private CityBean city;

            public CityBean getCity() {
                return city;
            }

            public void setCity(CityBean city) {
                this.city = city;
            }

            public static class CityBean implements Serializable{
                private String aqi;
                private String pm25;
                private String qlty;

                public String getAqi() {
                    return aqi;
                }

                public void setAqi(String aqi) {
                    this.aqi = aqi;
                }

                public String getPm25() {
                    return pm25;
                }

                public void setPm25(String pm25) {
                    this.pm25 = pm25;
                }

                public String getQlty() {
                    return qlty;
                }

                public void setQlty(String qlty) {
                    this.qlty = qlty;
                }
            }
        }

        public static class NowBean implements Serializable {
            /**
             * code : 100
             * txt : 晴
             */

            private CondBean cond;
            private String fl;
            private String pcpn;
            private String tmp;
            /**
             * deg : 350
             * dir : 东北风
             * sc : 4-5
             * spd : 11
             */

            private WindBean wind;

            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public static class CondBean implements Serializable{
                private String code;
                private String txt;

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class WindBean implements Serializable{
                private String dir;
                private String sc;

                public String getDir() {
                    return dir;
                }

                public void setDir(String dir) {
                    this.dir = dir;
                }

                public String getSc() {
                    return sc;
                }

                public void setSc(String sc) {
                    this.sc = sc;
                }
            }
        }

        public static class SuggestionBean implements Serializable{
            /**
             * brf : 较舒适
             * txt : 白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。
             */

            private ComfBean comf;
            /**
             * brf : 炎热
             * txt : 天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。
             */

            private DrsgBean drsg;
            /**
             * brf : 适宜
             * txt : 天气较好，是个好天气哦。稍热但是风大，能缓解炎热的感觉，适宜旅游，可不要错过机会呦！
             */

            private TravBean trav;

            public ComfBean getComf() {
                return comf;
            }

            public void setComf(ComfBean comf) {
                this.comf = comf;
            }

            public DrsgBean getDrsg() {
                return drsg;
            }

            public void setDrsg(DrsgBean drsg) {
                this.drsg = drsg;
            }

            public TravBean getTrav() {
                return trav;
            }

            public void setTrav(TravBean trav) {
                this.trav = trav;
            }

            public static class ComfBean implements Serializable{
                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class DrsgBean implements Serializable{
                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }

            public static class TravBean implements Serializable{
                private String brf;
                private String txt;

                public String getBrf() {
                    return brf;
                }

                public void setBrf(String brf) {
                    this.brf = brf;
                }

                public String getTxt() {
                    return txt;
                }

                public void setTxt(String txt) {
                    this.txt = txt;
                }
            }
        }

        public static class AlarmsBean implements Serializable{
            private String level;
            private String stat;
            private String title;
            private String txt;
            private String type;

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getStat() {
                return stat;
            }

            public void setStat(String stat) {
                this.stat = stat;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class DailyForecastBean implements Serializable{
            private String date;
            /**
             * code_d : 100
             * code_n : 101
             * txt_d : 晴
             * txt_n : 多云
             */

            private CondBean cond;
            /**
             * max : 33
             * min : 24
             */

            private TmpBean tmp;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public CondBean getCond() {
                return cond;
            }

            public void setCond(CondBean cond) {
                this.cond = cond;
            }

            public TmpBean getTmp() {
                return tmp;
            }

            public void setTmp(TmpBean tmp) {
                this.tmp = tmp;
            }

            public static class CondBean implements Serializable{
                private String code_d;
                private String code_n;
                private String txt_d;
                private String txt_n;

                public String getCode_d() {
                    return code_d;
                }

                public void setCode_d(String code_d) {
                    this.code_d = code_d;
                }

                public String getCode_n() {
                    return code_n;
                }

                public void setCode_n(String code_n) {
                    this.code_n = code_n;
                }

                public String getTxt_d() {
                    return txt_d;
                }

                public void setTxt_d(String txt_d) {
                    this.txt_d = txt_d;
                }

                public String getTxt_n() {
                    return txt_n;
                }

                public void setTxt_n(String txt_n) {
                    this.txt_n = txt_n;
                }
            }

            public static class TmpBean implements Serializable{
                private String max;
                private String min;

                public String getMax() {
                    return max;
                }

                public void setMax(String max) {
                    this.max = max;
                }

                public String getMin() {
                    return min;
                }

                public void setMin(String min) {
                    this.min = min;
                }
            }
        }
    }
}
