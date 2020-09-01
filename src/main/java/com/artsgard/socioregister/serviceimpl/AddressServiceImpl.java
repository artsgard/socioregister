package com.artsgard.socioregister.serviceimpl;

import com.artsgard.socioregister.service.AddressService;
import com.artsgard.socioregister.service.MapperService;
import com.artsgard.socioregister.DTO.AddressDTO;
import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.AddressModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AddressRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    
    org.slf4j.Logger logger;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MapperService mapperService;

    @Autowired
    private SocioRepository socioRepository;
    
    public AddressServiceImpl() { 
        logger = LoggerFactory.getLogger(SocioServiceImpl.class);
    }

    @Override
    public List<AddressDTO> findAllAddresses() throws ResourceNotFoundException {
        List<AddressModel> addresses = addressRepository.findAll();
        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("no Addresses found!");
        } else {
            List<AddressDTO> list = new ArrayList();
            addresses.forEach((addr) -> {
                AddressDTO addrDTO = mapperService.mapAddressModelToAddressDTO(addr);
                addrDTO.setSocioId(addr.getSocio().getId());
                list.add(addrDTO);
            });
            return list;
        }

    }

    @Override
    public AddressDTO findOneAddressById(Long id) throws ResourceNotFoundException {
        Optional<AddressModel> opAddress = addressRepository.findById(id);
        if (opAddress.isPresent()) {
            return mapperService.mapAddressModelToAddressDTO(opAddress.get());
        } else {
            throw new ResourceNotFoundException("no address found with id: " + id);
        }
    }

    @Override
    public List<AddressDTO> findAddressesBySocioId(Long id) throws ResourceNotFoundException {
        List<AddressModel> addresses = addressRepository.findAddressesBySocioId(id);
        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException("no Addresses found with socio id: " + id);
        } else {
            List<AddressDTO> list = new ArrayList();
            addresses.forEach((addr) -> {
                list.add(mapperService.mapAddressModelToAddressDTO(addr));
            });
            return list;
        }
    }

    @Override
    public AddressDTO saveAddress(AddressDTO addressDTO) throws ResourceNotFoundException {
        Optional<SocioModel> opSocio = socioRepository.findById(addressDTO.getSocioId());   
        if (opSocio.isPresent()) {
            AddressModel address = mapperService.mapAddressDTOToAddressModel(addressDTO);
            address.setSocio(opSocio.get());
            return mapperService.mapAddressModelToAddressDTO(addressRepository.save(address));
        } else {
             throw new ResourceNotFoundException("no socio found with socio id: " + addressDTO.getSocioId());
        }
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO) throws ResourceNotFoundException {
        Optional<AddressModel> opAddress = addressRepository.findById(addressDTO.getId());
       
        if (opAddress.isPresent()) {
            AddressModel repoAddress = opAddress.get();
            if (addressDTO.getStreet() == null) {
                addressDTO.setStreet(repoAddress.getStreet());
            }

            if (addressDTO.getCity() == null) {
                addressDTO.setStreet(repoAddress.getStreet());
            }

            if (addressDTO.getProvince() == null) {
                addressDTO.setProvince(repoAddress.getProvince());
            }

            if (addressDTO.getPostalcode() == null) {
                addressDTO.setPostalcode(repoAddress.getPostalcode());
            }

            if (addressDTO.getDescription() == null) {
                addressDTO.setDescription(repoAddress.getDescription());
            }

            if (addressDTO.getCountry() == null) {
                addressDTO.setCountry(repoAddress.getCountry());
            }

            if (addressDTO.getSocioId() == null) {
                addressDTO.setSocioId(repoAddress.getSocio().getId());
            }
            AddressModel addr = mapperService.mapAddressDTOToAddressModel(addressDTO);
            return mapperService.mapAddressModelToAddressDTO(addressRepository.save(addr));

        } else {
            throw new ResourceNotFoundException("no address found with id: " + addressDTO.getSocioId());
        }
    }

    @Override
    public boolean hasAddress(Long id) throws ResourceNotFoundException {
        return addressRepository.existsById(id);
    }

    @Override
    public void deleteAddressById(Long id) throws ResourceNotFoundException {
        Optional <AddressModel> optAdd = addressRepository.findById(id);
        
        if (optAdd.isPresent()) {
            addressRepository.existsById(id);
        } else {
            //logger.error("no Socio found with id: " + id);
            throw new ResourceNotFoundException("no Socio found with id: " + id);
        }
    }

}
