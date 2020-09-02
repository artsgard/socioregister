package com.artsgard.socioregister.controller;

import com.artsgard.socioregister.model.SocioAssociatedSocio;
import com.artsgard.socioregister.repository.AssociatedSocioRepository;
import com.artsgard.socioregister.service.AssociatedSocioService;
import com.artsgard.socioregister.service.SocioService;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associatedSocio")
public class AssociatedSocioController {
    
    org.slf4j.Logger logger = LoggerFactory.getLogger(AssociatedSocioController.class);

    @Autowired
    private SocioService socioService;
    
    @Autowired
    private AssociatedSocioService associatedSocioService;
    
    @Autowired
    private AssociatedSocioRepository repo;
    
    @GetMapping(path = "/{socioId}/{associatesdSocioId}")
    public ResponseEntity<?> getAssociatedSocio(@PathVariable Long socioId, @PathVariable Long associatesdSocioId) {
        SocioAssociatedSocio asc =  associatedSocioService.getAssociatedSocioBySocioIdAndAssociatedSocioId(socioId, associatesdSocioId);
        return new ResponseEntity<>(asc, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/{socioId}/{associatesdSocioId}")
    public ResponseEntity<?> registerAssociatedSocio(@PathVariable Long socioId, @PathVariable Long associatesdSocioId) {
        associatedSocioService.registerAssociatedSocio(socioId, associatesdSocioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PutMapping(path = "/{socioId}/{associatesdSocioId}/{state}")
    public ResponseEntity<?> updateStateAssociatedSocio(@PathVariable Long socioId, @PathVariable Long associatesdSocioId, @PathVariable boolean state) {
        associatedSocioService.updateStateAssociatedSocio(socioId, associatesdSocioId, state);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping(path = "/{socioId}/{associatedSocioId}")
    public ResponseEntity<?> deleteAssociatedSocio(@PathVariable Long socioId, @PathVariable Long associatedSocioId) {
        associatedSocioService.deleteAssociatedSocio(socioId, associatedSocioId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping(path = "/add")
    public String add() {
        List <SocioAssociatedSocio> list = repo.findAll();
        SocioAssociatedSocio socio = list.get(0);
        socio.setAssociatedSocioState(SocioAssociatedSocio.AssociatedSocioState.DENIED);
        repo.save(socio);
        return socio.getAssociatedSocioState().toString();
    }   
}
