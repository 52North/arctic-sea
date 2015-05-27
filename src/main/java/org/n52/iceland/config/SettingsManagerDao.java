package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.ds.ConnectionProviderException;

/**
 *
 * @author Christian Autermann
 */
public interface SettingsManagerDao {
    /**
     * @return all saved setting values
     *
     * @throws ConnectionProviderException
     */
    Set<SettingValue<?>> getSettingValues()
            throws ConnectionProviderException;

    /**
     * Returns the value of the specified setting or {@code null} if it does not
     * exist.
     * <p/>
     *
     * @param key
     *            the key
     * <p/>
     * @return the value
     *
     * @throws ConnectionProviderException
     */
    SettingValue<?> getSettingValue(String key)
            throws ConnectionProviderException;

    /**
     * Deletes the setting with the specified key.
     * <p/>
     *
     * @param key
     *            the key
     *
     * @throws ConnectionProviderException
     */
    void deleteSettingValue(String key)
            throws ConnectionProviderException;

    /**
     * Saves the setting value.
     * <p/>
     *
     * @param setting
     *                the value
     *
     * @throws ConnectionProviderException
     */
    void saveSettingValue(SettingValue<?> setting)
            throws ConnectionProviderException;

    void deleteAll()
            throws ConnectionProviderException;

}
