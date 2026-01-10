package codigo.resources;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;


/**
 * Simple JSON service for reading and writing JSON files without external dependencies
 */

public class JsonService {
    
    /**
     * Write an object to a JSON file
     * @param object The object to write
     * @param filePath The path to the JSON file
     * @throws IOException if writing fails
     */
    public <T> void writeToFile(T object, String filePath) throws IOException {
        File file = new File(filePath);
        // Create parent directories if they don't exist
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        
        String json = toJson(object);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }
    }
    
    /**
     * Read an object from a JSON file
     * @param filePath The path to the JSON file
     * @param clazz The class type to deserialize to
     * @return The deserialized object
     * @throws IOException if reading fails
     */
    public <T> T readFromFile(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return fromJson(content, clazz);
    }

    public <T> T readFromClasspath(String path, Class<T> clazz) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found in classpath: " + path);
            }
            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            return fromJson(content, clazz); // chama o teu método interno de parsing [file:1]
        }
    }


    

    
    /**
     * Read a list of objects from a JSON file
     * @param filePath The path to the JSON file
     * @param clazz The class type of list elements
     * @return The deserialized list
     * @throws IOException if reading fails
     */
    public <T> List<T> readListFromFile(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return fromJsonList(content, clazz);
    }
    
    /**
     * Check if a JSON file exists
     * @param filePath The path to check
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * Delete a JSON file
     * @param filePath The path to the file to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }
    
    // Simple JSON serialization (works for basic objects)
    private String toJson(Object obj) {
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(toJson(list.get(i)));
            }
            sb.append("]");
            return sb.toString();
        }
        
        return objectToJson(obj);
    }
    
    private String objectToJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String) return "\"" + escapeJson((String) obj) + "\"";
        if (obj instanceof Number) return obj.toString();
        if (obj instanceof Boolean) return obj.toString();
        if (obj instanceof Enum) return "\"" + ((Enum<?>) obj).name() + "\"";
        
        // For custom objects, use reflection (simplified)
        StringBuilder sb = new StringBuilder("{");
        try {
            java.lang.reflect.Field[] fields = obj.getClass().getDeclaredFields();
            boolean first = true;
            for (java.lang.reflect.Field field : fields) {
                // Skip synthetic fields, serialVersionUID, and static fields
                if (field.isSynthetic() || 
                    field.getName().equals("serialVersionUID") ||
                    java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                
                try {
                    field.setAccessible(true);
                    if (!first) sb.append(",");
                    sb.append("\"").append(field.getName()).append("\":");
                    Object value = field.get(obj);
                    sb.append(objectToJson(value));
                    first = false;
                } catch (IllegalAccessException | SecurityException e) {
                    // Skip fields that can't be accessed
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error serializing object", e);
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    // Simple JSON deserialization (basic implementation)
    @SuppressWarnings("unchecked")
    private <T> T fromJson(String json, Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return (T) json.substring(1, json.length() - 1); // Remove quotes
            }
            
            T instance = clazz.getDeclaredConstructor().newInstance();
            
            // Parse JSON object
            json = json.trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1);
                
                Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*([^,}]+)");
                Matcher matcher = pattern.matcher(json);
                
                while (matcher.find()) {
                    String fieldName = matcher.group(1);
                    String fieldValue = matcher.group(2).trim();
                    
                    try {
                        java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
                        // Skip synthetic fields and serialVersionUID
                        if (field.isSynthetic() || field.getName().equals("serialVersionUID")) {
                            continue;
                        }
                        
                        field.setAccessible(true);
                        Object value = parseValue(fieldValue, field.getType());
                        field.set(instance, value);
                    } catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
                        // Field not found or can't be accessed, skip
                    }
                }
            }
            
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> fromJsonList(String json, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
            
            // Simple array parsing - split by objects
            int braceCount = 0;
            StringBuilder current = new StringBuilder();
            
            for (char c : json.toCharArray()) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                
                if (c == ',' && braceCount == 0) {
                    if (current.length() > 0) {
                        list.add(fromJson(current.toString().trim(), clazz));
                        current = new StringBuilder();
                    }
                } else {
                    current.append(c);
                }
            }
            
            if (current.length() > 0) {
                list.add(fromJson(current.toString().trim(), clazz));
            }
        }
        
        return list;
    }
    
    private Object parseValue(String value, Class<?> type) {
    value = value.trim();
    
    if (value.equals("null")) return null;
    
    // ← NOVO: Suporte para List<String> / List<Enum>
    if (type == List.class || (type.isAssignableFrom(List.class) && type.getComponentType() == String.class)) {
        List<String> list = new ArrayList<>();
        if (value.startsWith("[") && value.endsWith("]")) {
            // Parse simples de array de strings: ["FRIO","PERIGOSO"]
            String arrayContent = value.substring(1, value.length() - 1);
            String[] items = arrayContent.split(",");
            for (String item : items) {
                String cleanItem = item.trim();
                if (cleanItem.startsWith("\"") && cleanItem.endsWith("\"")) {
                    cleanItem = cleanItem.substring(1, cleanItem.length() - 1).trim();
                }
                if (!cleanItem.isEmpty()) {
                    list.add(cleanItem);
                }
            }
        }
        return list;
    }
    
    if (type == String.class) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
    
    if (type == int.class || type == Integer.class) {
        return Integer.parseInt(value);
    }
    
    if (type == double.class || type == Double.class) {
        return Double.parseDouble(value);
    }
    
    if (type == boolean.class || type == Boolean.class) {
        return Boolean.parseBoolean(value);
    }
    
    // Handle enums
    if (type.isEnum()) {
        String enumValue = value;
        if (enumValue.startsWith("\"") && enumValue.endsWith("\"")) {
            enumValue = enumValue.substring(1, enumValue.length() - 1);
        }
        return Enum.valueOf((Class<Enum>) type, enumValue);
    }

    return value;
    }


    /** Lê do ficheiro JSON o array que está no campo de topo `fieldName`
 *  e converte para List<clazz> usando o fromJsonList existente. */
public <T> List<T> readListFieldFromFile(String filePath, String fieldName, Class<T> clazz) throws IOException {
    String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    String arrayJson = extractTopLevelArray(content, fieldName);
    return fromJsonList(arrayJson, clazz); // usa o teu parser de listas [file:1]
}

/** Extrai o array JSON (incluindo [ ]) do campo fieldName ao nível raiz. */
private String extractTopLevelArray(String json, String fieldName) throws IOException {
    String key = "\"" + fieldName + "\"";
    int keyPos = json.indexOf(key);
    if (keyPos < 0) throw new IOException("Campo não encontrado: " + fieldName);

    int colon = json.indexOf(':', keyPos + key.length());
    if (colon < 0) throw new IOException("JSON inválido (sem ':') no campo: " + fieldName);

    int start = json.indexOf('[', colon);
    if (start < 0) throw new IOException("Campo não é array: " + fieldName);

    int depth = 0;
    boolean inString = false;
    for (int i = start; i < json.length(); i++) {
        char c = json.charAt(i);

        if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) inString = !inString;
        if (inString) continue;

        if (c == '[') depth++;
        else if (c == ']') {
            depth--;
            if (depth == 0) {
                return json.substring(start, i + 1);
            }
        }
    }
    throw new IOException("Array não fechado no campo: " + fieldName);
}
}