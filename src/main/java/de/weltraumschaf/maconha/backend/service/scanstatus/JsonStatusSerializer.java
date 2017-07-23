package de.weltraumschaf.maconha.backend.service.scanstatus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.ScanService.ScanStatus;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JSON based implementation.
 */
public final class JsonStatusSerializer implements StatusSerializer {

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
            ? new ArrayList<>()
            : statuses;
    }

}
