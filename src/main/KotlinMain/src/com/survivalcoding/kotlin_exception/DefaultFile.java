package com.survivalcoding.kotlin_exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DefaultFileOperations implements FileOperations {

    @Override
    public void copyFile(File source, File destination) throws FileNotFoundException, IOException {
        // 원본 파일이 존재하지 않는 경우 예외 발생
        if (source == null || !source.exists()) {
            throw new FileNotFoundException("원본 파일이 존재하지 않습니다: " + (source != null ? source.getPath() : "null"));
        }

        // 스트림을 이용한 파일 복사 (버퍼 사용)
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}