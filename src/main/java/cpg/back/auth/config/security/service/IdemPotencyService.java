package cpg.back.auth.config.security.service;

public interface IdemPotencyService {

    boolean findKey(String key);

    void saveKey(String key);

}
