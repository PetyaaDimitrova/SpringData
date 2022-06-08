package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportForecastDTO;
import softuni.exam.models.dto.ImportForecastRootDTO;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.CityRepository;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForecastServiceImpl implements ForecastService {
    private final Unmarshaller unmarshaller;
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "forecasts.xml");
    private final ValidationUtil validator;
    private final ModelMapper modelMapper;
    private final ForecastRepository forecastRepository;
    private final CityRepository cityRepository;

    @Autowired
    public ForecastServiceImpl(ValidationUtil validator, ForecastRepository forecastRepository, CityRepository cityRepository)
            throws JAXBException {
        this.validator = validator;
        this.forecastRepository = forecastRepository;
        this.cityRepository = cityRepository;

        JAXBContext jaxbContext = JAXBContext.newInstance(ImportForecastRootDTO.class);
        this.unmarshaller = jaxbContext.createUnmarshaller();

        this.modelMapper = new ModelMapper();
        this.modelMapper.addConverter(ctx -> LocalTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("HH:mm:ss")),
                String.class, LocalTime.class);

        //LocalTime localTime = LocalTime.parse(myDateString, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        ImportForecastRootDTO forecastDTOs = (ImportForecastRootDTO) this.unmarshaller
                .unmarshal(new FileReader(path.toAbsolutePath().toString()));


        return forecastDTOs.getForecasts().stream().map(this::importForecast).collect(Collectors.joining("\n"));
    }
//optional??


    @Override
    public Optional<Forecast> getById(long id) {
        return forecastRepository.findOneById(id);
    }

    private String importForecast(ImportForecastDTO importForecastDTO) {
        Optional<City> city = cityRepository.findOneById(importForecastDTO.getCity());

        boolean isValid = this.validator.isValid(importForecastDTO,this::isUnique) && city.isPresent();

        if (isValid) {
            Forecast forecast = this.modelMapper.map(importForecastDTO, Forecast.class);
            forecast.setCity(city.get());
            this.forecastRepository.save(forecast);
            return String.format("Successfully import forecast %s - %.2f",
                    forecast.getDaysOfWeek(), forecast.getMaxTemperature());
        }
        return "Invalid forecast";
    }

    private  boolean isUnique(ImportForecastDTO forecast) {
        return !forecastRepository.existsAllByCityIdAndDaysOfWeek(forecast.getCity(), forecast.getDaysOfWeek());
    }
    //existsAllByTownTownNameAndArea

    @Override
    public String exportForecasts() {

        int minPopulation = 0;
        int maxPopulation = 150000-1;

        List<Forecast> forecasts = this.forecastRepository
                .findAllByDaysOfWeekAndCityPopulationBetweenOrderByMaxTemperatureDescIdAsc(DaysOfWeek.SUNDAY, minPopulation, maxPopulation);

        return forecasts.stream().map(Forecast::toString).collect(Collectors.joining("\n"));
    }


}
