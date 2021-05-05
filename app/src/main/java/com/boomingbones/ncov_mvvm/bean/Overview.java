package com.boomingbones.ncov_mvvm.bean;

import java.util.List;

public class Overview {

    public Domestic domestic;

    public List<Area> provincesList;

    public Global global;

    public List<Area> countriesList;

    public Overview(Domestic domestic, List<Area> provincesList,
                    Global global, List<Area> countriesList) {
        this.domestic = domestic;
        this.provincesList = provincesList;
        this.global = global;
        this.countriesList = countriesList;
    }
}
