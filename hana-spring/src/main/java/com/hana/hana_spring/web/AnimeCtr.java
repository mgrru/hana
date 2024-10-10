package com.hana.hana_spring.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hana.hana_spring.anno.LoginValidate;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.AnimeService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Result;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@LoginValidate
public class AnimeCtr {

    @Autowired
    private AnimeService anime_service;

    @Autowired
    private JwtUtil jwt_util;

    @Value("${save-path}")
    private String save_path;

    /**
     * 视频播放链接请求
     * 
     * @param name         视频名称
     * @param episode_name 集名
     * @throws IOException
     */
    @LoginValidate(value = false)
    @GetMapping("animes/{name}/{episode_name}")
    public void display_anime(@PathVariable String name, @PathVariable String episode_name, HttpServletRequest req,
            HttpServletResponse rep)
            throws IOException {
        File file = new File(save_path + name + episode_name + ".mp4");
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

    /**
     * 获取所有动漫信息
     * 
     * @throws JsonProcessingException
     */
    @LoginValidate(value = false)
    @GetMapping("animes")
    public Result get_all_anime() throws JsonProcessingException {
        List<Resource> resources = anime_service.get_all_anime();
        String data = new ObjectMapper().writeValueAsString(resources);

        return Result.success(data);
    }

    /**
     * 上传动漫
     * 
     * @param resources    动漫文件
     * @param cover        封面图
     * @param type         类型 '动画' | '漫画'
     * @param name         动漫名
     * @param episode_name 集名 第一集 | 第二集
     * @param sid          板块id
     * @throws IOException
     */
    @PostMapping("upload")
    public Result add_anime(@RequestParam MultipartFile resources, MultipartFile cover, String type, String name,
            String episode_name, Integer sid, HttpServletRequest req) throws IOException {

        // 检查动漫名称是否冲突
        if (anime_service.get_by_name(name, episode_name) != null) {
            return Result.error();
        }

        // 获取保存目录
        File dir = new File(save_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 获取原始动画名
        String original_name = resources.getOriginalFilename();
        // 设置新动画名（带路径）
        File anime = new File(dir, name + episode_name + ".mp4");
        Resource save_resource = new Resource();

        // 检查上传动画格式
        String suffix = original_name.substring(original_name.lastIndexOf("."));
        if (!suffix.equalsIgnoreCase(".mp4")) {
            log.error("上传视频有问题");
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
            log.error("上传图片有问题");
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
        save_resource.setEpisodeName(episode_name);
        save_resource.setSid(sid);
        save_resource.setPath(anime.getPath());

        save_resource.setUrl(url + "/animes/" + name + "/" + episode_name);
        save_resource.setProcess(false);

        String token = req.getHeader("Authorization");
        Integer id = jwt_util.getLoginUserId(token);
        save_resource.setUid(id);

        // 保存到数据库
        anime_service.add_anime(save_resource);
        return Result.success();
    }

    /**
     * 下架动漫
     * 
     * @param rid 动漫id
     * @return
     */
    @DeleteMapping("deactivate/{rid}")
    public Result del_anime(@PathVariable Integer rid) {
        anime_service.del_anime(rid);
        return Result.success();
    }

    /**
     * 通过审核
     * 
     * @param rid 动漫id
     */
    @PutMapping("approve/{rid}")
    public Result approve_anime(@PathVariable Integer rid) {
        anime_service.process_anime(rid);
        return Result.success();
    }

    /**
     * 不通过审核
     * 
     * @param rid 动漫id
     */
    @PutMapping("reject/{rid}")
    public Result reject_anime(@PathVariable Integer rid) {
        anime_service.del_anime(rid);
        return Result.success();
    }

    /**
     * 用户获取自己上传动漫的接口
     * 
     * @throws JsonProcessingException
     */
    @GetMapping("users/animes")
    public Result get_user_anime(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> resources = anime_service.get_by_user(uid);
        String data = new ObjectMapper().writeValueAsString(resources);
        return Result.success(data);
    }

    /**
     * 删除动漫
     * 
     * @param rid 动漫id
     * @return
     */
    @DeleteMapping("resource/{rid}")
    public Result del_user_anime(@PathVariable Integer rid) {
        anime_service.del_anime(rid);
        return Result.success();
    }

}
