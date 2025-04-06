package com.google.gson.internal.sql;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Adapter générique pour java.sql.Time et java.sql.Timestamp.
 */
@SuppressWarnings("JavaUtilDate")
public final class SqlDateTimeTypeAdapter extends TypeAdapter<Number> {
  
  private final DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a"); // Format pour Time
  private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format pour Timestamp

  public static final TypeAdapterFactory FACTORY =
      new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          if (typeToken.getRawType() == Time.class || typeToken.getRawType() == Timestamp.class) {
            return (TypeAdapter<T>) new SqlDateTimeTypeAdapter();
          }
          return null;
        }
      };

  @Override
  public Number read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    String s = in.nextString();
    synchronized (this) {
      if (s.contains("-") || s.contains(":")) {
        // Tentative de parsing en Timestamp (date + heure)
        try {
          return new Timestamp(timestampFormat.parse(s).getTime());
        } catch (ParseException e) {
          throw new JsonSyntaxException("Erreur de parsing pour le Timestamp : '" + s + "'", e);
        }
      } else {
        // Tentative de parsing en Time (heure uniquement)
        try {
          return new Time(timeFormat.parse(s).getTime());
        } catch (ParseException e) {
          throw new JsonSyntaxException("Erreur de parsing pour le Time : '" + s + "'", e);
        }
      }
    }
  }

  @Override
  public void write(JsonWriter out, Number value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }

    synchronized (this) {
      String formatted;
      if (value instanceof Timestamp) {
        formatted = timestampFormat.format(new Date(value.longValue()));
      } else if (value instanceof Time) {
        formatted = timeFormat.format(new Date(value.longValue()));
      } else {
        out.nullValue();
        return;
      }
      out.value(formatted);
    }
  }
}
