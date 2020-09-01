package com.artsgard.socioregister.service;

import com.artsgard.socioregister.DTO.AddressDTO;
import com.artsgard.socioregister.DTO.FilterDTO;
import com.artsgard.socioregister.DTO.SocioDTO;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.AddressModel;
import com.artsgard.socioregister.model.CountryModel;
import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AddressRepository;
import com.artsgard.socioregister.repository.CountryRepository;
import com.artsgard.socioregister.repository.LanguageRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.serviceimpl.AddressServiceImpl;
import com.artsgard.socioregister.serviceimpl.SocioServiceImpl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.convention.MatchingStrategies;

@ExtendWith(MockitoExtension.class)
public class SocioServiceMockitoTest {

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private SocioRepository socioRepo;

    @Mock
    private CountryRepository countryRepo;

    @Mock
    private AddressServiceImpl addressService;

    @Mock
    private LanguageRepository languageRepo;

    @InjectMocks
    SocioServiceImpl socioService;

    @Mock
    private MapperService mapperService;

    private SocioModel socioModelMock1;
    private SocioModel socioModelMock2;
    private SocioDTO socioDTOMock1;
    private SocioDTO socioDTOMock2;
    private List<SocioModel> socioModelListMock;
    public static final Long NON_EXISTING_ID = 7000L;
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";

    @BeforeEach
    public void setup() {
        LanguageModel lang1 = new LanguageModel(1L, "Netherlands", "NL");
        LanguageModel lang2 = new LanguageModel(1L, "Spain", "ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        socioModelMock1 = new SocioModel(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelMock1.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socioModelMock1.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));

        socioModelMock2 = new SocioModel(null, "username2", "secret2", "firstname2", "lastname2", "username2@gmail.com", true, langs, null);
        socioModelMock2.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socioModelMock2.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));

        socioDTOMock1 = new SocioDTO(null, "username1", "secret1", "firstname1", "lastname1", "username1@gmail.com", true, langs, null);
        socioModelListMock = new ArrayList();
        socioModelListMock.add(socioModelMock1);
        socioModelListMock.add(socioModelMock2);
    }

    @Test
    public void findAllSociosTest() {
        given(socioRepo.findAll()).willReturn(socioModelListMock);
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        List<SocioDTO> list = socioService.findAllSocios();
        assertThat(list).isNotEmpty().hasSize(2);
    }

    @Test
    public void findAllSociosTest_not_found() {
        List<SocioModel> emptyList = new ArrayList();
        given(socioRepo.findAll()).willReturn(emptyList);
        //given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(any(SocioDTO.class));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findAllSocios();
        });
    }

    @Test
    public void findSocioByIdTest() {
        given(socioRepo.findById(1L)).willReturn(Optional.of(socioModelMock1));
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        SocioDTO sc = socioService.findSocioById(1L);
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(socioModelMock1.getUsername());
    }

    @Test
    public void findSocioByIdTest_not_found() {
        given(socioRepo.findById(1L)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findSocioById(1L);
        });
    }

    @Test
    public void findSocioByUsernameTest() {
        given(socioRepo.findByUsername("js")).willReturn(Optional.of(socioModelMock1));
        given(mapperService.mapSocioModelToSocioDTO(any(SocioModel.class))).willReturn(socioDTOMock1);
        SocioDTO sc = socioService.findSocioByUsername("js");
        assertThat(sc).isNotNull();
        assertThat(sc.getUsername()).isEqualTo(socioModelMock1.getUsername());
    }

    @Test
    public void findSocioByUsernameTest_not_found() {
        given(socioRepo.findByUsername("js")).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            socioService.findSocioByUsername("js");
        });
    }

    @Test
    public void saveSocioTest() {
        /*
        LanguageModel lang1 = new LanguageModel(1L, "German", "DB");
        LanguageModel lang2 = new LanguageModel(2L, "English", "GB");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        SocioModel socio = new SocioModel(null, "username", "secret", "first name", "last name", "username@gmail.com", true, langs, null);
        SocioDTO socioDTO = new SocioDTO(null, "username", "secret", "first name", "last name", "username@gmail.com", true, langs, null);
        socio.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socio.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));
*/
        given(socioRepo.save(socioModelMock1)).willReturn(socioModelMock1);
        given(mapperService.mapSocioDTOToSocioModel(any(SocioDTO.class))).willReturn(socioModelMock1);
        SocioDTO sc = socioService.saveSocio(socioDTOMock1);
        assertThat(sc).isNotNull(); // why is this null!!!!!
    }

    //@Test
    public void updateSocioTest() {
        Optional<SocioModel> optSocio = socioRepo.findById(1L);
        SocioModel updateSocio = optSocio.get();
        updateSocio.setUsername("js edited");
        updateSocio.setActive(false);
        SocioModel updatedSocioFromDB = socioRepo.save(updateSocio);
        assertThat(optSocio.get()).isEqualTo(updatedSocioFromDB);
    }

    //@Test
    public void updateSocioTest_not_found() {
        SocioModel socio = socioRepo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void deleteSocioByIdTest() {
        LanguageModel lang1 = languageRepo.findByCode("NL");
        LanguageModel lang2 = languageRepo.findByCode("ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        SocioModel socio = new SocioModel(null, "username to delete", "secret to delete", "first name to delete", "last name to delete", "todelete@gmail.com", true, langs, null);
        socio.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socio.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));
        socioRepo.save(socio);
        Long id = socio.getId();
        socioRepo.deleteById(id);
        Optional<SocioModel> deletedSocio = socioRepo.findById(id);
        assertThat(deletedSocio.isPresent()).isFalse();
    }

    //@Test
    public void deleteSocioByIdTest_not_found() {
        SocioModel socio = socioRepo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void hasSocioByIdTest() {
        Optional<SocioModel> optSocio = socioRepo.findByUsername("js");
        SocioModel socio = optSocio.get();
        assertThat(socioRepo.existsById(socio.getId())).isTrue();
    }

    //@Test
    public void isSocioActiveByIdTest() {
        Optional<SocioModel> optSocio = socioRepo.findByUsername("js");
        SocioModel socio = optSocio.get();
        socioRepo.save(socio);
        socio.setActive(Boolean.FALSE);
        assertThat(socio.getActive()).isFalse();
    }

    //@Test
    public void isSocioActiveByIdTest_not_found() {
        SocioModel socio = socioRepo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testGetSociosBySortedPageByCountry() {
        FilterDTO filter = new FilterDTO();
        filter.setCountry("NL");
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByCountry(10, 0, filter.getCountry());
        for (SocioModel sc : socios) {
            for (AddressModel add : sc.getSocioAddresses()) {
                assertThat(add.getCountry()).isEqualTo(new CountryModel(1L, "Netherlands", "NL"));
            }

        }
        assertThat(socios).isNotEmpty();
        assertThat(socios).hasSize(2);

    }

    //@Test
    public void testGetSociosBySortedPageByCountry_not_found() {
        FilterDTO filter = new FilterDTO();
        filter.setCountry("NLxxxxx");
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByCountry(10, 0, filter.getCountry());
        assertThatExceptionOfType(ResourceNotFoundException.class);

    }

    //@Test
    public void testGetSociosBySortedPageByLanguage() {
        FilterDTO filter = new FilterDTO();
        filter.setLanguage("FR");
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByLanguage(3, 0, filter.getLanguage());
        for (SocioModel sc : socios) {
            assertThat(sc.getSocioLanguages()).contains(new LanguageModel(3L, "France", "FR"));
        }
        assertThat(socios).isNotEmpty();

    }

    //@Test
    public void testGetSociosBySortedPageByLanguage_not_found() {
        FilterDTO filter = new FilterDTO();
        filter.setLanguage("FRxxxxx");
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPageByLanguage(3, 0, filter.getLanguage());
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testGetSociosBySortedPage() {
        List<SocioModel> socios = socioRepo.getSociosBySortedPage(2, 0);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testGetSociosBySortedPage_not_found() {
        socioRepo.deleteAll();
        List<SocioModel> socios = socioRepo.getSociosBySortedPage(2, 0);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}
