package de.weltraumschaf.maconha.model;

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
    // Video types:
    AUDIO_VIDEO_INTERLEAVE("avi", "video/avi"),
    DIVX_ENCODED_MOVIE_FILE("divx", "video/divx"),
    ITUNES_VIDEO_FILE("m4v", "video/mp4"),
    MATROSKA_VIDEO_FILE("mkv", "video/x-matroska"),
    APPLE_QUICKTIME_MOVIE("mov", "video/quicktime"),
    MPEG4_VIDEO_FILE("mp4", "video/mp4"),
    MPEG_MOVIE("mpeg", "video/mpeg"),
    MPEG_VIDEO_FILE("mpg", "video/mpeg"),
    OGG_MEDIA_FILE("ogm", "video/ogg"),
    REAL_MEDIA_FILE("rm", "application/vnd.rn-realmedia"),
    WINDOWS_MEDIA_VIDEO_FILE("wmv", "video/wmv"),
    XVID_ENCODED_VIDEO_FILE("xvid", "video/x-xvid"),
    // New
    HTML_TEXT_FIlE("html", "text/html"),
    HTM_TEXT_FILE("htm", "text/html"),
    HTML_WITH_SERVER_SIDE_INCLUDES_TEXT_FILE("shtml", "text/html"),
    CASCADING_STYLE_SHEET_TET_FILE("css", "text/css"),
    XML_TEXT_FILE("xml", "text/xml"),
    GIF_IMAGE_FILE("gif", "image/gif"),
    JPEG_IMAGE_FILE("jpeg", "image/jpeg"),
    JPE_IMAGE_FILE("jpg", "image/jpeg"),
    JAVASSCRIPT_TEXT_FILE("js", "application/javascript"),
    ATOM_FEED_TEXT_FILE("atom", "application/atom+xml"),
    RSS_FEED_TEXT_FILE("rss", "application/rss+xml"),
    MATHML_TEXT_FILE("mml", "text/mathml"),
    PLAIN_TEXT_FILE("txt", "text/plain"),
    JAVA_APPLICATION_DESCRIPTOR_FILE("jad", "text/vnd.sun.j2me.app-descriptor"),
    WIRELESS_MARKUP_LANGUAGE_FILE("wml", "text/vnd.wap.wml"),
    HTML_COMPONENTS_FILE("htc", "text/x-component"),
    PNG_IMAGE_FILE("png", "image/png"),
    TIF_IMAGE_FILE("tif", "image/tiff"),
    TIFF_IMAGE_FILE("tiff", "image/tiff"),
    WIRELESS_APPLICATION_PROTOCOL_BITMAP_IMAGE("wbmp", "image/vnd.wap.wbmp"),
    ICON_IMAGE_FILE("ico", "image/x-icon"),
    JPEG_NETWORK_GRAPHICS_IMAGE("jng", "image/x-jng"),
    BITMAP_IMAGE("bmp", "image/x-ms-bmp"),
    SCALABLE_VECTOR_GRAPHICS_IMAGE("svg", "image/svg+xml"),
    COMPRESSED_SCALABLE_VECTOR_GRAPHICS_IMAGE("svgz", "image/svg+xml"),
    WEBP_IMAGE("webp", "image/webp"),
    WEB_OPEN_FONT_FORMAT_FILE("woff", "application/font-woff"),
    JAVA_ARCHVE_FILE("jar", "application/java-archive"),
    JAVA_WEB_ARCHIVE_FILE("war", "application/java-archive"),
    JAVA_ENETERPRISE_APPLICATION_ARCHIVE_FILE("ear", "application/java-archive"),
    JSON_TEXT_FILE("json", "application/json"),
    MACOS_BINHEX_FILE("hqx", "application/mac-binhex40"),
    MS_WORD_DOCUMENT_FILE("doc", "application/msword"),
    PDF_FILE("pdf", "application/pdf"),
    POSTSCRIPT_FILE("ps", "application/postscript"),
    ENCAPSULATED_POSTSCRIPT_FILE("eps", "application/postscript"),
    ADOBE_ILLUSTRATOR_FILE("ai", "application/postscript"),
    RICHT_TEXT_FORMAT_FILE("rtf", "application/rtf"),
    MEDIA_PLAYLIST_UTF8_FILE("m3u8", "application/vnd.apple.mpegurl"),
    MS_EXCEL_FILE("xls", "application/vnd.ms-excel"),
    EMBEDDED_OPEN_TYPE_FILE("eot", "application/vnd.ms-fontobject"),
    MS_POWERPOINT("ppt", "application/vnd.ms-powerpoint"),
    KEYHOLE_MARKUP_LANGUAGE("kml", "application/vnd.google-earth.kml+xml"),
    COMPRESSED_KEYHOLE_MARKUP_LANGUAGE("kmz", "application/vnd.google-earth.kmz"),
    SEVEN_ZIP_COMPRESSED("7z", "application/x-7z-compressed"),
    JAVA_ARCHIVE_DIFF("jardiff", "application/x-java-archive-diff"),
    JAVA_NETWORK_LAUNCHING_PROTOCOL("jnlp", "application/x-java-jnlp-file"),
    PERL_SOURCE_FILE("pl", "application/x-perl"),
    PERL_MODULE_FILE("pm", "application/x-perl"),
    RAR_COMPRESSED("rar", "application/x-rar-compressed"),
    REDHAT_PACKAGE_MANAGER_FILE("rpm", "application/x-redhat-package-manager"),
    SELF_EXTRACTING_ARCHIVE("sea", "application/x-sea"),
    SHOCKWAVE_FLASH("swf", "application/x-shockwave-flash"),
    STUFFIT_COMPRESSED("sit", "application/x-stuffit"),
    TCL_SOURCE("tcl", "application/x-tcl"),
    TK_SOURCE("tk", "application/x-tcl"),
    X509_DER_BINARY_CERTIFICATE("der", "application/x-x509-ca-cert"),
    X509_PEM_TEXT_CERTIFICATE("pem", "application/x-x509-ca-cert"),
    X509_CERTIFICATE("crt", "application/x-x509-ca-cert"),
    FIREFOX_CROSS_PLATTFORM_INSTALL("xpi", "application/x-xpinstall"),
    XHTML_TEXT_FILE("xhtml", "application/xhtml+xml"),
    XML_SHAREABLE_PLAYLIST("xspf", "application/xspf+xml"),
    ZIP_COMPRESSED("zip", "application/zip"),
    BINARY_FILE("bin", "application/octet-stream"),
    MS_WINDOWS_EXECUTABLE("exe", "application/octet-stream"),
    MS_WINDOWS_DYNAMIC_LINK_LIBRARY("dll", "application/octet-stream"),
    DEBIAN_PACKAGE_MANAGER_FILE("deb", "application/octet-stream"),
    MACOS_DISK_IMAGE_FILE("dmg", "application/octet-stream"),
    ISO9660_IMAGE_FILE("iso", "application/octet-stream"),
    BINARY_RAW_IMAGE("img", "application/octet-stream"),
    MS_WINDOWS_INSTALLER("msi", "application/octet-stream"),
    MS_WINDOWS_PATCH("msp", "application/octet-stream"),
    MS_WINDOWS_MERGE_PACKAGE("msm", "application/octet-stream"),
    MS_WORD_COMPRESSED_XML("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    MS_EXCEL_COMPRESSED_XML("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    MS_POWERPOINT_COMPRESSED_XML("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    MID_MIDI_FILE("mid", "audio/midi"),
    MIDI_FILE("midi", "audio/midi"),
    KARAOKE_MIDI_FILE("kar", "audio/midi"),
    MP3_AUDIO("mp3", "audio/mpeg"),
    OGG_CONTAINER("ogg", "audio/ogg"),
    MP4_AUDIO("m4a", "audio/x-m4a"),
    REAL_AUDIO("ra", "audio/x-realaudio"),
    TRANSPORT_STREAM_VIDEO("ts", "video/mp2t"),
    MP4_VIDEO("mp4", "video/mp4"),
    MPEG_VIDEO("mpeg", "video/mpeg"),
    MPG_VIDEO("mpg", "video/mpeg"),
    QUICKTIME_VIDEO("mov", "video/quicktime"),
    WEBM_VIDEO("webm", "video/webm"),
    FLASH_VIDEO("flv", "video/x-flv"),
    M4V_VIDEO("m4v", "video/x-m4v"),
    MULTIPLE_IMAGE_NETWORK_GRAPHICS_VIDEO("mng", "video/x-mng"),
    ADVANCED_STREAM_REDIRECTOR_VIDEO("asx", "video/x-ms-asf"),
    WINDOWS_AUDIO_VIDEO("asf", "video/x-ms-asf"),
    WINDOWS_MEDIA_VIDEO("wmv", "video/x-ms-wmv");

    private static final Map<String, FileExtension> LOOKUP;

    static {
        final Map<String, FileExtension> tmp = new HashMap<>();

        for (final FileExtension ext : values()) {
            tmp.put(ext.getExtension().toLowerCase(), ext);
        }

        LOOKUP = Collections.unmodifiableMap(tmp);
    }

    private final String extension;
    // https://www.sitepoint.com/web-foundations/mime-types-complete-list/
    // https://gstreamer.freedesktop.org/documentation/plugin-development/advanced/media-types.html
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

    public static FileExtension forValue(final String fileExtension) {
        if (hasValue(fileExtension)) {
            return LOOKUP.get(fileExtension.toLowerCase());
        }

        throw new IllegalArgumentException(String.format("Unknown extension '%s'!", fileExtension));
    }

    /**
     * Extracts the extension from a given path.
     *
     * @see #extractExtension(java.lang.String)
     * @param input may be {@code null}
     * @return never {@code null}, as default {@link #NONE#getExtension()}
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
     * @return never {@code null}, as default {@link #NONE#getExtension()}
     */
    public static String extractExtension(final String input) {
        if (null == input || input.trim().isEmpty()) {
            return NONE.getExtension();
        }

        return input.substring(input.lastIndexOf('.') + 1);
    }
}
