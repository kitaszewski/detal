package pl.rawinet.detal.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.rawinet.detal.model.OnuProfile;

@Repository
public interface OnuProfileDao extends JpaRepository<OnuProfile, Integer> {

    @Query("Select o from OnuProfile o where o.publicIp=:ip and o.iptv=:tv and o.speed=:speed and o.vlan=:vlan")
    OnuProfile bindOnuLineProfile(@Param("ip") Boolean ip, @Param("tv") Boolean tv, @Param("speed") String speed, @Param("vlan") int vlan);
    @Query("Select o from OnuProfile o where o.publicIp=:ip and o.wifi=:wifi and o.iptv=:tv and o.tvPort=:tvPort and o.type='service' and o.vlan=:vlan")
    OnuProfile bindOnuServiceProfile(@Param("ip") Boolean ip, @Param("tv") Boolean tv, @Param("wifi") Boolean wifi, @Param("tvPort") int tvPort, @Param("vlan") int vlan);

}
