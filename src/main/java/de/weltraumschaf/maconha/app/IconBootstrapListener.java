package de.weltraumschaf.maconha.app;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Adds link-tags for "add to homescreen" icons to the head-section of the bootstrap page.
 * <p>
 * Generates links of the type:
 * </p>
 * <pre>
 * {@code
 * <link rel="icon" SIZES="96x96" href="VAADIN/themes/apptheme/icon-96.png">
 * <link rel="apple-touch-icon" SIZES="192x192" href="VAADIN/themes/apptheme/icon-192.png">
 * }
 * </pre>
 */
final class IconBootstrapListener implements BootstrapListener, HasLogger {

    private static final String BASE_URI = "theme://icon-";
    private static final String[] RELS = {"icon", "apple-touch-icon"};
    private static final String EXTENSION = ".png";
    private static final int[] SIZES = {192, 96};

    @Override
    public void modifyBootstrapFragment(final BootstrapFragmentResponse response) {
        // NOP
    }

    @Override
    public void modifyBootstrapPage(final BootstrapPageResponse response) {
        logger().debug("Modify bootstrap page.");
        // Generate link-tags for "add to homescreen" icons
        final Document document = response.getDocument();
        final Element head = document.getElementsByTag("head").get(0);

        for (final String rel : RELS) {
            for (final int size : SIZES) {
                final String iconUri = BASE_URI + size + EXTENSION;
                final String href = response.getUriResolver().resolveVaadinUri(iconUri);
                final String sizes = size + "x" + size;
                final Element element = document.createElement("link");
                element.attr("rel", rel);
                element.attr("sizes", sizes);
                element.attr("href", href);
                head.appendChild(element);
            }
        }

        // Enable these to hide browser controls when app is started from homescreen:
//        Element element = document.createElement("meta");
//        element.attr("name", "mobile-web-app-capable");
//        element.attr("content", "yes");
//        head.appendChild(element);
//
//        element = document.createElement("meta");
//        element.attr("name", "apple-mobile-web-app-capable");
//        element.attr("content", "yes");
//        head.appendChild(element);
    }

}