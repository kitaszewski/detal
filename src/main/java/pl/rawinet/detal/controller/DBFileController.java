package pl.rawinet.detal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.rawinet.detal.model.DBFile;
import pl.rawinet.detal.service.DBFileServiceImpl;

@RestController
public class DBFileController {
    @Autowired
    DBFileServiceImpl dbFileService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable int fileId){
        DBFile file = dbFileService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getData()));
    }

}
