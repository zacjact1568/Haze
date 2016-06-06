package com.zack.enderweather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/** 和风天气bean，与服务器返回的JSON数据对应 */
public class HeWeather {

    @SerializedName("HeWeather data service 3.0")
    private List<HeWeatherAPI> heWeatherAPIList;

    public List<HeWeatherAPI> getHeWeatherAPIList() {
        return heWeatherAPIList;
    }

    public void setHeWeatherAPIList(List<HeWeatherAPI> heWeatherAPIList) {
        this.heWeatherAPIList = heWeatherAPIList;
    }

    public static class HeWeatherAPI {

        @SerializedName("aqi")
        private Aqi aqi;
        @SerializedName("basic")
        private Basic basic;
        @SerializedName("now")
        private Now now;
        @SerializedName("status")
        private String status;
        @SerializedName("suggestion")
        private Suggestion suggestion;
        @SerializedName("daily_forecast")
        private List<DailyForecast> dailyForecastList;
        @SerializedName("hourly_forecast")
        private List<HourlyForecast> hourlyForecastList;

        public Aqi getAqi() {
            return aqi;
        }

        public void setAqi(Aqi aqi) {
            this.aqi = aqi;
        }

        public Basic getBasic() {
            return basic;
        }

        public void setBasic(Basic basic) {
            this.basic = basic;
        }

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Suggestion getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(Suggestion suggestion) {
            this.suggestion = suggestion;
        }

        public List<DailyForecast> getDailyForecastList() {
            return dailyForecastList;
        }

        public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
            this.dailyForecastList = dailyForecastList;
        }

        public List<HourlyForecast> getHourlyForecastList() {
            return hourlyForecastList;
        }

        public void setHourlyForecastList(List<HourlyForecast> hourlyForecastList) {
            this.hourlyForecastList = hourlyForecastList;
        }

        /**
         * 空气质量指数<br>
         * city : {"aqi":"65","co":"1","no2":"22","o3":"224","pm10":"77","pm25":"29","qlty":"轻度污染","so2":"11"}
         */
        public static class Aqi {

            @SerializedName("city")
            private City city;

            public City getCity() {
                return city;
            }

            public void setCity(City city) {
                this.city = city;
            }

            /**
             * 城市数据<br>
             * aqi : 65
             * co : 1
             * no2 : 22
             * o3 : 224
             * pm10 : 77
             * pm25 : 29
             * qlty : 轻度污染
             * so2 : 11
             */
            public static class City {

                @SerializedName("aqi")
                private String aqi;
                @SerializedName("co")
                private String co;
                @SerializedName("no2")
                private String no2;
                @SerializedName("o3")
                private String o3;
                @SerializedName("pm10")
                private String pm10;
                @SerializedName("pm25")
                private String pm25;
                @SerializedName("qlty")
                private String qlty;
                @SerializedName("so2")
                private String so2;

                public String getAqi() {
                    return aqi;
                }

                public void setAqi(String aqi) {
                    this.aqi = aqi;
                }

                public String getCo() {
                    return co;
                }

                public void setCo(String co) {
                    this.co = co;
                }

                public String getNo2() {
                    return no2;
                }

                public void setNo2(String no2) {
                    this.no2 = no2;
                }

                public String getO3() {
                    return o3;
                }

                public void setO3(String o3) {
                    this.o3 = o3;
                }

                public String getPm10() {
                    return pm10;
                }

                public void setPm10(String pm10) {
                    this.pm10 = pm10;
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

                public String getSo2() {
                    return so2;
                }

                public void setSo2(String so2) {
                    this.so2 = so2;
                }
            }
        }

        /**
         * 城市基本信息<br>
         * city : 德阳
         * cnty : 中国
         * id : CN101272001
         * lat : 31.147000
         * lon : 104.375000
         * update : {"loc":"2016-05-17 15:52","utc":"2016-05-17 07:52"}
         */
        public static class Basic {

            @SerializedName("city")
            private String city;
            @SerializedName("cnty")
            private String cnty;
            @SerializedName("id")
            private String id;
            @SerializedName("lat")
            private String lat;
            @SerializedName("lon")
            private String lon;
            @SerializedName("update")
            private Update update;

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

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public Update getUpdate() {
                return update;
            }

            public void setUpdate(Update update) {
                this.update = update;
            }

            /**
             * 数据更新时间，24小时制<br>
             * loc : 2016-05-17 15:52
             * utc : 2016-05-17 07:52
             */
            public static class Update {

                @SerializedName("loc")
                private String loc;
                @SerializedName("utc")
                private String utc;

                public String getLoc() {
                    return loc;
                }

                public void setLoc(String loc) {
                    this.loc = loc;
                }

                public String getUtc() {
                    return utc;
                }

                public void setUtc(String utc) {
                    this.utc = utc;
                }
            }
        }

        /**
         * 实况天气<br>
         * cond : {"code":"104","txt":"阴"}
         * fl : 26
         * hum : 38
         * pcpn : 0
         * pres : 1007
         * tmp : 27
         * vis : 10
         * wind : {"deg":"103","dir":"东风","sc":"4-5","spd":"21"}
         */
        public static class Now {

            @SerializedName("cond")
            private Cond cond;
            @SerializedName("fl")
            private String fl;
            @SerializedName("hum")
            private String hum;
            @SerializedName("pcpn")
            private String pcpn;
            @SerializedName("pres")
            private String pres;
            @SerializedName("tmp")
            private String tmp;
            @SerializedName("vis")
            private String vis;
            @SerializedName("wind")
            private Wind wind;

            public Cond getCond() {
                return cond;
            }

            public void setCond(Cond cond) {
                this.cond = cond;
            }

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public Wind getWind() {
                return wind;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            /**
             * 天气状况<br>
             * code : 104
             * txt : 阴
             */
            public static class Cond {

                @SerializedName("code")
                private String code;
                @SerializedName("txt")
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

            /**
             * 风力状况<br>
             * deg : 103
             * dir : 东风
             * sc : 4-5
             * spd : 21
             */
            public static class Wind {

                @SerializedName("deg")
                private String deg;
                @SerializedName("dir")
                private String dir;
                @SerializedName("sc")
                private String sc;
                @SerializedName("spd")
                private String spd;

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

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

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }

        /**
         * 生活指数<br>
         * comf : {"brf":"较舒适","txt":"白天以阴或多云天气为主，但稍会让您感到有点儿热，但大部分人完全可以接受。"}
         * cw : {"brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"}
         * drsg : {"brf":"舒适","txt":"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。"}
         * flu : {"brf":"少发","txt":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。"}
         * sport : {"brf":"较适宜","txt":"阴天，较适宜进行各种户内外运动。"}
         * trav : {"brf":"适宜","txt":"天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。"}
         * uv : {"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
         */
        public static class Suggestion {

            @SerializedName("comf")
            private Comf comf;
            @SerializedName("cw")
            private Cw cw;
            @SerializedName("drsg")
            private Drsg drsg;
            @SerializedName("flu")
            private Flu flu;
            @SerializedName("sport")
            private Sport sport;
            @SerializedName("trav")
            private Trav trav;
            @SerializedName("uv")
            private Uv uv;

            public Comf getComf() {
                return comf;
            }

            public void setComf(Comf comf) {
                this.comf = comf;
            }

            public Cw getCw() {
                return cw;
            }

            public void setCw(Cw cw) {
                this.cw = cw;
            }

            public Drsg getDrsg() {
                return drsg;
            }

            public void setDrsg(Drsg drsg) {
                this.drsg = drsg;
            }

            public Flu getFlu() {
                return flu;
            }

            public void setFlu(Flu flu) {
                this.flu = flu;
            }

            public Sport getSport() {
                return sport;
            }

            public void setSport(Sport sport) {
                this.sport = sport;
            }

            public Trav getTrav() {
                return trav;
            }

            public void setTrav(Trav trav) {
                this.trav = trav;
            }

            public Uv getUv() {
                return uv;
            }

            public void setUv(Uv uv) {
                this.uv = uv;
            }

            /**
             * brf : 较舒适
             * txt : 白天以阴或多云天气为主，但稍会让您感到有点儿热，但大部分人完全可以接受。
             */
            public static class Comf {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 较适宜
             * txt : 较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。
             */
            public static class Cw {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 舒适
             * txt : 建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。
             */
            public static class Drsg {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 少发
             * txt : 各项气象条件适宜，无明显降温过程，发生感冒机率较低。
             */
            public static class Flu {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 较适宜
             * txt : 阴天，较适宜进行各种户内外运动。
             */
            public static class Sport {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 适宜
             * txt : 天气较好，温度适宜，总体来说还是好天气哦，这样的天气适宜旅游，您可以尽情地享受大自然的风光。
             */
            public static class Trav {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

            /**
             * brf : 最弱
             * txt : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
             */
            public static class Uv {

                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
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

        /**
         * 天气预报<br>
         * astro : {"sr":"06:05","ss":"19:52"}
         * cond : {"code_d":"104","code_n":"104","txt_d":"阴","txt_n":"阴"}
         * date : 2016-05-17
         * hum : 38
         * pcpn : 0.0
         * pop : 4
         * pres : 1007
         * tmp : {"max":"27","min":"16"}
         * vis : 10
         * wind : {"deg":"108","dir":"无持续风向","sc":"微风","spd":"1"}
         */
        public static class DailyForecast {

            @SerializedName("astro")
            private Astro astro;
            @SerializedName("cond")
            private Cond cond;
            @SerializedName("date")
            private String date;
            @SerializedName("hum")
            private String hum;
            @SerializedName("pcpn")
            private String pcpn;
            @SerializedName("pop")
            private String pop;
            @SerializedName("pres")
            private String pres;
            @SerializedName("tmp")
            private Tmp tmp;
            @SerializedName("vis")
            private String vis;
            @SerializedName("wind")
            private Wind wind;

            public Astro getAstro() {
                return astro;
            }

            public void setAstro(Astro astro) {
                this.astro = astro;
            }

            public Cond getCond() {
                return cond;
            }

            public void setCond(Cond cond) {
                this.cond = cond;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public Tmp getTmp() {
                return tmp;
            }

            public void setTmp(Tmp tmp) {
                this.tmp = tmp;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public Wind getWind() {
                return wind;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            /**
             * 天文数值<br>
             * sr : 06:05
             * ss : 19:52
             */
            public static class Astro {

                @SerializedName("sr")
                private String sr;
                @SerializedName("ss")
                private String ss;

                public String getSr() {
                    return sr;
                }

                public void setSr(String sr) {
                    this.sr = sr;
                }

                public String getSs() {
                    return ss;
                }

                public void setSs(String ss) {
                    this.ss = ss;
                }
            }

            /**
             * 天气状况<br>
             * code_d : 104
             * code_n : 104
             * txt_d : 阴
             * txt_n : 阴
             */
            public static class Cond {

                @SerializedName("code_d")
                private String codeD;
                @SerializedName("code_n")
                private String codeN;
                @SerializedName("txt_d")
                private String txtD;
                @SerializedName("txt_n")
                private String txtN;

                public String getCodeD() {
                    return codeD;
                }

                public void setCodeD(String codeD) {
                    this.codeD = codeD;
                }

                public String getCodeN() {
                    return codeN;
                }

                public void setCodeN(String codeN) {
                    this.codeN = codeN;
                }

                public String getTxtD() {
                    return txtD;
                }

                public void setTxtD(String txtD) {
                    this.txtD = txtD;
                }

                public String getTxtN() {
                    return txtN;
                }

                public void setTxtN(String txtN) {
                    this.txtN = txtN;
                }
            }

            /**
             * 温度<br>
             * max : 27
             * min : 16
             */
            public static class Tmp {

                @SerializedName("max")
                private String max;
                @SerializedName("min")
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

            /**
             * 风力状况<br>
             * deg : 108
             * dir : 无持续风向
             * sc : 微风
             * spd : 1
             */
            public static class Wind {

                @SerializedName("deg")
                private String deg;
                @SerializedName("dir")
                private String dir;
                @SerializedName("sc")
                private String sc;
                @SerializedName("spd")
                private String spd;

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

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

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }

        /**
         * 每小时天气预报<br>
         * date : 2016-05-17 16:00
         * hum : 44
         * pop : 0
         * pres : 1006
         * tmp : 28
         * wind : {"deg":"54","dir":"东北风","sc":"微风","spd":"8"}
         */
        public static class HourlyForecast {

            @SerializedName("date")
            private String date;
            @SerializedName("hum")
            private String hum;
            @SerializedName("pop")
            private String pop;
            @SerializedName("pres")
            private String pres;
            @SerializedName("tmp")
            private String tmp;
            @SerializedName("wind")
            private Wind wind;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public Wind getWind() {
                return wind;
            }

            public void setWind(Wind wind) {
                this.wind = wind;
            }

            /**
             * 风力状况<br>
             * deg : 54
             * dir : 东北风
             * sc : 微风
             * spd : 8
             */
            public static class Wind {

                @SerializedName("deg")
                private String deg;
                @SerializedName("dir")
                private String dir;
                @SerializedName("sc")
                private String sc;
                @SerializedName("spd")
                private String spd;

                public String getDeg() {
                    return deg;
                }

                public void setDeg(String deg) {
                    this.deg = deg;
                }

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

                public String getSpd() {
                    return spd;
                }

                public void setSpd(String spd) {
                    this.spd = spd;
                }
            }
        }
    }
}
