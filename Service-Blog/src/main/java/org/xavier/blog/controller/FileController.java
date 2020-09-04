package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.FileInfo;
import org.xavier.blog.domain.RequestBODeleteFileMultiple;
import org.xavier.common.exception.PropertiesRuntimeException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-5
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class FileController extends HyggeWriterController {
    @Value("${file.upload.path}")
    String filePath;

    @PostMapping("/main/file/upload")
    public ResponseEntity<?> fileUpload(@RequestParam("files") ArrayList<MultipartFile> filesList) {
        try {
            ArrayList<String> pathList = new ArrayList();
            for (MultipartFile temp : filesList) {
                String path = filePath + temp.getOriginalFilename();
                pathList.add(path);
                File file = new File(path);
                if (file.exists()) {
                    throw new PropertiesRuntimeException("File(" + temp.getOriginalFilename() + ") was duplicate.");
                }
                file.createNewFile();
                temp.transferTo(file);
            }
            return success(pathList);
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.FAIL_TO_UPLOAD_FILE.getErrorCod(), e.getMessage());
        }
    }

    @GetMapping("/main/file")
    public ResponseEntity<?> queryFile() {
        try {
            // 文件路径+文件名称
            ArrayList<FileInfo> files = new ArrayList();
            File file = new File(filePath);
            File[] tempList = file.listFiles();
            if (tempList != null) {
                for (int i = 0; i < tempList.length; i++) {
                    File currentFile = tempList[i];
                    if (currentFile.isFile()) {
                        FileInfo item = new FileInfo();
                        String fileFullPath = tempList[i].toString();
                        item.setFileName(fileFullPath.substring(filePath.length()));
                        item.setFileSize(new BigDecimal(currentFile.length()).divide(FileInfo.byteToMb));
                        files.add(item);
                    }
                }
            }
            return success(files);
        } catch (Throwable e) {
            logger.error("查询文件失败", e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.FAIL_TO_QUERY_FILE.getErrorCod(), e.getMessage());
        }
    }

    @DeleteMapping("/main/file")
    public ResponseEntity<?> deleteFile(@RequestBody RequestBODeleteFileMultiple requestBO) {
        try {
            for (String fileName : requestBO.getFileNameList()) {
                File file = new File(filePath + fileName);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
            return success();
        } catch (Throwable e) {
            logger.error("删除文件失败", e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.FAIL_TO_DELETE_FILE.getErrorCod(), e.getMessage());
        }
    }

}