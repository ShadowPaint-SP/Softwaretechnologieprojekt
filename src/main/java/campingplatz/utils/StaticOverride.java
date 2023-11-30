package campingplatz.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * This decorator technically does nothing.
 * It is still included, because static Methods cannot be marked
 * as @Override. For the symmetry with @Override, this decorator allows
 * you to mark a static method as overriding some static method from the
 * base class. Unlike the @Override annotation, this is not enforced.
 * */
@Target(ElementType.METHOD)
public @interface StaticOverride {



}
