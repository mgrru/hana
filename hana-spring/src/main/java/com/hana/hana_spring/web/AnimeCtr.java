package com.hana.hana_spring.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.AnimeService;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
public class AnimeCtr {

    @Autowired
    private AnimeService anime_service;

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

    @GetMapping("animes")
    public Result get_all_anime() throws JsonProcessingException {
        List<Resource> resources = anime_service.get_all_anime();
        String data = new ObjectMapper().writeValueAsString(resources);

        return Result.success(data);
    }

    @PostMapping("upload")
    public Result add_anime(@RequestParam MultipartFile resources, MultipartFile cover, String type, String name,
            Integer episodes,
            String episode_name, Integer sid, HttpServletRequest req) throws IOException {
        // 获取保存目录
        String user_home = System.getProperty("user.home");
        File dir = new File(user_home + "/Videos/animes");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 获取原始动画名
        String original_name = resources.getOriginalFilename();
        // 设置新动画名（带路径）
        File anime = new File(dir, name + ".mp4");
        Resource save_resource = new Resource();

        // 检查上传动画格式
        String suffix = original_name.substring(original_name.lastIndexOf("."));
        if (!suffix.equalsIgnoreCase(".mp4")) {
            return Result.error();
        } else {
            anime.createNewFile();
            if (anime.isFile()) {
                resources.transferTo(anime);
            }
        }

        // 检查封面图片格式
        String cover_suffix = cover.getOriginalFilename().substring(cover.getOriginalFilename().lastIndexOf("."));
        if (!(cover_suffix.equalsIgnoreCase(".png") || cover_suffix.equalsIgnoreCase(".jpg"))) {
            if (anime.exists()) {
                anime.delete();
            }
            return Result.error();
        }

        // 获取访问地址
        String scheme = req.getScheme(); // "http" or "https"
        String server_name = req.getServerName(); // "localhost" or "example.com"
        int server_port = req.getServerPort(); // 80, 443, etc.
        String context_path = req.getContextPath(); // "/myapp"
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(server_name);
        if ((scheme.equals("http") && server_port != 80) || (scheme.equals("https") && server_port != 443)) {
            url.append(":").append(server_port);
        }
        url.append(context_path);

        // 设置动画保存信息
        save_resource.setType(type);
        save_resource.setCover(cover.getBytes());
        save_resource.setName(name);
        save_resource.setEpisodes(episodes);
        save_resource.setEpisodeName(episode_name);
        save_resource.setSid(sid);
        save_resource.setPath(anime.getPath());

        save_resource.setUrl(url + "/animes/" + name);
        save_resource.setProcess(false);

        save_resource.setUid(1);

        // 保存到数据库
        anime_service.add_anime(save_resource);
        return Result.success();
    }

}
