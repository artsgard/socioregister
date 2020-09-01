package com.artsgard.socioregister.service;

import com.artsgard.socioregister.controller.AddressController;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.AddressModel;
import com.artsgard.socioregister.model.AddressModel.AddressType;
import com.artsgard.socioregister.model.CountryModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AddressRepository;
import com.artsgard.socioregister.repository.CountryRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.serviceimpl.AddressServiceImpl;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    private AddressServiceImpl addressService;

    @InjectMocks
    AddressController addressController;

    public static final Long NON_EXISTING_ID = 7000L;
    public static final String NON_EXISTING_USERNAME = "SDFSDFSFSDFSDF";

    //@Test
    public void testFindAllAddresses() {
        List<AddressModel> addresses = addressRepo.findAll();
        assertThat(addresses).isNotEmpty();
        assertThat(addresses).isNotEmpty().hasSize(2);
    }

    //@Test
    public void testFindAllAddresses_not_found() {
        addressRepo.deleteAll();
        List<AddressModel> addresses = addressRepo.findAll();
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testFindAddressById() {
        Optional<AddressModel> address = addressRepo.findById(1L);
        assertThat(address.get().getStreet()).isEqualTo("Edmondstraat 36");
    }

    //@Test
    public void testFindAddressById_not_found() {
        Optional<AddressModel> address = addressRepo.findById(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
    }

    //@Test
    public void testFindAddressBySocioId() {
        List<AddressModel> addresses = addressRepo.findAddressesBySocioId(1L);
        assertThat(addresses).isNotEmpty().hasSize(2);
    }

    //@Test
    public void testFindAddressBySocioId_not_found() {
        List<AddressModel> addresses = addressRepo.findAddressesBySocioId(NON_EXISTING_ID);
        assertThatExceptionOfType(ResourceNotFoundException.class);
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
