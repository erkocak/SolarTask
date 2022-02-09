package com.example.solartask.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class Converters {

    @TypeConverter
    public Date toDueDate(long timestamp) {
        return new Date(timestamp);
    }

    @TypeConverter
    public long fromDueDate(Date date) {
        return date == null ? null :date.getTime();
    }

    @TypeConverter
    public String fromCategories(List<String> categories) {
        if (categories == null) {
            return (null);
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        String json = gson.toJson(categories, type);
        return json;
    }

    @TypeConverter // note this annotation
    public List<String> toCategoriesList(String categories) {
        if (categories == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> categoriesList = gson.fromJson(categories, type);
        return categoriesList;
    }
}
