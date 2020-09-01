package com.artsgard.socioregister.service;

import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.SocioAssociatedSocio;
import com.artsgard.socioregister.model.SocioAssociatedSocio.AssociatedSocioState;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AssociatedSocioRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import java.sql.Timestamp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource({"classpath:application-test.properties"})
@DataJpaTest
public class AssociatedSocioServiceDataJpaTest {

    @Autowired
    private SocioRepository socioRepo;

    @Autowired
    private AssociatedSocioRepository associatedSocioRepository;

    public static final Long NON_EXISTING_ID = 7000L;

    @Test
    public void testRegisterAssociatedSocio() {
        Long socioId = 1L;
        Long associatedSocioId = 2L;
        Optional<SocioModel> optSocio = socioRepo.findById(socioId);
        Optional<SocioModel> optAssociatedSocio = socioRepo.findById(associatedSocioId);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SocioAssociatedSocio associatedSocio = new SocioAssociatedSocio(socioId, associatedSocioId, optSocio.get(), optAssociatedSocio.get(), AssociatedSocioState.PENDING, now);
        associatedSocioRepository.save(associatedSocio);
        SocioAssociatedSocio createdAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThat(createdAssociatedSocio.getAssociatedSocioState()).isEqualTo(AssociatedSocioState.PENDING);
    }

    @Test
    public void testUpdateStateAssociatedSocio() {
        Long socioId = 1L;
        Long associatedSocioId = 2L;
        SocioAssociatedSocio associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        associatedSocio.setAssociatedSocioDate(new Timestamp(System.currentTimeMillis()));
        associatedSocio.setAssociatedSocioState(AssociatedSocioState.ACCEPTED);
        associatedSocioRepository.save(associatedSocio);
        SocioAssociatedSocio updateAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThat(updateAssociatedSocio.getAssociatedSocioState()).isEqualTo(AssociatedSocioState.ACCEPTED);
    }

    @Test
    public void testUpdateStateAssociatedSocio_not_found() {
        Long socioId = 1L;
        Long associatedSocioId = NON_EXISTING_ID;
        SocioModel socio = socioRepo.getOne(socioId);
        SocioModel socio2 = socioRepo.getOne(associatedSocioId);

        SocioAssociatedSocio associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        if (associatedSocio != null) {
            associatedSocio.setAssociatedSocioDate(new Timestamp(System.currentTimeMillis()));
            associatedSocio.setAssociatedSocioState(AssociatedSocioState.ACCEPTED);
            associatedSocioRepository.save(associatedSocio);
            SocioAssociatedSocio updateAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        }
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testDeleteStateAssociatedSocio() {
        Long socioId = 1L;
        Long associatedSocioId = 2L;
        SocioAssociatedSocio associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        associatedSocioRepository.deleteAssociatedSocio(socioId, associatedSocioId);
        SocioAssociatedSocio deletedAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThat(deletedAssociatedSocio).isNull();
    }

    @Test
    public void testDeleteStateAssociatedSocio_notg_found() {
        Long socioId = 1L;
        Long associatedSocioId = NON_EXISTING_ID;
        SocioModel socio = socioRepo.getOne(socioId);
        SocioModel socio2 = socioRepo.getOne(associatedSocioId);
        SocioAssociatedSocio associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        associatedSocioRepository.deleteAssociatedSocio(socioId, associatedSocioId);
        SocioAssociatedSocio deletedAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}
