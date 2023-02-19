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

    @GetMapping(path = "/{service}/{command}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(@PathVariable("service") String service, @PathVariable("command") String command,
            @RequestParam Map<String, String> params) {
        String response = this.service.run(service, command, params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{service}/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(@PathVariable("service") String service,
            @RequestParam Map<String, String> params) {
        String response = this.service.run(service, "", params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping(path = "//", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> date(
            @RequestParam Map<String, String> params) {
        String response = this.service.run("", "", params);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
