package com.oup.cachedemo.sbcachedemo.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.oup.cachedemo.sbcachedemo.bo.EmployeeBO;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.SerializationUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository(value = "rocksdbrepo")
public class RocksDBRepository {

    private final static String FILE_NAME = "spring-boot-db";
    File baseDir;
    RocksDB db;
    @Value("${rocksdb.base.path}")
    private String baseDirectoryForRocksDB;

    @PostConstruct // execute after the application starts.
    void initialize() {
        RocksDB.loadLibrary();
        final Options options = new Options();
        options.setCreateIfMissing(true);
        baseDir = new File(baseDirectoryForRocksDB, FILE_NAME);
        try {
            Files.createDirectories(baseDir.getParentFile().toPath());
            Files.createDirectories(baseDir.getAbsoluteFile().toPath());
            db = RocksDB.open(options, baseDir.getAbsolutePath());
            log.info("RocksDB initialized");
        } catch (IOException | RocksDBException e) {
            log.error("Error initializng RocksDB. Exception: '{}', message: '{}'", e.getCause(), e.getMessage(), e);
        }
    }

    public synchronized boolean save(Integer key, Object value) {
        log.info("saving value '{}' with key '{}'", value, key);
        try {

            //byte[] bytes = db.get(key.toString().getBytes());
            //if (bytes != null)
            //    value = SerializationUtils.deserialize(bytes);

            db.put(key.toString().getBytes(), SerializationUtils.serialize(value));
        } catch (RocksDBException e) {
            log.error("Error saving entry. Cause: '{}', message: '{}'", e.getCause(), e.getMessage());
            return false;
        }
        return true;
    }

    public synchronized Optional<Object> find(Integer key) {
        Object value = null;
        try {
            byte[] bytes = db.get(key.toString().getBytes());
            if (bytes != null)
                value = SerializationUtils.deserialize(bytes);
        } catch (RocksDBException e) {
            log.error(
                    "Error retrieving the entry with key: {}, cause: {}, message: {}",
                    key,
                    e.getCause(),
                    e.getMessage());
        }
        log.info("finding key '{}' returns '{}'", key, value);
        return value != null ? Optional.of(value) : Optional.empty();
    }

    public synchronized ArrayList<EmployeeBO> findAll() {
        ArrayList<EmployeeBO> allEmployees = new ArrayList<>();
        RocksIterator rocksIterator = db.newIterator();
        try {

            rocksIterator.seekToFirst();
            log.info("Start iterating from first........");
            while (rocksIterator.isValid()) {
                allEmployees.add((EmployeeBO) SerializationUtils.deserialize(rocksIterator.value()));
                rocksIterator.next();
            }
        } finally {
            rocksIterator.close();
        }

        return allEmployees;
    }

}
