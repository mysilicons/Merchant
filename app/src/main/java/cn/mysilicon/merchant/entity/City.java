package cn.mysilicon.merchant.entity;

import java.util.List;

public class City {
    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    public static class CityBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}

