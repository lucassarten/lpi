package cyan.lpi.service;

import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import cyan.lpi.module.Module;

public class Service {
    public String run(String service, String command, Map<String, String> params) {
        if (service.equals("")) {
            return listModules();
        }
        if (command.equals("")) {
            command = "help";
        }
        try {
            Class<?> clazz = Class.forName(
                    "cyan.lpi.module." + service.substring(0, 1).toUpperCase()
                            + service.substring(1));
            Module module = (Module) clazz.getConstructor().newInstance();
            try {
                return (String) clazz.getMethod(command, Map.class)
                        .invoke(module, params);
            } catch (Exception e) {
                return module.help();
            }
        } catch (Exception e) {
            return listModules();
        }
    }

    private String listModules() {
        // Find all classes that implement Module
        Reflections reflections = new Reflections("cyan.lpi.module");
        Set<Class<? extends Module>> classes = reflections.getSubTypesOf(Module.class);
        // List all modules
        StringBuilder sb = new StringBuilder();
        for (Class<? extends Module> clazz : classes) {
            try {
                Module module = clazz.getConstructor().newInstance();
                sb.append(module.name() + ": " + module.desc() + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
