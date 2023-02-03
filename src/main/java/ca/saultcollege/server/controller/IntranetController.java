package ca.saultcollege.server.controller;

import ca.saultcollege.server.data.Account;
import ca.saultcollege.server.data.content;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.saultcollege.server.security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import ca.saultcollege.server.data.AuthRequest;
import ca.saultcollege.server.data.AuthResponse;
import jakarta.validation.Valid;
import
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IntranetController {
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;

    @GetMapping("/publiccontent")
    public ResponseEntity<String> getPublicContent() {
        return ResponseEntity.ok("getPublicContent().");
    }

    @GetMapping("/staffcontent")
    public ResponseEntity<String> getStaffContent() {
        return ResponseEntity.ok("getStaffContent().");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody Account account) {
        return ResponseEntity.ok("singUp()." + account.getFirstName());
    }

    @GetMapping("/login")
    public ResponseEntity<String> logIn() {
        return ResponseEntity.ok("logIn().");
    }

    @PutMapping("/editpubliccontent")
    public ResponseEntity<String> editPublicContent(@RequestBody content content) {
        return ResponseEntity.ok("editPublicContent()."+content.getPublicContent());
    }
    @PutMapping("/editstaffcontent")
    public ResponseEntity<String> editStaffContent(@RequestBody content content) {
        return ResponseEntity.ok("editStaffContent()."+content.getStaffContent());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> logIn(@RequestBody @Valid AuthRequest request){
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );
            Account account = new Account();
            account.setId(1);
            account.setEmail(authentication.getPrincipal().toString());
            String accessToken = jwtUtil.generateAccessToken(account);
            AuthResponse response = new AuthResponse(account.getEmail(), accessToken);
            return ResponseEntity.ok().body(response);
        } catch( Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
