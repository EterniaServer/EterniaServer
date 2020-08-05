package br.com.eterniaserver.eterniaserver.objects;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.common.base.Charsets;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class UUIDFetcher {

    private static int PROFILES_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private final Gson gson = new Gson();
    private final List<String> names;
    private final boolean rateLimiting;

    static final HashMap<String, UUID> lookupCache = new HashMap<>();
    static final HashMap<UUID, String> lookupNameCache = new HashMap<>();

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = names;
        this.rateLimiting = rateLimiting;
    }

    public UUIDFetcher(List<String> names) {
        this(names, true);
    }

    public void call(boolean onlinemode) throws Exception {

        if (onlinemode) {
            for (int i = 0; i * PROFILES_PER_REQUEST < names.size(); i++) {
                boolean retry;
                JsonArray array;
                do {
                    HttpURLConnection connection = createConnection();
                    String body = gson.toJson(names.subList(i * PROFILES_PER_REQUEST, Math.min((i + 1) * PROFILES_PER_REQUEST, names.size())));
                    writeBody(connection, body);
                    retry = false;
                    array = null;
                    try {
                        array = gson.fromJson(new InputStreamReader(connection.getInputStream()), JsonArray.class);
                    }
                    catch (Exception e) {

                        if (e.getMessage().contains("429")) {
                            retry = true;
                            if (i == 0 && PROFILES_PER_REQUEST > 1) {
                                PROFILES_PER_REQUEST = Math.max(PROFILES_PER_REQUEST - 5, 1);
                            } else {
                                Thread.sleep(30000);
                            }
                        } else {
                            throw e;
                        }
                    }
                } while (retry);

                for (JsonElement profile : array) {
                    JsonObject jsonProfile = profile.getAsJsonObject();
                    String id = jsonProfile.get("id").getAsString();
                    String name = jsonProfile.get("name").getAsString();
                    UUID uuid = UUIDFetcher.getUUID(id);
                    lookupCache.put(name, uuid);
                    lookupCache.put(name.toLowerCase(), uuid);
                    lookupNameCache.put(uuid, name);
                }
                if (rateLimiting) {
                    Thread.sleep(200L);
                }
            }
        } else {
            for (String name : names) {
                UUID uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
                lookupCache.put(name, uuid);
                lookupCache.put(name.toLowerCase(), uuid);
                lookupNameCache.put(uuid, name);
            }
        }
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }

    private static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    public static UUID getUUIDOf(String name) {
        UUID result = lookupCache.get(name);
        if (result == null) throw new IllegalArgumentException(name);
        return result;
    }

    public static String getNameOf(UUID uuid) {
        String result = lookupNameCache.get(uuid);
        if (result == null) throw new IllegalArgumentException(String.valueOf(uuid));
        return result;
    }

}