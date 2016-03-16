package com.rabbitframework.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;


/**
 * Utility class to help call {@link Initializable#init() Initializable.init()} and
 * {@link Destroyable#destroy() Destroyable.destroy()} methods cleanly on any object.
 *
 * @since 0.2
 */
public abstract class LifecycleUtils {

    private static final Logger log = LoggerFactory.getLogger(LifecycleUtils.class);

    public static void init(Object o) throws SecurityException {
        if (o instanceof Initializable) {
            init((Initializable) o);
        }
    }

    public static void init(Initializable initializable) throws SecurityException {
        initializable.init();
    }

    /**
     * Calls {@link #init(Object) init} for each object in the collection.  If the collection is {@code null} or empty,
     * this method returns quietly.
     *
     * @param c the collection containing objects to {@link #init init}.
     * @throws SecurityException if unable to initialize one or more instances.
     * @since 0.9
     */
    public static void init(Collection c) throws SecurityException {
        if (c == null || c.isEmpty()) {
            return;
        }
        for (Object o : c) {
            init(o);
        }
    }

    public static void destroy(Object o) {
        if (o instanceof Destroyable) {
            destroy((Destroyable) o);
        } else if (o instanceof Collection) {
            destroy((Collection) o);
        }
    }

    public static void destroy(Destroyable d) {
        if (d != null) {
            try {
                d.destroy();
            } catch (Throwable t) {
                if (log.isDebugEnabled()) {
                    String msg = "Unable to cleanly destroy instance [" + d + "] of type [" + d.getClass().getName() + "].";
                    log.debug(msg, t);
                }
            }
        }
    }

    /**
     * Calls {@link #destroy(Object) destroy} for each object in the collection.
     * If the collection is {@code null} or empty, this method returns quietly.
     *
     * @param c the collection of objects to destroy.
     * @since 0.9
     */
    public static void destroy(Collection c) {
        if (c == null || c.isEmpty()) {
            return;
        }

        for (Object o : c) {
            destroy(o);
        }
    }
}
