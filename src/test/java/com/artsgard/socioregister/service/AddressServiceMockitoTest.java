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
import static com.artsgard.socioregister.service.SocioServiceMockitoTest.EXISTING_ID;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AddressServiceMockitoTest {

    @Mock
    private AddressRepository addressRepo;

    @Mock
    private SocioRepository socioRepo;

    @Mock
    private CountryRepository countryRepo;

    @Mock
    private MapperService mapperService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private SocioModel socioMock;
    private AddressModel addressMock;
    private AddressModel addressMock2;
    private final AddressModel addressModelEmptyMock = new AddressModel();
    private final AddressDTO addressDTOEmptyMock = new AddressDTO();
    private final List<AddressModel> addressesEmptyMock = new ArrayList();
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
        addressMock = new AddressModel(null, "Wagner street 4", "München", "5426", "Bauern", country, "some Wagner description", AddressType.HOME, socioMock);
        addressDTOMock = new AddressDTO(null, "Wagner street 4", "München", "5426", "Bauern", country, "some Wagner description", AddressType.HOME, 1L);
        addressMock2 = new AddressModel(null, "Bach street 4", "Leipzich", "5426", "Sachsen", country, "some Bach description", AddressType.HOME, socioMock);
        addressesMock.add(addressMock);
        addressesMock.add(addressMock2);
    }

    @Test
    public void testFindAllAddresses() {
        given(addressRepo.findAll()).willReturn(addressesMock);
        given(mapperService.mapAddressModelToAddressDTO(any(AddressModel.class))).willReturn(addressDTOMock);
        List<AddressDTO> list = addressService.findAllAddresses();
        assertThat(list).isNotEmpty().hasSize(2);
    }

    @Test
    public void testFindAllAddresses_not_found() {
        List<AddressModel> emptyList = new ArrayList();
        given(addressRepo.findAll()).willReturn(emptyList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.findAllAddresses();
        });
    }

    @Test
    public void testFindAddressById() {
        addressMock.setId(EXISTING_ID);
        given(addressRepo.findById(EXISTING_ID)).willReturn(Optional.of(addressMock));
        given(mapperService.mapAddressModelToAddressDTO(any(AddressModel.class))).willReturn(addressDTOMock);
        AddressDTO addr = addressService.findOneAddressById(1L);
        assertThat(addr.getStreet()).isEqualTo(addressMock.getStreet());
    }

    @Test
    public void testFindAddressById_not_found() {
        given(addressRepo.findById(1L)).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            AddressDTO addr = addressService.findOneAddressById(1L);
        });
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

    @Test
    public void testSaveAddress() { 
        addressMock.setId(EXISTING_ID);
        given(addressRepo.save(addressMock)).willReturn(addressMock);
        given(mapperService.mapAddressDTOToAddressModel(any(AddressDTO.class))).willReturn(addressMock);
        given(socioRepo.findById(any(Long.class))).willReturn(Optional.of(socioMock));
        AddressDTO addr = addressService.saveAddress(addressDTOMock);
        assertThat(addr).isNotNull(); // why is this null!!!!!
    }

    @Test
    public void testUpdateAddress() {
        addressDTOMock.setId(EXISTING_ID);
        addressMock.setId(EXISTING_ID);
        given(addressRepo.save(addressMock)).willReturn(addressMock);
        given(addressRepo.findById(any(Long.class))).willReturn(Optional.of(addressMock));
        given(mapperService.mapAddressDTOToAddressModel(any(AddressDTO.class))).willReturn(addressMock);
        AddressDTO addr = addressService.updateAddress(addressDTOMock);
        assertThat(addr).isNotNull(); // why is this null!!!!!
    }

    @Test
    public void testUpdateAddress_not_found() {
        given(addressRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressDTOMock.setId(any(Long.class));
            addressService.updateAddress(addressDTOMock);
        });
    }

    @Test
    public void testDeleteAddressById() {
        addressRepo.deleteById(EXISTING_ID);
        verify(addressRepo, times(1)).deleteById(eq(EXISTING_ID));
    }

    @Test
    public void testDeleteAddressById_not_found() {
        given(addressRepo.findById(any(Long.class))).willReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            addressService.deleteAddressById(any(Long.class));
        });
    }
}
