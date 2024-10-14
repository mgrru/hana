package com.hana.hana_spring.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.hana.hana_spring.anno.Validate;
import com.hana.hana_spring.entity.Resource;
import com.hana.hana_spring.service.AnimeService;
import com.hana.hana_spring.utils.JwtUtil;
import com.hana.hana_spring.utils.Oss;
import com.hana.hana_spring.utils.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@CrossOrigin("*")
@Validate
public class AnimeCtr {

    @Autowired
    private AnimeService anime_service;

    @Autowired
    private JwtUtil jwt_util;

    @Autowired
    private Oss oss;

    @Value("${save-path}")
    private String save_path;

    @Operation(summary = "视频播放链接")
    @Validate(login = false)
    @GetMapping("animes/{name}/{episode_name}")
    public void display_anime(@PathVariable String name, @PathVariable String episode_name, HttpServletRequest req,
            HttpServletResponse rep)
            throws IOException {
        File file = new File(save_path + "/" + name + episode_name + ".mp4");
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

        Resource resource = anime_service.get_by_name_episode(name, episode_name);
        anime_service.add_views(resource.getId());

        random_access_file.close();
        rep.getOutputStream().flush();
        rep.getOutputStream().close();
    }

    @Operation(summary = "获取所有动漫信息")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @Validate(login = false)
    @GetMapping("animes")
    public ResponseEntity<String> get_all_anime() throws JsonProcessingException {
        List<Resource> resources = anime_service.get_all_anime();
        String data = new ObjectMapper().writeValueAsString(resources);

        return Result.success(data);
    }

    @Operation(summary = "上传动漫")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schemaProperties = {
            @SchemaProperty(name = "resources", schema = @Schema(type = "string", format = "binary", name = "resources")),
            @SchemaProperty(name = "cover", schema = @Schema(type = "string", format = "binary", name = "cover")),
            @SchemaProperty(name = "type", schema = @Schema(name = "type")),
            @SchemaProperty(name = "name", schema = @Schema(name = "name")),
            @SchemaProperty(name = "episode_name", schema = @Schema(name = "episode_name")),
            @SchemaProperty(name = "sid", schema = @Schema(type = "integer", format = "int32", name = "sid")),
    }))
    @PostMapping("upload")
    public ResponseEntity<String> add_anime(
            MultipartFile resources,
            MultipartFile cover,
            String type,
            String name,
            String episode_name,
            Integer sid,
            HttpServletRequest req)
            throws IOException {
        // 检查动漫名称是否冲突
        if (anime_service.get_by_name_episode(name, episode_name) != null) {
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
        String cover_suffix = cover.getOriginalFilename()
                .substring(cover.getOriginalFilename().lastIndexOf("."));
        if (!(cover_suffix.equalsIgnoreCase(".png")
                || cover_suffix.equalsIgnoreCase(".jpg")
                || cover_suffix.equalsIgnoreCase(".jpeg"))) {
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
        String cover_url = oss.upload(cover);
        save_resource.setCover(cover_url);
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

    @Operation(summary = "下架动漫")
    @Parameters(@Parameter(name = "rid", description = "要下架的动漫id"))
    @Validate(auth = true)
    @DeleteMapping("deactivate/{rid}")
    public ResponseEntity<String> del_anime(@PathVariable Integer rid) {
        anime_service.del_anime(rid);
        return Result.success();
    }

    @Operation(summary = "通过审核")
    @Parameters(@Parameter(name = "rid", description = "要审核的动漫id"))
    @Validate(auth = true)
    @PutMapping("approve/{rid}")
    public ResponseEntity<String> approve_anime(@PathVariable Integer rid) {
        anime_service.process_anime(rid);
        return Result.success();
    }

    @Operation(summary = "不通过审核")
    @Parameters(@Parameter(name = "rid", description = "要审核的动漫id"))
    @Validate(auth = true)
    @PutMapping("reject/{rid}")
    public ResponseEntity<String> reject_anime(@PathVariable Integer rid) {
        anime_service.del_anime(rid);
        return Result.success();
    }

    @Operation(summary = "用户获取自己上传动漫")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @GetMapping("users/animes")
    public ResponseEntity<String> get_user_anime(HttpServletRequest req) throws JsonProcessingException {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        List<Resource> resources = anime_service.get_by_user(uid);
        String data = new ObjectMapper().writeValueAsString(resources);
        return Result.success(data);
    }

    @Operation(summary = "用户删除自己上传的动漫")
    @Parameters(@Parameter(name = "rid", description = "要删除的动漫id"))
    @DeleteMapping("resource/{rid}")
    public ResponseEntity<String> del_user_anime(@PathVariable Integer rid, HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        Integer uid = jwt_util.getLoginUserId(token);
        anime_service.del_user_anime(uid, rid);
        return Result.success();
    }

    @Operation(summary = "按名称搜索动漫(模糊搜索)")
    @Parameters(@Parameter(name = "name", description = "搜索动漫名称关键词"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @Validate(login = false)
    @GetMapping("search")
    public ResponseEntity<String> search(@RequestParam String name) throws JsonProcessingException {

        List<Resource> resources = anime_service.search(name);
        String data = new ObjectMapper().writeValueAsString(resources);

        return Result.success(data);
    }

    @Operation(summary = "点赞")
    @Parameters(@Parameter(name = "rid", description = "要点赞的动漫id"))
    @Validate(login = false)
    @PutMapping("animes/{rid}/like")
    public ResponseEntity<String> add_likes(@PathVariable Integer rid) {
        anime_service.add_likes(rid);
        return Result.success();
    }

    @Operation(summary = "按名称获取动漫信息")
    @Parameters(@Parameter(name = "name", description = "动漫名称"))
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @Validate(login = false)
    @GetMapping("animes/{name}")
    public ResponseEntity<String> get_animes_by_name(@PathVariable String name) throws JsonProcessingException {
        List<Resource> resources = anime_service.get_by_name(name);
        String data = new ObjectMapper().writeValueAsString(resources);

        return Result.success(data);
    }

    @Operation(summary = "获取播放量前十的动漫")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @Validate(login = false)
    @GetMapping("animes/popular")
    public ResponseEntity<String> get_popular() throws JsonProcessingException {
        List<Resource> popular = anime_service.get_popular();
        String data = new ObjectMapper().writeValueAsString(popular);
        return Result.success(data);
    }

    @Operation(summary = "获取播放量前十的动漫")
    @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    @Validate(login = false)
    @GetMapping("animes/recommend")
    public ResponseEntity<String> get_recommend() throws JsonProcessingException {
        List<Resource> recommend = anime_service.get_recommend();
        String data = new ObjectMapper().writeValueAsString(recommend);
        return Result.success(data);
    }

}
