package de.weltraumschaf.maconha.backend.service.scanreport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.weltraumschaf.commons.validate.Validate;
import de.weltraumschaf.maconha.backend.service.scanreport.reporting.Report;

import java.io.Reader;

/**
 * JSON based implementation.
 */
final class JsonReportSerializer implements ReportSerializer {
    private final Gson json = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void serialize(final Report report, final Appendable writer) {
        Validate.notNull(report, "report");
        Validate.notNull(writer, "writer");
        json.toJson(report, writer);
    }

    @Override
    public Report deserialize(final Reader reader) {
        Validate.notNull(reader, "reader");
        final Report report = json.fromJson(reader, Report.class);
        return report == null
            ? Report.EMPTY
            : report;
    }
}
