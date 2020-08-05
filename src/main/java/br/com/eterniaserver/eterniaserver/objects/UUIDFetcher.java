package br.com.eterniaserver.eterniaserver.objects;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;

public class UUIDFetcher implements Callable<Map<String, UUID>> {

    private static final boolean onlineMode = Bukkit.getOnlineMode();

    private static final double PROFILES_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft";
    private final JSONParser jsonParser = new JSONParser();
    private final List<String> names;
    private final boolean rateLimiting;

    static final HashMap<String, UUID> lookupCache = new HashMap<>();
    static final HashMap<UUID, String> lookupNameCache = new HashMap<>();

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = ImmutableList.copyOf(names);
        this.rateLimiting = rateLimiting;
    }

    private static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
    }

    public static void putUUIDAndName(UUID uuid, String playerName) {
        lookupCache.put(playerName, uuid);
        lookupNameCache.put(uuid, playerName);
    }

    public static UUID getUUIDOf(String name) {
        UUID result = lookupCache.get(name);
        if (result == null) {
            if (onlineMode) {
                try {
                    return new UUIDFetcher(Collections.singletonList(name), true).call().get(name);
                } catch(Exception e) {
                    return null;
                }
            } else {
                result = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
            }
            lookupCache.put(name, result);
            lookupNameCache.put(result, name);
        }
        return result;
    }

    public static String getNameOf(UUID uuid) {
        String result = lookupNameCache.get(uuid);
        if (result == null) {
            result = Bukkit.getOfflinePlayer(uuid).getName();
            lookupCache.put(result, uuid);
            lookupNameCache.put(uuid, result);
        }
        return result;
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes(StandardCharsets.UTF_8));
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

    @Override
    public Map<String, UUID> call() throws Exception {
        Map<String, UUID> uuidMap = new HashMap<>();
        int requests = (int) Math.ceil(names.size() / PROFILES_PER_REQUEST);
        for (int i = 0; i < requests; i++) {
            HttpURLConnection connection = createConnection();
            String body = JSONArray.toJSONString(names.subList(i * 100, Math.min((i + 1) * 100, names.size())));
            writeBody(connection, body);
            JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            for (Object profile : array) {
                JSONObject jsonProfile = (JSONObject) profile;
                String id = (String) jsonProfile.get("id");
                String name = (String) jsonProfile.get("name");
                UUID uuid = UUIDFetcher.getUUID(id);
                uuidMap.put(name, uuid);
                lookupCache.put(name, uuid);
                lookupNameCache.put(uuid, name);
            }
            if (rateLimiting && i != requests - 1) {
                Thread.sleep(10L);
            }
        }
        return uuidMap;
    }

}