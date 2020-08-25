package com.artsgard.socioregister.mock;

import com.artsgard.socioregister.model.SocioModel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WillemDragstra
 */
public final class SocioMock {
    
    private SocioMock() { }
    
    /**
     * 
     * @return single socio
     */
    public static SocioModel generateSocio() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SocioModel socio = new SocioModel(1L, "js", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, null, null);
        socio.setRegisterDate(now);
        socio.setLastCheckinDate(now);
        return socio;
    }
    
    /**
     * 
     * @return mock of 3 socios List format
     */
    public static List<SocioModel> generateSocios() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SocioModel socio1 = new SocioModel(1L, "js", "secret", "Johann Sebastian", "Bach", "jsbach@gmail.com", true, null, null);
        socio1.setRegisterDate(now);
        socio1.setLastCheckinDate(now);
        SocioModel socio2 = new SocioModel(2L, "rw", "secret", "Richard", "Wagner", "rwagner@gmail.com", true, null, null);
        socio2.setRegisterDate(now);
        socio2.setLastCheckinDate(now);
        SocioModel socio3 = new SocioModel(3L, "bb", "secret", "Bela", "Bartok", "bbartok@gmail.com", true, null, null);
        socio3.setRegisterDate(now);
        socio3.setLastCheckinDate(now);
        List<SocioModel> socios = new ArrayList();
        socios.add(socio1);
        socios.add(socio2);
        socios.add(socio3);
        return socios;
    } 
}
