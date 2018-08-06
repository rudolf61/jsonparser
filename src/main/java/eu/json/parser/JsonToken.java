package eu.json.parser;

public enum JsonToken {
    start_document, start_array, end_array, start_object, end_object, field, value, end_document, ignore;
}
