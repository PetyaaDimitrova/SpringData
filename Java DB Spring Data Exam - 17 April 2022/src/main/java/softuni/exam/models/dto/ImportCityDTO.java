package softuni.exam.models.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class ImportCityDTO {

    @Size(min = 2)
    @Size(max = 60)
    private String cityName;

    @Size(min = 2)
    private String description;

    @Min(500)
    private int population;

    private long country;

    public long getCountry() {
        return country;
    }

    public void setCountry(long country) {
        this.country = country;
    }

    public ImportCityDTO() {
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }


}
