package com.artsgard.socioregister.serviceimpl;

import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.SocioAssociatedSocio;
import com.artsgard.socioregister.model.SocioAssociatedSocio.AssociatedSocioState;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.AssociatedSocioRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import com.artsgard.socioregister.service.AssociatedSocioService;
import java.sql.Timestamp;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author artsgard
 */
@Service
public class AssociatedSocioServiceImpl implements AssociatedSocioService {

    org.slf4j.Logger logger;

    @Autowired
    private SocioRepository socioRepò;

    @Autowired
    private AssociatedSocioRepository associatedSocioRepo;

    public AssociatedSocioServiceImpl() {
        logger = LoggerFactory.getLogger(SocioServiceImpl.class);
    }

    @Override
    public SocioAssociatedSocio getAssociatedSocioBySocioIdAndAssociatedSocioId(Long socioId, Long associatedSocioId) throws ResourceNotFoundException {

        Optional<SocioAssociatedSocio> optAsc = associatedSocioRepo.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatedSocioId);
        if (optAsc.isPresent()) {
            return optAsc.get();
        } else {
            throw new ResourceNotFoundException("No socio present with " + socioId);
        }
    }

    @Override
    public SocioAssociatedSocio registerAssociatedSocio(Long socioId, Long associatedSocioId) {
        Optional<SocioModel> optSocio = socioRepò.findById(socioId);
        Optional<SocioModel> optAssociatedSocio = socioRepò.findById(associatedSocioId);
        isPresent(optSocio, optAssociatedSocio, socioId, associatedSocioId);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        SocioAssociatedSocio assSocio = new SocioAssociatedSocio(socioId, associatedSocioId, optSocio.get(), optAssociatedSocio.get(), AssociatedSocioState.PENDING, now);

        return associatedSocioRepo.save(assSocio);
    }

    @Override
    public SocioAssociatedSocio updateStateAssociatedSocio(Long socioId, Long associatedSocioId, boolean state) {
        Optional<SocioModel> optSocio = socioRepò.findById(socioId);
        Optional<SocioModel> optAssociatedSocio = socioRepò.findById(associatedSocioId);
        isPresent(optSocio, optAssociatedSocio, socioId, associatedSocioId);

        AssociatedSocioState changedState;
        if (state) {
            changedState = AssociatedSocioState.ACCEPTED;
        } else {
            changedState = AssociatedSocioState.DENIED;
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        SocioAssociatedSocio assSocio = new SocioAssociatedSocio(socioId, associatedSocioId, optSocio.get(), optAssociatedSocio.get(), changedState, now);
        return associatedSocioRepo.save(assSocio);
    }

    @Override
    public void deleteAssociatedSocio(Long socioId, Long associatedSocioId) {
        Optional<SocioModel> optSocio = socioRepò.findById(socioId);
        Optional<SocioModel> optAssociatedSocio = socioRepò.findById(associatedSocioId);
        isPresent(optSocio, optAssociatedSocio, socioId, associatedSocioId);
        associatedSocioRepo.deleteAssociatedSocio(socioId, associatedSocioId);
    }

    private void isPresent(Optional<SocioModel> optSocio, Optional<SocioModel> optAssociatedSocio, Long socioId, Long associatedSocioId) {
        if (!optSocio.isPresent()) {
            throw new ResourceNotFoundException("No socio present with " + socioId);
        }

        if (!optAssociatedSocio.isPresent()) {
            throw new ResourceNotFoundException("No associated socio present with " + associatedSocioId);
        }
    }
}
