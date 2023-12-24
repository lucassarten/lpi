package cyan.lpi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cyan.lpi.service.CommandService;
import cyan.lpi.service.FileService;

@RestController
public class LpiController {
    @Autowired
    CommandService commandService;

    @Autowired
    FileService fileService;

    // Standard call to a module with a command
    @GetMapping(path = "/api/{module}/{command}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> dispatch(@PathVariable("module") String module,
            @PathVariable("command") String command,
            @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers) {

        String response = commandService.run(module, command, params, headers);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Call to a module without a command
    @GetMapping(path = "/api/{module}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> dispatch(@PathVariable("module") String module,
            @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers) {

        String response = commandService.run(module, "", params, headers);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Call to neither a module nor a command
    @GetMapping(path = { "/api" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> dispatch(
            @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers) {

        String response = commandService.run("", "", params, headers);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PutMapping(path = "/api/{module}/{command}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> dispatch(@PathVariable("module") String module,
            @PathVariable("command") String command,
            @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers,
            @RequestParam("file") MultipartFile file) {

        String response = commandService.run(module, command, params, headers, file);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    // Call to auth module with login command
    @GetMapping(path = "/auth/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> dispatchAuth(@RequestParam Map<String, String> params,
            @RequestHeader Map<String, String> headers) {

        String response = commandService.run("auth", "login", params, headers);

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
