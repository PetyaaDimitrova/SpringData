package softuni.exam.service;

import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Forecast;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

// TODO: Implement all methods
public interface ForecastService {

    boolean areImported();

    String readForecastsFromFile() throws IOException;
	
	String importForecasts() throws IOException, JAXBException;

    String exportForecasts();
    Optional<Forecast> getById(long id);
}
