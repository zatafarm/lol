package com.mooland.bj;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;


@Service
public class ImageDownloader {

    public String imgdown(String bjurl, String fbjurl) {
        String imageUrl = "https://stimg.afreecatv.com/LOGO/" + fbjurl + "/" + bjurl + "/" + bjurl + ".jpg"; // 다운로드할 이미지 URL
        String savePath = "/home/ubuntu/bjimg/" + bjurl + ".jpg"; // 저장할 경로와 파일 이름

        try {
            // img 폴더가 없으면 생성
            createDirectoryIfNotExists("/home/ubuntu/bjimg");

            // 이미지 다운로드
            downloadImage(imageUrl, savePath);
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static void downloadImage(String imageUrl, String savePath) throws IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            Path path = Path.of(savePath);
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Path.of(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
}
