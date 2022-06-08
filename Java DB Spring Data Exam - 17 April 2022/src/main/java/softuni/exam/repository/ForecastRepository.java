package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.DaysOfWeek;
import softuni.exam.models.entity.Forecast;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Integer> {

    Optional<Forecast> findOneById(long id);



    boolean existsAllByCityIdAndDaysOfWeek(long id, DaysOfWeek daysOfWeek);

    List<Forecast> findAllByDaysOfWeekAndCityPopulationBetweenOrderByMaxTemperatureDescIdAsc(DaysOfWeek sunday, int minPopulation, int maxPopulation);
}
