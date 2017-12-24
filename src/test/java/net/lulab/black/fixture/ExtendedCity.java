package net.lulab.black.fixture;

import java.util.List;

public class ExtendedCity extends City {
    private List<City> cities;

    public ExtendedCity(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }
}

