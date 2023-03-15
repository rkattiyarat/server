package ca.saultcollege.server.controller;

import ca.saultcollege.server.data.*;
import ca.saultcollege.server.repositories.AccountRepository;
import ca.saultcollege.server.security.RefreshTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ca.saultcollege.server.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import ca.saultcollege.server.repositories.RegistryRepository;
import java.util.List;
import ca.saultcollege.server.repositories.AccountRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IntranetController {

    private Boolean updateRegistry(String registryKey, String registryValue) {
    //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);
        Registry registryEntry = new Registry();
        if (registryEntries.size() == 0) {
            registryEntry.setRegistryKey(registryKey);
        } else {
            registryEntry = registryEntries.get(0);
        }
        registryEntry.setRegistryValue(registryValue);
    //Update the registry table with new value
        registryRepository.save(registryEntry);
        return true;
    }

    @Autowired AuthenticationManager authManager;
    @Autowired JwtTokenUtil jwtUtil;
    @Autowired RegistryRepository registryRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired RefreshTokenUtil refreshTokenUtil;

    @PutMapping("/publiccontent")
    public ResponseEntity<Boolean> savePublicContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }
    @PutMapping("/staffcontent")
    public ResponseEntity<Boolean> saveStaffContent(@RequestBody @Valid Registry content)
    {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }
    @GetMapping("/publiccontent")
    public ResponseEntity<String> getPublicContent() {
        return ResponseEntity.ok(getRegistry("public_content"));
    }

    @GetMapping("/staffcontent")
    public ResponseEntity<String> getStaffContent() {
        return ResponseEntity.ok(getRegistry("staff_content"));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(signUpFormData.getPassword());
        signUpFormData.setPassword(password);
        Account savedAccount = accountRepository.save(signUpFormData);
//        Account newAccount = new Account(signUpFormData.getEmail(), password);
//        Account savedAccount = accountRepository.save(newAccount);
        return ResponseEntity.ok("createAccount(): " + signUpFormData.getEmail());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> logIn(@RequestBody @Valid AuthRequest request){
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//            Account account = new Account();
//            account.setId(1);
//            account.setEmail(authentication.getPrincipal().toString());
            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(account);
            RefreshToken refreshToken = refreshTokenUtil.createRefreshToken(account.getId());
            AuthResponse response = new AuthResponse(account.getEmail(), accessToken,refreshToken.getToken());
            return ResponseEntity.ok().body(response);
        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        try {
            RefreshToken refreshToken = refreshTokenUtil.findByToken(requestRefreshToken);
            if (refreshToken != null) {
                if (refreshTokenUtil.verifyExpiration(refreshToken)) {
                    Account account = accountRepository.getById(refreshToken.getAccount().getId());
                    String token = jwtUtil.generateAccessToken(account);
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                } else {
                    throw new Exception("RefreshTokenExpired");
                }
            }
        } catch(Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String getRegistry(String registryKey) {
    //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);
        Registry registryEntry = new Registry();
        if (registryEntries.size() == 0) {
            return "";
        }
        return registryEntries.get(0).getRegistryValue();
    }
}
