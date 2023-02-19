package cyan.lpi.module;

import java.util.Map;

public interface Module {
    public default String name() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    default String desc() {
        return this.getClass().getAnnotation(ModuleDef.class).desc();
    }

    default String help(Map<String, String> params) {
        return help();
    }

    public default String help() {
        // get list of methods in this class that have the @Command annotation
        StringBuilder sb = new StringBuilder();
        for (java.lang.reflect.Method method : this.getClass().getMethods()) {
            if (method.isAnnotationPresent(CommandDef.class)) {
                CommandDef command = method.getAnnotation(CommandDef.class);
                sb.append(method.getName() + ": " + command.desc() + "\n");
            }
        }
        return sb.toString();
    }
}
