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
        SocioAssociatedSocio asc = associatedSocioRepository.save(associatedSocio);
        Optional<SocioAssociatedSocio> optCreatedAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThat(optCreatedAssociatedSocio.get().getAssociatedSocioState()).isEqualTo(AssociatedSocioState.PENDING);
        assertThat(asc.getSocioId()).isEqualTo(associatedSocio.getSocioId());
        assertThat(asc.getAssociatedSocioId()).isEqualTo(associatedSocio.getAssociatedSocioId());
    }

    @Test
    public void testUpdateStateAssociatedSocio() {
        Long socioId = 1L;
        Long associatedSocioId = 2L;
        Optional<SocioAssociatedSocio> optAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        optAssociatedSocio.get().setAssociatedSocioDate(new Timestamp(System.currentTimeMillis()));
        optAssociatedSocio.get().setAssociatedSocioState(AssociatedSocioState.ACCEPTED);
        SocioAssociatedSocio asc = associatedSocioRepository.save(optAssociatedSocio.get());
        Optional<SocioAssociatedSocio> optUpdateAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThat(optUpdateAssociatedSocio.get().getAssociatedSocioState()).isEqualTo(AssociatedSocioState.ACCEPTED);
        assertThat(asc.getSocioId()).isEqualTo(optAssociatedSocio.get().getSocioId());
        assertThat(asc.getAssociatedSocioId()).isEqualTo(optAssociatedSocio.get().getAssociatedSocioId());
    }

    @Test
    public void testUpdateStateAssociatedSocio_not_found() {
        Long socioId = 1L;
        Long associatedSocioId = NON_EXISTING_ID;
        SocioModel socio = socioRepo.getOne(socioId);
        SocioModel socio2 = socioRepo.getOne(associatedSocioId);

        Optional<SocioAssociatedSocio> optAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        if (optAssociatedSocio.isPresent()) {
            optAssociatedSocio.get().setAssociatedSocioDate(new Timestamp(System.currentTimeMillis()));
            optAssociatedSocio.get().setAssociatedSocioState(AssociatedSocioState.ACCEPTED);
            associatedSocioRepository.save(optAssociatedSocio.get());
            associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        }
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testDeleteStateAssociatedSocio() {
        Long socioId = 1L;
        Long associatedSocioId = 2L;
        Optional<SocioAssociatedSocio> associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        associatedSocioRepository.deleteAssociatedSocio(socioId, associatedSocioId);
        Optional<SocioAssociatedSocio> deletedAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testDeleteStateAssociatedSocio_not_found() {
        Long socioId = 1L;
        Long associatedSocioId = NON_EXISTING_ID;
        SocioModel socio = socioRepo.getOne(socioId);
        SocioModel socio2 = socioRepo.getOne(associatedSocioId);
        Optional<SocioAssociatedSocio> associatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        associatedSocioRepository.deleteAssociatedSocio(socioId, associatedSocioId);
        Optional<SocioAssociatedSocio> deletedAssociatedSocio = associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}
