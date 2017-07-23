package de.weltraumschaf.maconha.backend.model;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Super type for file extension enums.
 * <p>
 * File extension is the string portion after the last dot of a file name.
 * </p>
 */
public enum FileExtension {

    NONE("", ""),
    // Text types:
    HTML("html", "text/html"),
    HTM("htm", "text/html"),
    HTML_WITH_SERVER_SIDE_INCLUDES("shtml", "text/html"),
    CASCADING_STYLE_SHEET("css", "text/css"),
    XML("xml", "text/xml"),
    MATHML("mml", "text/mathml"),
    PLAIN_TEXT("txt", "text/plain"),
    JAVA_APPLICATION_DESCRIPTOR("jad", "text/vnd.sun.j2me.app-descriptor"),
    WIRELESS_MARKUP_LANGUAGE("wml", "text/vnd.wap.wml"),
    HTML_COMPONENTS("htc", "text/x-component"),
    // Video types:
    AUDIO_VIDEO_INTERLEAVE("avi", "video/avi"),
    DIVX_ENCODED_MOVIE("divx", "video/divx"),
    ITUNES_VIDEO("m4v", "video/mp4"),
    MATROSKA_VIDEO("mkv", "video/x-matroska"),
    APPLE_QUICKTIME_MOVIE("mov", "video/quicktime"),
    MPEG4_VIDEO("mp4", "video/mp4"),
    MPEG("mpeg", "video/mpeg"),
    MPG("mpg", "video/mpeg"),
    OGG_VIDEO("ogm", "video/ogg"),
    WINDOWS_MEDIA_VIDEO("wmv", "video/wmv"),
    XVID_ENCODED_VIDEO("xvid", "video/x-xvid"),
    TRANSPORT_STREAM("ts", "video/mp2t"),
    WEBM("webm", "video/webm"),
    FLASH("flv", "video/x-flv"),
    MULTIPLE_IMAGE_NETWORK_GRAPHICS("mng", "video/x-mng"),
    ADVANCED_STREAM_REDIRECTOR("asx", "video/x-ms-asf"),
    WINDOWS_AUDIO_VIDEO("asf", "video/x-ms-asf"),
    // Audio types:
    MID("mid", "audio/midi"),
    MIDI("midi", "audio/midi"),
    KARAOKE_MIDI("kar", "audio/midi"),
    MP3("mp3", "audio/mpeg"),
    OGG_AUDIO("ogg", "audio/ogg"),
    MP4_AUDIO("m4a", "audio/x-m4a"),
    REAL_AUDIO("ra", "audio/x-realaudio"),
    // Image types:
    GIF("gif", "image/gif"),
    JPEG("jpeg", "image/jpeg"),
    JPG("jpg", "image/jpeg"),
    PNG_IMAGE_FILE("png", "image/png"),
    TIF("tif", "image/tiff"),
    TIFF("tiff", "image/tiff"),
    WIRELESS_APPLICATION_PROTOCOL_BITMAP("wbmp", "image/vnd.wap.wbmp"),
    ICON("ico", "image/x-icon"),
    JPEG_NETWORK_GRAPHICS("jng", "image/x-jng"),
    BITMAP("bmp", "image/x-ms-bmp"),
    SCALABLE_VECTOR_GRAPHICS("svg", "image/svg+xml"),
    COMPRESSED_SCALABLE_VECTOR_GRAPHICS("svgz", "image/svg+xml"),
    WEBP("webp", "image/webp"),
    // Application types:
    REAL_MEDIA("rm", "application/vnd.rn-realmedia"),
    JAVASSCRIPT("js", "application/javascript"),
    ATOM_FEED("atom", "application/atom+xml"),
    RSS_FEED("rss", "application/rss+xml"),
    WEB_OPEN_FONT_FORMAT("woff", "application/font-woff"),
    JAVA_ARCHVE("jar", "application/java-archive"),
    JAVA_WEB_ARCHIVE("war", "application/java-archive"),
    JAVA_ENETERPRISE_APPLICATION_ARCHIVE("ear", "application/java-archive"),
    JSON_TEXT("json", "application/json"),
    MACOS_BINHEX("hqx", "application/mac-binhex40"),
    MS_WORD_DOCUMENT("doc", "application/msword"),
    PDF("pdf", "application/pdf"),
    POSTSCRIPT("ps", "application/postscript"),
    ENCAPSULATED_POSTSCRIPT("eps", "application/postscript"),
    ADOBE_ILLUSTRATOR("ai", "application/postscript"),
    RICHT_TEXT_FORMAT("rtf", "application/rtf"),
    MEDIA_PLAYLIST_UTF8("m3u8", "application/vnd.apple.mpegurl"),
    MS_EXCEL("xls", "application/vnd.ms-excel"),
    EMBEDDED_OPEN_TYPE("eot", "application/vnd.ms-fontobject"),
    MS_POWERPOINT("ppt", "application/vnd.ms-powerpoint"),
    KEYHOLE_MARKUP_LANGUAGE("kml", "application/vnd.google-earth.kml+xml"),
    COMPRESSED_KEYHOLE_MARKUP_LANGUAGE("kmz", "application/vnd.google-earth.kmz"),
    SEVEN_ZIP_COMPRESSED("7z", "application/x-7z-compressed"),
    JAVA_ARCHIVE_DIFF("jardiff", "application/x-java-archive-diff"),
    JAVA_NETWORK_LAUNCHING_PROTOCOL("jnlp", "application/x-java-jnlp-file"),
    PERL_SOURCE("pl", "application/x-perl"),
    PERL_MODULE("pm", "application/x-perl"),
    RAR_COMPRESSED("rar", "application/x-rar-compressed"),
    REDHAT_PACKAGE_MANAGER("rpm", "application/x-redhat-package-manager"),
    SELF_EXTRACTING_ARCHIVE("sea", "application/x-sea"),
    SHOCKWAVE_FLASH("swf", "application/x-shockwave-flash"),
    STUFFIT_COMPRESSED("sit", "application/x-stuffit"),
    TCL_SOURCE("tcl", "application/x-tcl"),
    TK_SOURCE("tk", "application/x-tcl"),
    X509_DER_BINARY_CERTIFICATE("der", "application/x-x509-ca-cert"),
    X509_PEM_TEXT_CERTIFICATE("pem", "application/x-x509-ca-cert"),
    X509_CERTIFICATE("crt", "application/x-x509-ca-cert"),
    FIREFOX_CROSS_PLATTFORM_INSTALL("xpi", "application/x-xpinstall"),
    XHTML("xhtml", "application/xhtml+xml"),
    XML_SHAREABLE_PLAYLIST("xspf", "application/xspf+xml"),
    ZIP_COMPRESSED("zip", "application/zip"),
    BINARY("bin", "application/octet-stream"),
    MS_WINDOWS_EXECUTABLE("exe", "application/octet-stream"),
    MS_WINDOWS_DYNAMIC_LINK_LIBRARY("dll", "application/octet-stream"),
    DEBIAN_PACKAGE_MANAGER("deb", "application/octet-stream"),
    MACOS_DISK_IMAGE("dmg", "application/octet-stream"),
    ISO9660_IMAGE("iso", "application/octet-stream"),
    BINARY_RAW_IMAGE("img", "application/octet-stream"),
    MS_WINDOWS_INSTALLER("msi", "application/octet-stream"),
    MS_WINDOWS_PATCH("msp", "application/octet-stream"),
    MS_WINDOWS_MERGE_PACKAGE("msm", "application/octet-stream"),
    MS_WORD_COMPRESSED_XML("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    MS_EXCEL_COMPRESSED_XML("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    MS_POWERPOINT_COMPRESSED_XML("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    private static final Map<String, FileExtension> LOOKUP;

    static {
        final Map<String, FileExtension> tmp = new HashMap<>();

        for (final FileExtension ext : values()) {
            tmp.put(ext.getExtension().toLowerCase(), ext);
        }

        LOOKUP = Collections.unmodifiableMap(tmp);
    }

    private final String extension;
    private final String mimeType;

    FileExtension(final String extension, final String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    /**
     * Get the literal extension.
     *
     * @return never {@code null} nor empty
     */
    public String getExtension() {
        return extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    /**
     * Checks if the enum is the given extension.
     * <p>
     * This method ignores case of extension.
     * </p>
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if it is the extension, else {@code false}
     */
    public boolean isExtension(final String fileExtension) {
        return extension.equalsIgnoreCase(fileExtension);
    }

    /**
     * Whether the given string is a known extension.
     *
     * @param fileExtension may be {@code null} or empty
     * @return {@code true} if known, else {@code false}
     */
    public static boolean hasValue(final String fileExtension) {
        return null != fileExtension && LOOKUP.containsKey(fileExtension.toLowerCase());

    }

    /**
     * Finds the extension for a given string representation.
     * <p>
     * May return {@link #NONE} if the extension is not known.
     * </p>
     *
     * @param fileExtension may be {@code null} or empty
     * @return never {@code null}
     */
    public static FileExtension forValue(final String fileExtension) {
        if (hasValue(fileExtension)) {
            return LOOKUP.get(fileExtension.toLowerCase());
        }

        return NONE;
    }

    /**
     * Extracts the extension from a given path.
     *
     * @param input may be {@code null}
     * @return never {@code null}, as default the {@link #getExtension() extension} of {@link #NONE}
     * @see #extractExtension(java.lang.String)
     */
    public static String extractExtension(final Path input) {
        if (null == input) {
            return NONE.getExtension();
        }

        return extractExtension(input.toString());
    }

    /**
     * Extracts the extension from a given path.
     * <p>
     * As extension the string portion after the last dot is recognized. the returned extension is without that point.
     * </p>
     *
     * @param input may be {@code null} or empty
     * @return never {@code null}, as default {@link #getExtension() extension} of {@link #NONE}
     */
    public static String extractExtension(final String input) {
        if (null == input || input.trim().isEmpty()) {
            return NONE.getExtension();
        }

        return input.substring(input.lastIndexOf('.') + 1);
    }
}
