package com.artsgard.socioregister.service;

import com.artsgard.socioregister.DTO.AddressDTO;
import com.artsgard.socioregister.DTO.SocioDTO;
import com.artsgard.socioregister.model.AddressModel;
import com.artsgard.socioregister.model.CountryModel;
import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.serviceimpl.MapperServiceImpl;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

/**
 *
 * @author WillemDragstra
 */
@ExtendWith(MockitoExtension.class)
public class MapperServiceTest {
    private SocioDTO socioDTOMock;
    private SocioModel socioModelMock;
    private AddressDTO addressDTOMock;
    private AddressModel addressModelMock;
    
    @Mock
    private SocioRepository socioRepository;

    MapperService mapperService = new MapperServiceImpl();

    @BeforeEach
    public void setup() {
        LanguageModel lang1 = new LanguageModel(1L, "Netherlands", "NL");
        LanguageModel lang2 = new LanguageModel(1L, "Spain", "ES");
        List<LanguageModel> langs = new ArrayList();
        langs.add(lang1);
        langs.add(lang2);
        
        socioDTOMock = new SocioDTO(1L, "js", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, langs, null);
        socioDTOMock.setRegisterDate(new Timestamp(1479250540110L));
        socioModelMock = new SocioModel(1L, "js", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, langs, null);
        socioModelMock.setRegisterDate(new Timestamp(1479250540110L));
        addressDTOMock = new AddressDTO(1L, "Vermeerstraat 23", "Eindhoven", "3454JK", "Brabant", new CountryModel(1L, "Netherlands", "NL"), "ok place", AddressModel.AddressType.HOME, 1L);
        addressModelMock = new AddressModel(1L, "Vermeerstraat 23", "Eindhoven", "3454JK", "Brabant", new CountryModel(1L, "Netherlands", "NL"), "ok place", AddressModel.AddressType.HOME, socioModelMock);
    }

    @Test
    public void mapSocioDTOToSocioModelTest() {
        assertNotNull(socioModelMock);
        SocioDTO dto = mapperService.mapSocioModelToSocioDTO(socioModelMock);
        assertThat(dto, isA(SocioDTO.class));
        assertEquals(dto.getUsername(), socioDTOMock.getUsername());
    }
    
    @Test
    public void mapSocioModelToSocioDTOTest() {
        assertNotNull(socioDTOMock);
        SocioModel model = new SocioModel();
        model = mapperService.mapSocioDTOToSocioModel(socioDTOMock);
        assertThat(model, isA(SocioModel.class));
        assertEquals(model.getEmail(), socioModelMock.getEmail());
    }
    
    @Test
    public void mapAddressDTOToAddressModelTest() {
        assertNotNull(addressDTOMock);
        
        ModelMapper modelMapper = new ModelMapper();
        AddressModel addr = modelMapper.map(addressDTOMock, AddressModel.class);
        addr.setSocio(socioModelMock);
        
        assertThat(addr, isA(AddressModel.class));
        assertEquals(addr.getCountry(), addressModelMock.getCountry());
        assertEquals(addr.getCity(), addressModelMock.getCity());
        assertEquals(addr.getStreet(), addressModelMock.getStreet());
        assertEquals(addr.getPostalcode(), addressModelMock.getPostalcode());
        assertEquals(addr.getAddressType(), addressModelMock.getAddressType());
        assertEquals(addr.getSocio(), addressModelMock.getSocio());      
    }
    
    @Test
    public void mapAddressModelToAddressDTOTest() {
        assertNotNull(addressModelMock);
        AddressDTO dto = mapperService.mapAddressModelToAddressDTO(addressModelMock);
        dto.setCountry(new CountryModel(1L, "Netherlands", "NL"));
        assertThat(dto, isA(AddressDTO.class));
        assertEquals(dto.getPostalcode(), addressModelMock.getPostalcode());
    }
}
