package com.artsgard.socioregister.service;

import com.artsgard.socioregister.exception.ResourceNotFoundException;
import com.artsgard.socioregister.model.RoleModel;
import com.artsgard.socioregister.model.SocioModel;
import com.artsgard.socioregister.repository.RoleRepository;
import com.artsgard.socioregister.repository.SocioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private SocioRepository socioRepo;

    @Test
    public void testGetAllRoles() {
        List<RoleModel> roles = roleRepo.findAll();
        assertThat(roles).isNotEmpty();
        assertThat(roles).isNotEmpty().hasSize(2);
    }

    @Test
    public void findRolesBySocioId() throws ResourceNotFoundException {
        Optional<SocioModel> optSocio = socioRepo.findByUsername("js");
        SocioModel socio = optSocio.get();
        RoleModel role = roleRepo.findByName("ROLE_ADMIN");
        List<RoleModel> roles = new ArrayList();
        roles.add(role);
        socio.setSocioRoles(roles);
        socioRepo.save(socio);
        List<RoleModel> rolesDB = roleRepo.findRolesBySocioId(socio.getId());
        assertThat(rolesDB).hasSize(1).isEqualTo(roles);
    }

    @Test
    public void testFindByName() throws ResourceNotFoundException {
        RoleModel role = roleRepo.findByName("ROLE_ADMIN");
        assertThat(role.getName()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    public void testFindRolesBySocioUsername() {
        Optional<SocioModel> optSocio = socioRepo.findByUsername("js");
        SocioModel socio = optSocio.get();
        RoleModel role = roleRepo.findByName("ROLE_ADMIN");
        List<RoleModel> roles = new ArrayList();
        roles.add(role);
        socio.setSocioRoles(roles);
        socioRepo.save(socio);
        List<RoleModel> rls = roleRepo.findRolesBySocioUsername("js");
        assertThat(rls).isNotEmpty().hasSize(1);
    }

}
