package softuni.exam.models.dto;

import javax.validation.constraints.Size;

public class ImportCountryDTO {

    @Size(min = 2)
    @Size(max = 60)
    private String countryName;

    @Size(min = 2)
    @Size(max = 20)
    private String currency;

    public ImportCountryDTO() {
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
