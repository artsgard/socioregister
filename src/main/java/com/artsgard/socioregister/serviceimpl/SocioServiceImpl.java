package com.artsgard.socioregister.serviceimpl;

import com.artsgard.socioregister.DTO.FilterDTO;
import com.artsgard.socioregister.service.MapperService;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.artsgard.socioregister.DTO.SocioDTO;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.service.SocioService;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author WillemDragstra
 */
@Service
public class SocioServiceImpl implements SocioService {

    org.slf4j.Logger logger;

    @Autowired
    private MapperService mapperService;

    @Autowired
    private SocioRepository socioRepo;

    public SocioServiceImpl() {
        logger = LoggerFactory.getLogger(SocioServiceImpl.class);
    }

    /**
     *
     * @return list of all socios
     */
    @Override
    public List<SocioDTO> findAllSocios() throws ResourceNotFoundException {
        List<SocioModel> socios = socioRepo.findAll();
        if (socios.isEmpty()) {
            throw new ResourceNotFoundException("no Socios found!");
        } else {
            List<SocioDTO> socioList = new ArrayList();

            for (SocioModel sco : socios) {
                SocioDTO scDTO = mapperService.mapSocioModelToSocioDTO(sco);
                socioList.add(scDTO);
            }
            return socioList;
        }
    }

    @Override
    public List<SocioDTO> getSociosBySortedPage(int rows, int offset, FilterDTO filter) throws ResourceNotFoundException {
        List<SocioModel> socios = null;

        if (filter.getCountry() != null) {
            socios = socioRepo.getSociosBySortedPageByCountry(rows, offset, filter.getCountry());
        } else if (filter.getLanguage() != null) {
            socios = socioRepo.getSociosBySortedPageByLanguage(rows, offset, filter.getLanguage());
        } else {
            socios = socioRepo.getSociosBySortedPage(rows, offset);
        }

        if (!socios.isEmpty()) {
            List<SocioDTO> socioList = new ArrayList();
            socios.stream().map(sco -> mapperService.mapSocioModelToSocioDTO(sco)).forEachOrdered(scDTO -> {
                socioList.add(scDTO);
            });
            return socioList;
        } else {
            throw new ResourceNotFoundException("no Socios found!");
        }
    }

    /**
     *
     * @param id
     * @return single socio by id
     */
    @Override
    public SocioDTO findSocioById(Long id) throws ResourceNotFoundException {
        Optional<SocioModel> opSocio = socioRepo.findById(id);
        if (opSocio.isPresent()) {
            return mapperService.mapSocioModelToSocioDTO(opSocio.get());
        } else {
            throw new ResourceNotFoundException("no socio found with id: " + id);
        }
    }

    /**
     *
     * @param username
     * @return single socio by username
     */
    @Override
    public SocioDTO findSocioByUsername(String username) throws ResourceNotFoundException {
        Optional<SocioModel> opSocio = socioRepo.findByUsername(username);
        if (opSocio.isPresent()) {
            return mapperService.mapSocioModelToSocioDTO(opSocio.get());
        } else {
            throw new ResourceNotFoundException("no socio found with username: " + username);
        }
    }

    /**
     *
     * @param socioDTO
     * @return save single socio
     */
    @Override
    public SocioDTO saveSocio(SocioDTO socioDTO) {
        SocioModel socio = mapperService.mapSocioDTOToSocioModel(socioDTO);
        socio.setRegisterDate(new Timestamp(System.currentTimeMillis()));
        socio.setLastCheckinDate(new Timestamp(System.currentTimeMillis()));
        return mapperService.mapSocioModelToSocioDTO(socioRepo.save(socio));
    }

    /**
     *
     * @param socioDTO
     * @param id
     * @return update single socio by id
     */
    @Override
    public SocioDTO updateSocio(final SocioDTO socioDTO) throws ResourceNotFoundException {

        Optional<SocioModel> optSocio = socioRepo.findById(socioDTO.getId());
        SocioDTO updatedDTO;
        if (optSocio.isPresent()) {
            SocioModel repoSocio = optSocio.get();
            if (socioDTO.getUsername() == null) {
                socioDTO.setUsername(repoSocio.getUsername());
            }

            if (socioDTO.getFirstName() == null) {
                socioDTO.setFirstName(repoSocio.getFirstName());
            }

            if (socioDTO.getLastName() == null) {
                socioDTO.setLastName(repoSocio.getLastName());
            }

            if (socioDTO.getEmail() == null) {
                socioDTO.setEmail(repoSocio.getEmail());
            }

            if (socioDTO.getSocioLanguages() == null) {
                socioDTO.setEmail(repoSocio.getEmail());
            }
            socioDTO.setRegisterDate(repoSocio.getRegisterDate());
            socioDTO.setLastCheckinDate(repoSocio.getLastCheckinDate());
            socioDTO.setSocioLanguages(repoSocio.getSocioLanguages());
            //     socioDTO.setSocioAddresses(repoSocio.getSocioAddresses());
            socioDTO.setId(socioDTO.getId());

        } else {
             throw new ResourceNotFoundException("no socio found with id: " + socioDTO.getId());
        }
        SocioModel socio = mapperService.mapSocioDTOToSocioModel(socioDTO);
        return mapperService.mapSocioModelToSocioDTO(socioRepo.save(socio));
    }

    /**
     *
     * Delete socio
     *
     * @param id
     */
    @Override
    public void deleteSocioById(Long id) throws ResourceNotFoundException {
        Optional<SocioModel> opSocio = socioRepo.findById(id);

        if (opSocio.isPresent()) {
            socioRepo.deleteById(id);
        } else {
             throw new ResourceNotFoundException("no socio found with id: " + id);
        }
    }

    /**
     *
     * @param id
     * @return boolean has socio
     */
    @Override
    public boolean hasSocioById(Long id) {
        return socioRepo.existsById(id);
    }

    /**
     *
     * @param id
     * @return boolean is socio active
     */
    @Override
    public boolean isSocioActiveById(Long id) throws ResourceNotFoundException {
        Optional<SocioModel> opSocio = socioRepo.findById(id);
        if (opSocio.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
