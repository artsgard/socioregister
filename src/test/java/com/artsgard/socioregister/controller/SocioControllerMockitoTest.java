package com.artsgard.socioregister.controller;

import com.artsgard.socioregister.DTO.AddressDTO;
import com.artsgard.socioregister.mock.SocioMock;
import com.artsgard.socioregister.DTO.SocioDTO;
import com.artsgard.socioregister.model.LanguageModel;
import com.artsgard.socioregister.service.MapperService;
import com.artsgard.socioregister.serviceimpl.MapperServiceImpl;
import com.artsgard.socioregister.serviceimpl.SocioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author WillemDragstra
 */

@ExtendWith(MockitoExtension.class)
public class SocioControllerMockitoTest {

    private MockMvc mockMvc;

    @Mock
    private SocioServiceImpl socioService;

    @InjectMocks
    SocioController socioController;

    private JacksonTester<SocioDTO> jsonSocio;
    private JacksonTester<List<SocioDTO>> jsonSocios;
    private SocioDTO socio;
    private List<SocioDTO> socios;

    private final MapperService mapperService = new MapperServiceImpl();

    @BeforeEach
    public void setup() {
        
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(socioController).build();

        socio = mapperService.mapSocioModelToSocioDTO(SocioMock.generateSocio());
        socios = new ArrayList();
        SocioMock.generateSocios().forEach((sci) -> {
            socios.add(mapperService.mapSocioModelToSocioDTO(sci));
        });
    }

    @Test
    public void testFindAllSocios() throws Exception {

        given(socioService.findAllSocios()).willReturn(socios);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/socio"))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonSocios.write(socios).getJson()
        );
    }

    @Test
    public void testFindSocioById() throws Exception {
        given(socioService.findSocioById(1L)).willReturn(socio);

        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/socio/1"))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonSocio.write(socio).getJson()
        );
    }

    @Test
    public void testFindSocioByUsername() throws Exception {
        given(socioService.findSocioByUsername("wd")).willReturn(socio);
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.get("/socio/username/wd"))
                .andExpect((content()
                .contentType(MediaType.APPLICATION_JSON_VALUE)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonSocio.write(socio).getJson()
        );
    }

    @Test
    public void testSaveSocio() throws Exception {
        SocioDTO newSocio = new SocioDTO(null, "js edited", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, new <LanguageModel>ArrayList(), new <AddressDTO>ArrayList());
        newSocio.setRegisterDate(new Timestamp(1479250540110L));
        newSocio.setLastCheckinDate(new Timestamp(1479250540110L));
        given(socioService.saveSocio(any(SocioDTO.class))).willReturn(newSocio);
        
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.post("/socio/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)      
                .content(jsonSocio.write(newSocio)
                .getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
         assertThat(response.getContentAsString()).isEqualTo(
                jsonSocio.write(newSocio).getJson());
    }

    @Test
    public void testUpdatesocio() throws Exception {
        SocioDTO editSocio = new SocioDTO(1L, "js edited", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, new <LanguageModel>ArrayList(), new <AddressDTO>ArrayList());
        editSocio.setRegisterDate(new Timestamp(1479250540110L));
        given(socioService.updateSocio(any(SocioDTO.class))).willReturn(editSocio);
        
        MockHttpServletResponse response = mockMvc
                .perform(MockMvcRequestBuilders.put("/socio/")
                .contentType(MediaType.APPLICATION_JSON)
            .content(jsonSocio.write(editSocio)
                .getJson())).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonSocio.write(editSocio).getJson());
    }
    
    @Test
    public void testDeleteSocio() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/socio/{id}", "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
