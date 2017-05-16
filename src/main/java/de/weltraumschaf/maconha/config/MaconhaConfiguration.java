package de.weltraumschaf.maconha.config;

import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.core.BCrypt;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

/**
 * Holds custom application configuration.
 * <p>
 * Class must not be final for Springs sake.
 * </p>
 */
@ConfigurationProperties(prefix = "maconha")
public class MaconhaConfiguration {

    @NotBlank
    @SuppressWarnings("unused")
    private String version;

    @SuppressWarnings("unused")
    private boolean debug;

    @SuppressWarnings("FieldCanBeLocal")
    private int passwordStrength = 10;

    @NotBlank
    @SuppressWarnings("FieldCanBeLocal")
    private String title = "¡Fuder par à paz do mundo!";

    @NotBlank
    @SuppressWarnings("unused")
    private String bindir;

    @NotBlank
    @SuppressWarnings("unused")
    private String homedir;

    /**
     * Get the applications semantic version.
     *
     * @return never {@code null} nor blank
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the applications semantic version.
     *
     * @param version must not be {@code null} nor blank
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Whether debugging is enabled.
     *
     * @return {@code true} if enabled, else {@code false}
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Whether to enable debugging.
     *
     * @param debug {@code true} to enable, else {@code false}
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    /**
     * Get the password strength.
     *
     * @return number between {@link BCrypt#MIN_LOG_ROUNDS} and {@link BCrypt#MAX_LOG_ROUNDS}
     */
    public int getPasswordStrength() {
        if (passwordStrength < BCrypt.MIN_LOG_ROUNDS) {
            return BCrypt.MIN_LOG_ROUNDS;
        }

        if (passwordStrength > BCrypt.MAX_LOG_ROUNDS) {
            return BCrypt.MAX_LOG_ROUNDS;
        }

        return passwordStrength;
    }

    /**
     * Set the password strength.
     *
     * @param passwordStrength must be between {@link BCrypt#MIN_LOG_ROUNDS} and {@link BCrypt#MAX_LOG_ROUNDS}
     */
    public void setPasswordStrength(final int passwordStrength) {
        Validate.greaterThanOrEqual(passwordStrength, BCrypt.MIN_LOG_ROUNDS, "passwordStrength");

        if (passwordStrength > BCrypt.MAX_LOG_ROUNDS) {
            throw new IllegalArgumentException(
                "Parameter 'passwordStrength' must not be greater than " + BCrypt.MAX_LOG_ROUNDS + "!");
        }

        this.passwordStrength = passwordStrength;
    }

    /**
     * Get the main title of application.
     *
     * @return never {@code null} nor blank
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the main title of application.
     *
     * @param title must not be {@code null} nor blank
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Get the main distributions binary executables directory.
     *
     * @return never {@code null} nor blank
     */
    public String getBindir() {
        return bindir;
    }

    /**
     * Set the main distributions binary executables directory.
     *
     * @param bindir must not be {@code null} nor blank
     */
    public void setBindir(final String bindir) {
        this.bindir = bindir;
    }

    /**
     * Get the main data directory.
     *
     * @return never {@code null} nor blank
     */
    public String getHomedir() {
        return homedir;
    }

    /**
     * Set the main data directory.
     *
     * @param homedir must not be {@code null} nor blank
     */
    public void setHomedir(final String homedir) {
        this.homedir = homedir;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MaconhaConfiguration)) {
            return false;
        }

        final MaconhaConfiguration that = (MaconhaConfiguration) o;
        return debug == that.debug &&
            passwordStrength == that.passwordStrength &&
            Objects.equals(version, that.version) &&
            Objects.equals(title, that.title) &&
            Objects.equals(bindir, that.bindir) &&
            Objects.equals(homedir, that.homedir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, debug, passwordStrength, title, bindir, homedir);
    }

    @Override
    public String toString() {
        return "MaconhaConfiguration{" +
            "version='" + version + '\'' +
            ", debug=" + debug +
            ", passwordStrength=" + passwordStrength +
            ", title='" + title + '\'' +
            ", bindir='" + bindir + '\'' +
            ", homedir='" + homedir + '\'' +
            '}';
    }
}
