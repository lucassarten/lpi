package cyan.lpi.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cyan.lpi.service.Service;

@RestController
public class LpiController {
    Service service = new Service();

    // Standard call to a module with a command
    @GetMapping(path = "/{module}/{command}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(@PathVariable("module") String module, @PathVariable("command") String command,
            @RequestParam Map<String, String> params) {
        String response = this.service.run(module, command, params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Call to a module without a command
    @GetMapping(path = "/{module}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(@PathVariable("module") String module,
            @RequestParam Map<String, String> params) {
        String response = this.service.run(module, "", params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Call to neither a module nor a command
    @GetMapping(path = "//", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(
            @RequestParam Map<String, String> params) {
        String response = this.service.run("", "", params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
