package com.artsgard.socioregister.service;

import com.artsgard.socioregister.model.CountryModel;
import com.artsgard.socioregister.repository.CountryRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Sql({"/data-test.sql"})
@DataJpaTest
public class CountryServiceTest {

    @Autowired
    private CountryRepository repo;
    
    
    @Test
    public void testFindAllCountries() {
        List<CountryModel> countries = repo.findAll();
        assertThat(countries).isNotEmpty();
        assertThat(countries).isNotEmpty().hasSize(5);
    }
}