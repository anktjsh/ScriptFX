/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

import java.io.File;
import static java.lang.String.format;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 *
 * @author Aniket
 */
public class Service {

    public static final String DESKTOP = "Desktop";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";

    private static final Logger LOG = Logger.getLogger(Service.class.getName());

    private static Service instance;

    public static synchronized Service get() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private final ServiceLoader<Provider> serviceLoader;
    private Provider provider;

    private Service() {
        serviceLoader = ServiceLoader.load(Provider.class);
        try {
            Iterator<Provider> iterator = serviceLoader.iterator();
            while (iterator.hasNext()) {
                if (provider == null) {
                    provider = iterator.next();
                    LOG.info(format("Using Provider: %s", provider.getClass().getName()));
                } else {
                    LOG.info(format("This Provider is ignored: %s", iterator.next().getClass().getName()));
                }
            }
        } catch (Exception e) {
            throw new ServiceConfigurationError("Failed to access + ", e);
        }
        if (provider == null) {
            LOG.severe("No Provider implementation could be found!");
        }
    }

    public File getFile(String s) {
        if (provider != null) {
            return provider.getFile(s);
        }
        return null;
    }

    public void sendEmail(File s) {
        if (provider != null) {
            provider.sendEmail(s);
        }
    }

}
