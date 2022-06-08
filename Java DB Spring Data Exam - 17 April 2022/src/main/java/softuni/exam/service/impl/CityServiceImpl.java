package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportCityDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CityService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validator;
    private final CountryRepository countryRepository;

    @Autowired
    public CityServiceImpl(ValidationUtil validator, CityRepository cityRepository, CountryRepository countryRepository) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;

        this.gson = new GsonBuilder().create();
        this.validator = validator;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cities.json");
        return String.join("", Files.readAllLines(path));
    }

    @Override
    public String importCities() throws IOException {
        String json = this.readCitiesFileContent();

        ImportCityDTO[] importTeamDTOS = this.gson.fromJson(json, ImportCityDTO[].class);

        return Arrays.stream(importTeamDTOS).map(this::importCity).collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<City> getById(long id) {
        return cityRepository.findOneById(id);
    }


    private String importCity(ImportCityDTO importCityDTO) {
        Optional<Country> country = countryRepository.findOneById(importCityDTO.getCountry());

        boolean isValid = this.validator.isValid(importCityDTO, this::isUnique) && country.isPresent();
        if (isValid) {
            City city = this.modelMapper.map(importCityDTO, City.class);
            city.setCountry(country.get());
            this.cityRepository.save(city);
            return (String.format("Successfully imported city %s - %d",
                    city.getCityName(), city.getPopulation()));
        }
        return "Invalid city";
    }

    private boolean isUnique(ImportCityDTO city) {
        return !cityRepository.existsAllByCityName(city.getCityName());
    }
}