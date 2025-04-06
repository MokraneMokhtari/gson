
package com.google.gson;

import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;

public abstract class BaseGsonBuilder  {
    protected int dateStyle = DateFormat.DEFAULT;
    protected int timeStyle = DateFormat.DEFAULT;
    protected String datePattern;
    
    protected boolean serializeSpecialFloatingPointValues = false;
    protected boolean useJdkUnsafe = true;
    
    // Méthode commune pour vérifier et appliquer les formats de date
    protected int checkDateFormatStyle(int style) {
        if (style < 0 || style > 3) {
            throw new IllegalArgumentException("Style invalide : " + style);
        }
        return style;
    }

    // Méthode commune pour ajouter un sérialiseur spécifique à un type
    protected void registerAdapter(Type type, Object typeAdapter, List<TypeAdapterFactory> factories) {
        if (typeAdapter instanceof JsonSerializer<?> || typeAdapter instanceof JsonDeserializer<?>) {
            TypeToken<?> typeToken = TypeToken.get(type);
            factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter<?>) {
            TypeAdapterFactory factory = TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter<?>) typeAdapter);
            factories.add(factory);
        }
    }
    
    // Méthode pour configurer le format de la date
    public BaseGsonBuilder setDateFormat(int dateStyle, int timeStyle) {
        this.dateStyle = checkDateFormatStyle(dateStyle);
        this.timeStyle = checkDateFormatStyle(timeStyle);
        this.datePattern = null;
        return this;
    }
    
    // Méthode pour configurer les valeurs spéciales en virgule flottante
    public BaseGsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }
    
    
}

