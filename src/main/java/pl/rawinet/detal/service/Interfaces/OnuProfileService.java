package pl.rawinet.detal.service.Interfaces;

import pl.rawinet.detal.model.OnuProfile;
import pl.rawinet.detal.model.Subscription;

public interface OnuProfileService {
    public OnuProfile bindOnuLineProfile(Subscription s);
    public OnuProfile bindOnuServiceProfile(Subscription s);
}
