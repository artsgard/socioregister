 package com.artsgard.socioregister.service;

import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.SocioAssociatedSocio;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * 
 * @author WillemDragstra
 */
@Service
public interface AssociatedSocioService  {  
    SocioAssociatedSocio getAssociatedSocioBySocioIdAndAssociatedSocioId(Long socioId, Long associatedSocioId) throws ResourceNotFoundException;
    SocioAssociatedSocio registerAssociatedSocio(Long socioId, Long associatedSocioId) throws ResourceNotFoundException;
    SocioAssociatedSocio updateStateAssociatedSocio(Long socioId, Long associatedSocioId, boolean state) throws ResourceNotFoundException;
    void deleteAssociatedSocio(Long socioId, Long associatedSocioId) throws ResourceNotFoundException;
}