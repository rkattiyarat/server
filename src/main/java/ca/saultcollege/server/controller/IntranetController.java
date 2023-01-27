package ca.saultcollege.server.controller;

import ca.saultcollege.server.data.Account;
import ca.saultcollege.server.data.content;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IntranetController {
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

}
