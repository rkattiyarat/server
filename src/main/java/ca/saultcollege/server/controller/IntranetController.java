package ca.saultcollege.server.controller;

import ca.saultcollege.server.data.*;
import ca.saultcollege.server.repositories.*;
import ca.saultcollege.server.security.RefreshTokenUtil;
import org.aspectj.weaver.ast.Test;
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

import java.util.List;
import ca.saultcollege.server.repositories.AccountRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IntranetController {
    @Autowired AuthenticationManager authManager;
    @Autowired JwtTokenUtil jwtUtil;
    @Autowired RegistryRepository registryRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired RefreshTokenUtil refreshTokenUtil;
    @Autowired MessageRepository messageRepository;
    @Autowired PostRepository postRepository;


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

    private String getRegistry(String registryKey) {
        //Find the record for the registry entry based on the supplied key
        List<Registry> registryEntries = registryRepository.findByRegistryKey(registryKey);
        Registry registryEntry = new Registry();
        if (registryEntries.size() == 0) {
            return "";
        }
        return registryEntries.get(0).getRegistryValue();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createAccount(@RequestBody Account signUpFormData) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(signUpFormData.getPassword());
        signUpFormData.setPassword(password);
        Account savedAccount = accountRepository.save(signUpFormData);
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
    @GetMapping("/publiccontent")
    public ResponseEntity<String> getPublicContent() {
        return ResponseEntity.ok(getRegistry("public_content"));
    }

    @PutMapping("/publiccontent")
    public ResponseEntity<Boolean> savePublicContent(@RequestBody @Valid Registry content) {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/staffcontent")
    public ResponseEntity<String> getStaffContent() {
        return ResponseEntity.ok(getRegistry("staff_content"));
    }

    @PutMapping("/staffcontent")
    public ResponseEntity<Boolean> saveStaffContent(@RequestBody @Valid Registry content)
    {
        Boolean result = updateRegistry(content.getRegistryKey(), content.getRegistryValue());
        return ResponseEntity.ok(result);
    }
//
//    @PostMapping("/messages")
//    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
//        Message saveMessage = messageRepository.save(message);
//        return ResponseEntity.ok(saveMessage);
//    }

//    @GetMapping("/messages")
//    public ResponseEntity<String> getMessages() {
//        return ResponseEntity.ok(getMessages("rose"));
//    }

    //get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageRepository.findAll();
        return ResponseEntity.ok(messageRepository.findBySender("rose"));
    }

    @PutMapping("/messages")
    public ResponseEntity<Boolean> saveMessage(@RequestBody @Valid Message message) {
        messageRepository.save(message);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/posts")
    public ResponseEntity<String> getContent() {
        return ResponseEntity.ok(postRepository.findByAuthorId(1).getContent());
    }

    @GetMapping("/posts/id")
    public ResponseEntity<Integer> getAuthorId() {
        return ResponseEntity.ok(postRepository.findByAuthorId(1).getAuthorId());
    }




    @PutMapping("/posts")
    public ResponseEntity<Boolean> saveContent(@RequestBody @Valid Post post) {
        postRepository.save(post);
        return ResponseEntity.ok(true);
    }

}
