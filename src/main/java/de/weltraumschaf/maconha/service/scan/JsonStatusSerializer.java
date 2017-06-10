package de.weltraumschaf.maconha.service.scan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.service.ScanService.ScanStatus;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * JSON based implementation.
 */
final class JsonStatusSerializer implements StatusSerializer {

    private final Gson json = new Gson();
    private final Type genericType = new TypeToken<ArrayList<ScanStatus>>() {
    }.getType();

    @Override
    public void serialize(final Collection<ScanStatus> statuses, final Appendable writer) {
        Validate.notNull(statuses, "statuses");
        Validate.notNull(writer, "writer");
        json.toJson(statuses, writer);
    }

    @Override
    public Collection<ScanStatus> deserialize(final Reader reader) {
        Validate.notNull(reader, "reader");
        final Collection<ScanStatus> statuses = json.fromJson(reader, genericType);
        return statuses == null
            ? Collections.emptyList()
            : Collections.unmodifiableCollection(statuses);
    }

}
