package ru.etozhealexis.districtsservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.etozhealexis.districtsservice.model.District;
import ru.etozhealexis.districtsservice.repository.DistrictRepository;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DistrictsLoader implements CommandLineRunner {
    private final DistrictRepository districtRepository;

    @Override
    public void run(String... args) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("districts.csv");
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);

        List<String> districtNames = new LinkedList<>();
        String districtName = reader.readLine();
        while (districtName != null) {
            districtNames.add(districtName);
            districtName = reader.readLine();
        }
        reader.close();

        districtNames.forEach(name -> {
            District district = districtRepository.getByNameOrAlias(name);
            if (district == null) {
                districtRepository.save(District.builder()
                        .name(name)
                        .build());
            }
        });
    }
}
