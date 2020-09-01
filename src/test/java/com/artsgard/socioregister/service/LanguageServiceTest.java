package com.artsgard.socioregister.service;

import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.repository.LanguageRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LanguageServiceTest {

    @Autowired
    private LanguageRepository repo;
 
    @Test
    public void testFindAllLanguages() {
        List<LanguageModel> langs = repo.findAll();
        assertThat(langs).isNotEmpty();
        assertThat(langs).isNotEmpty().hasSize(5);
    }
}