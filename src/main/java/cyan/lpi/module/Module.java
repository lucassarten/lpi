package cyan.lpi.module;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Interface for a module, all modules must implement this interface
 */
public interface Module {
    /**
     * Get the name of the module
     * 
     * @return
     */
    public default String name() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Get the description of the module
     * 
     * @return
     */
    default String desc() {
        return this.getClass().getAnnotation(ModuleDef.class).desc();
    }

    /**
     * Route commands with params
     * 
     * @param params
     * @return
     */
    default String help(Map<String, String> params) {
        return help();
    }

    /**
     * Builds help string for a command
     * 
     * @param commandName
     * @return
     */
    public default String help(String commandName) {
        try {
            // get the commands method
            Method command = this.getClass().getMethod(commandName, Map.class);
            // get the @CommandDef annotation
            CommandDef commandDef = command.getAnnotation(CommandDef.class);
            String params = commandDef.params().length == 0 ? ""
                    : Arrays.toString(commandDef.params());
            // build help string
            return commandName + " " + params + " - "
                    + commandDef.desc();
        } catch (Exception e) {
            e.printStackTrace();
            return "No help available :(";
        }
    }

    /**
     * Builds help string for all commands in this module
     * 
     * @return
     */
    public default String help() {
        // get list of methods in this class that have the @CommandDef annotation
        StringBuilder sb = new StringBuilder();
        for (Method command : this.getClass().getMethods()) {
            // call help() for each command
            if (command.isAnnotationPresent(CommandDef.class)) {
                sb.append(this.help(command.getName()) + "\n");
            }
        }
        // remove trailing newline
        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
