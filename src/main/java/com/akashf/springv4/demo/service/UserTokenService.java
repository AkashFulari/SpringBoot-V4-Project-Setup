package com.akashf.springv4.demo.service;

import com.akashf.springv4.demo.dto.SetUserReq;
import com.akashf.springv4.demo.dto.SaveTokenReq;
import com.akashf.springv4.demo.dto.TokenResp;
import com.akashf.springv4.demo.model.UserToken;
import com.akashf.springv4.demo.repository.UserTokenRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserTokenService {
    private final UserTokenRepo repo;

    public UserTokenService(UserTokenRepo repo) {
        this.repo = repo;
    }

    public UserToken createUser(SetUserReq user) throws Exception {
        return setUserToken(new UserToken());
    }

    public UserToken updateUser(Long id, SetUserReq user) throws Exception {
        UserToken u = repo.findById(id).orElseThrow(() -> new RuntimeException("UserToken not found"));
        return setUserToken(u);
    }

    public Page<UserToken> getAllUsers(Pageable pageable) {
        return repo.findAll(pageable);
    }

    public UserToken getById(Long id) throws Exception {
        UserToken u = repo.findById(id).orElseThrow(() -> new RuntimeException("UserToken not found"));
        return u;
    }

    public UserToken getByLatest() throws Exception {
        UserToken u = repo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("UserToken not found"));

        return u;
    }

    /**
     * Save a new notification token with device type.
     * Creates a new UserToken record in the database.
     *
     * @param req SaveTokenReq containing token and device type
     * @return TokenResp with saved token details
     * @throws Exception if token is null or empty
     */
    public TokenResp saveToken(SaveTokenReq req) throws Exception {
        if (req == null || req.getToken() == null || req.getToken().isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        UserToken userToken = new UserToken();
        userToken.setToken(req.getToken());
        userToken.setDevice(req.getDevice() != null ? req.getDevice() : com.akashf.springv4.demo.enums.DeviceType.WEB);

        UserToken saved = repo.save(userToken);

        return convertToResp(saved);
    }

    /**
     * Fetch the latest notification token from the database.
     * Returns it as TokenResp if found.
     *
     * @return TokenResp containing the latest token
     * @throws Exception if no token is found
     */
    public TokenResp getLatestToken() throws Exception {
        UserToken u = repo.findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No token found in database"));

        return convertToResp(u);
    }

    /**
     * Convert UserToken entity to TokenResp DTO.
     */
    private TokenResp convertToResp(UserToken userToken) {
        return new TokenResp(
                userToken.getId(),
                userToken.getToken(),
                userToken.getDevice()
        );
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }

    private UserToken setUserToken(UserToken u) throws Exception {
        return repo.save(u);
    }
}