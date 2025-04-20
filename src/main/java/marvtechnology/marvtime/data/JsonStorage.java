package marvtechnology.marvtime.data;

import org.bukkit.plugin.Plugin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JsonStorage implements StorageProvider {

    private final File file;
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<TimeRecord>>() {}.getType();
    private final Logger logger;

    public JsonStorage(Plugin plugin) {
        this.file = new File(plugin.getDataFolder(), "records.json");
        this.logger = plugin.getLogger();

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    saveAll(new ArrayList<>());
                } else {
                    logger.warning("records.json の作成に失敗しました（既に存在？）");
                }
            } catch (IOException e) {
                logger.warning("records.json の作成中にエラーが発生: " + e.getMessage());
            }
        }
    }

    @Override
    public void saveTime(long year, long day) {
        List<TimeRecord> records = loadAll();
        records.add(new TimeRecord(year, day));
        saveAll(records);
    }

    private List<TimeRecord> loadAll() {
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            List<TimeRecord> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            logger.warning("records.json の読み込み失敗: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveAll(List<TimeRecord> records) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(records, listType, writer);
        } catch (IOException e) {
            logger.warning("records.json の保存失敗: " + e.getMessage());
        }
    }
}
