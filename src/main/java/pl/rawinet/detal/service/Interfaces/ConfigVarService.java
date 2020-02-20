package pl.rawinet.detal.service.Interfaces;

public interface ConfigVarService {
    String getVal(String key);
    void setVal(String key, String val);
    void intPlusOne(String key);
}
