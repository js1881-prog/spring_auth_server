package cpg.back.auth.config.security.service;

public interface IdemPotencyService {

    boolean search(String key);

    void saveKey(String key);

}
