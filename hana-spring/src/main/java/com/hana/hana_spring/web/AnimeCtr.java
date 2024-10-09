package com.hana.hana_spring.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
public class AnimeCtr {
    @GetMapping("animes/{name}")
    public void display_anime(@PathVariable String name, HttpServletRequest req, HttpServletResponse rep)
            throws IOException {
        String user_home = System.getProperty("user.home");
        File file = new File(user_home + "/Videos/animes/" + name + ".mp4");
        if (!file.exists()) {
            rep.getOutputStream().close();
            return;
        }
        String range = req.getHeader("Range");
        long len_start = 0;
        if (range != null && range.length() > 7) {
            range = range.substring(6, range.length() - 1);
            len_start = Long.parseLong(range);
        }

        int size = 1048576;
        rep.setHeader("Content-Range",
                "bytes " + len_start + "-"
                        + ((file.length() - len_start - 2 < size) ? file.length() - 1 : len_start + size - 1) + "/"
                        + file.length());
        rep.setContentType("video/mp4");
        rep.setStatus(HttpStatus.PARTIAL_CONTENT.value());
        RandomAccessFile random_access_file = new RandomAccessFile(file, "rw");
        random_access_file.seek(len_start);
        byte[] buffer = new byte[size];
        int len = random_access_file.read(buffer);
        if (len != -1) {
            rep.getOutputStream().write(buffer, 0, len);
        }
        random_access_file.close();
        rep.getOutputStream().flush();
        rep.getOutputStream().close();
    }
}
