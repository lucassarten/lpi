package cyan.lpi.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import cyan.lpi.module.Module;

public class Service {
    /**
     * Run a command in a module
     * 
     * @param service
     * @param command
     * @param params
     * @return
     */
    public String run(String module, String command, Map<String, String> params) {
        // if no command is specified, run the help command for the module
        if (command.equals("")) {
            command = "help";
        }
        // otherwise try to execute
        try {
            // get the module class
            Class<?> clazz = Class.forName(
                    "cyan.lpi.module." + module.substring(0, 1).toUpperCase()
                            + module.substring(1));
            // get the module instance
            Module moduleDef = (Module) clazz.getConstructor().newInstance();
            try {
                // get the command method
                Method commandDef = clazz.getMethod(command, Map.class);
                // get the commands parameters
                String[] commandParams = commandDef.getAnnotation(cyan.lpi.module.CommandDef.class).params();
                // validate parameters
                if (commandParams.length != params.size()) {
                    // no parameters given, run the help command
                    if (params.isEmpty()) {
                        return moduleDef.help(command);
                    }
                    return "Invalid parameters\n" + moduleDef.help(command);
                }
                // execute command
                try {
                    return (String) commandDef.invoke(moduleDef, params);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Invalid parameters\n" + moduleDef.help(command);
                }
            } catch (Exception e) {
                // if the command does not exist, run the help command
                return moduleDef.help();
            }
        } catch (Exception e) {
            // if the module does not exist, list all modules
            return listModules();
        }
    }

    /**
     * List all modules and their descriptions
     * 
     * @return
     */
    private String listModules() {
        // find all classes that implement Module
        Reflections reflections = new Reflections("cyan.lpi.module");
        Set<Class<? extends Module>> classes = reflections.getSubTypesOf(Module.class);
        // for each class
        StringBuilder sb = new StringBuilder();
        for (Class<? extends Module> clazz : classes) {
            try {
                // get the module instance
                Module module = clazz.getConstructor().newInstance();
                // build help string
                sb.append(module.name() + " - " + module.desc() + "\n");
            } catch (Exception e) {
                // we have a problem
                e.printStackTrace();
            }
        }
        // remove trailing newline
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
