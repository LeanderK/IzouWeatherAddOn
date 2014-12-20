package leanderk.izou.weatheraddon;

import ro.fortsoft.pf4j.PluginWrapper;

/**
 * This class MUST be included in any plugin to work.
 * It shouldn't contain logic, the logic belongs in the AddOn classes
 */
@SuppressWarnings("ALL")
public class ZipFileManager extends intellimate.izou.addon.ZipFileManager{
    public ZipFileManager(PluginWrapper wrapper) {
        super(wrapper);
    }
}
