package cyan.lpi.module;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

/**
 * Annotation for a command
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface CommandDef {
    /**
     * description of the command
     */
    public String desc() default "";

    /**
     * parameters of the command
     */
    public String[] params() default {};
}
