package com.artsgard.socioregister.service;

import com.artsgard.socioregister.DTO.AddressDTO;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.AddressModel;
import com.artsgard.socioregister.model.AddressModel.AddressType;
import com.artsgard.socioregister.model.CountryModel;
import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AddressRepository;
import com.artsgard.socioregister.repository.CountryRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.serviceimpl.AddressServiceImpl;
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
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class AddressServiceMockitoTest {

    @Mock
    private AddressRepository addressRepo;

    //@Mock
    private SocioRepository socioRepo;

    //@Mock
    private CountryRepository countryRepo;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private SocioModel socioMock;
    private AddressModel addressMock;
    private AddressModel addressMock2;
    private AddressModel addressEmptyMock = new AddressModel();
    private List<AddressModel> addressesEmptyMock = new ArrayList();
    private AddressDTO addressDTOMock;
    private List<AddressModel> addressesMock;

    public static final Long NON_EXISTING_ID = 7000L;
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";

    @BeforeEach
    public void setup() {
        LanguageModel lang1 = new LanguageModel(1L, "Netherlands", "NL");
        LanguageModel lang2 = new LanguageModel(1L, "Spain", "ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        socioMock = new SocioModel(null, "username", "secret", "first name", "last name", "username@gmail.com", true, langs, null);
        socioMock.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socioMock.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));

        CountryModel country = new CountryModel(1L, "", "");
        addressesMock = new ArrayList();
        addressMock = new AddressModel(1L, "Wagner street 4", "München", "5426", "Bauern", country, "some Wagner description", AddressType.HOME, socioMock);
        addressDTOMock = new AddressDTO(1L, "Wagner street 4", "München", "5426", "Bauern", country, "some Wagner description", AddressType.HOME, 1L);
        addressMock2 = new AddressModel(2L, "Bach street 4", "Leipzich", "5426", "Sachsen", country, "some Bach description", AddressType.HOME, socioMock);
        addressesMock.add(addressMock);
        addressesMock.add(addressMock2);
    }

    //@Test
    public void testFindAllAddresses() {
        given(addressRepo.findAll()).willReturn(addressesMock);
        given(mapperService.mapAddressModelToAddressDTO(new AddressModel())).willReturn(addressDTOMock);
        List<AddressDTO> list = addressService.findAllAddresses();
        assertThat(list).isNotEmpty().hasSize(2);
    }

    //@Test
    public void testFindAllAddresses_not_found() {
        addressRepo.deleteAll();
        List<AddressModel> addresses = addressRepo.findAll();
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testFindAddressById() {
        given(addressRepo.getOne(1L)).willReturn(addressMock);
        AddressDTO addr = addressService.findOneAddressById(1L);
        assertThat(addr.getStreet()).isEqualTo(addressMock.getStreet());
    }

    //@Test
    public void testFindAddressById_not_found() {
        Optional<AddressModel> address = addressRepo.findById(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    @Test
    public void testFindAddressBySocioId() {
        given(addressRepo.findAddressesBySocioId(1L)).willReturn(addressesMock);
        List<AddressDTO> addresses = addressService.findAddressesBySocioId(1L);
        assertThat(addresses).isNotEmpty().hasSize(2);
    }

    @Test()
    public void testFindAddressBySocioId_not_found() throws Exception {
        given(addressRepo.findAddressesBySocioId(7000L)).willReturn(addressesEmptyMock);
       
        
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           List<AddressDTO> addresses = addressService.findAddressesBySocioId(7000L);
        });
    }

    //@Test
    public void testSaveAddress() {
        SocioModel socio = socioRepo.getOne(1L);
        CountryModel country = countryRepo.findByCode("NL");
        AddressModel address = new AddressModel(null, "Wagner street 4", "München", "5426", "Bauern", country, "soem description", AddressType.HOME, socio);
        addressRepo.save(address);
        assertThat(address.getId()).isNotNull();
    }

    //@Test
    public void testUpdateAddress() {
        AddressModel updateAddress = addressRepo.getOne(1L);
        updateAddress.setPostalcode("edited postal code");
        updateAddress.setStreet("edit street");
        AddressModel updatedAddressFromDB = addressRepo.save(updateAddress);
        assertThat(updateAddress).isEqualTo(updatedAddressFromDB);
    }

    //@Test
    public void testUpdateAddress_not_found() {
        AddressModel updateAddress = addressRepo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testDeleteAddressById() {
        SocioModel socio = socioRepo.getOne(1L);
        CountryModel country = countryRepo.findByCode("NL");
        AddressModel address = new AddressModel(null, "Wagner street 4", "München", "5426", "Bauern", country, "some description", AddressType.HOME, socio);
        addressRepo.save(address);
        Long id = address.getId();
        addressRepo.deleteById(address.getId());
        assertThat(addressRepo.existsById(id)).isFalse();
    }

    //@Test
    public void testDeleteAddressById_not_found() {
        SocioModel socio = socioRepo.getOne(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }
}
