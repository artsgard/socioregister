package com.artsgard.socioregister.service;

import com.artsgard.socioregister.DTO.SocioDTO;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.model.SocioAssociatedSocio;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AddressRepository;
import com.artsgard.socioregister.repository.AssociatedSocioRepository;
import com.artsgard.socioregister.repository.CountryRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import static com.artsgard.socioregister.service.SocioServiceMockitoTest.EXISTING_ID;
import com.artsgard.socioregister.serviceimpl.AddressServiceImpl;
import com.artsgard.socioregister.serviceimpl.AssociatedSocioServiceImpl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AssociatedSocioServiceMockitoTest {

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private SocioRepository socioRepo;

    @Mock
    private CountryRepository countryRepo;

    @Mock
    private AssociatedSocioRepository associatedSocioRepository;

    @Mock
    private AddressServiceImpl addressService;

    @InjectMocks
    AssociatedSocioServiceImpl associatedSocioService;

    @Mock
    private MapperService mapperService;

    private SocioModel socioModelMock1;
    private SocioModel socioModelMock2;
    private SocioDTO socioDTOMock1;
    private SocioDTO socioDTOMock2;

    private SocioAssociatedSocio associatedSocioMock;

    private List<SocioModel> socioModelListMock;
    public static final Long NON_EXISTING_ID = 7000L;
    public static final String EXISTING_USERNAME = "username";
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";
    public static final Long EXISTING_ID = 1L;
    public static final Long SOCIO_ID = 1L;
    public static final Long ASSOCIATED_SOCIO_ID = 2L;

    ;

    @BeforeEach
    public void setup() {
        LanguageModel lang1 = new LanguageModel(1L, "Netherlands", "NL");
        LanguageModel lang2 = new LanguageModel(1L, "Spain", "ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        socioModelMock1 = new SocioModel(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelMock1.setRegisterDate(now);
        socioModelMock1.setLastCheckinDate(now);

        socioModelMock2 = new SocioModel(null, "username2", "secret2", "firstname2", "lastname2", "username2@gmail.com", true, langs, null);
        socioModelMock2.setRegisterDate(now);
        socioModelMock2.setLastCheckinDate(now);

        socioDTOMock1 = new SocioDTO(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelListMock = new ArrayList();
        socioModelListMock.add(socioModelMock1);
        socioModelListMock.add(socioModelMock2);
        associatedSocioMock = new SocioAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID, socioModelMock1, socioModelMock2, SocioAssociatedSocio.AssociatedSocioState.PENDING, now);
    }

    @Test
    public void getAssociatedSocioBySocioIdAndAssociatedSocioId() {
        given(associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(SOCIO_ID, ASSOCIATED_SOCIO_ID))
                .willReturn(Optional.of(associatedSocioMock));

        SocioAssociatedSocio asc = associatedSocioService.getAssociatedSocioBySocioIdAndAssociatedSocioId(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        assertThat(asc).isNotNull();
        assertThat(asc).isEqualTo(associatedSocioMock);
    }

    @Test
    public void getAssociatedSocioBySocioIdAndAssociatedSocioId_not_found() {
        given(associatedSocioRepository.getAssociatedSocioBySocioIdAndAssociatedSocioId(SOCIO_ID, ASSOCIATED_SOCIO_ID))
                .willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            associatedSocioService.getAssociatedSocioBySocioIdAndAssociatedSocioId(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        });
    }

    @Test
    public void testRegisterAssociatedSocio() {
        given(associatedSocioRepository.save(any(SocioAssociatedSocio.class)))
                .willReturn(associatedSocioMock);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioModelMock1));
        SocioAssociatedSocio asc = associatedSocioService.registerAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        assertThat(asc).isNotNull();
        assertThat(asc).isEqualTo(associatedSocioMock);
    }

    @Test
    public void testRegisterAssociatedSocio_socios_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            associatedSocioService.registerAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        });
    }

    @Test
    public void testUpdateStateAssociatedSocio() {
        associatedSocioMock.setAssociatedSocioState(SocioAssociatedSocio.AssociatedSocioState.ACCEPTED);
        given(associatedSocioRepository.save(any(SocioAssociatedSocio.class)))
                .willReturn(associatedSocioMock);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioModelMock1));
        SocioAssociatedSocio asc = associatedSocioService.updateStateAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID, true);
        assertThat(asc).isNotNull();
        assertThat(asc.getAssociatedSocioState())
                .isEqualTo(SocioAssociatedSocio.AssociatedSocioState.ACCEPTED);
        assertThat(asc).isEqualTo(associatedSocioMock);
    }

    @Test
    public void testUpdateStateAssociatedSocio_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            associatedSocioService.updateStateAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID, true);
        });
    }

    @Test
    public void testDeleteAssociatedSocio() {
        associatedSocioRepository.deleteAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        verify(associatedSocioRepository, times(1)).deleteAssociatedSocio(eq(SOCIO_ID), eq(ASSOCIATED_SOCIO_ID));
    }

    @Test
    public void testDeleteStateAssociatedSocio_not_found() {
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            associatedSocioService.deleteAssociatedSocio(SOCIO_ID, ASSOCIATED_SOCIO_ID);
        });
    }
}
