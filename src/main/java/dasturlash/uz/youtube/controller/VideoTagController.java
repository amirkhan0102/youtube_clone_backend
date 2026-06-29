package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.videotag.VideoTagDTO;
import dasturlash.uz.youtube.dto.videotag.VideoTagFullInfoDTO;
import dasturlash.uz.youtube.service.VideoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video-tag")
public class VideoTagController {

    @Autowired
    private VideoTagService videoTagService;

    @PostMapping("/add")
    public ResponseEntity<String> addTag(@RequestBody VideoTagDTO dto){
        return ResponseEntity.ok(videoTagService.addTagToVideo(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTag(@RequestBody VideoTagDTO dto){
        return ResponseEntity.ok(videoTagService.deleteTagFromVideo(dto));
    }

    @GetMapping("/list/{videoId}")
    public ResponseEntity<List<VideoTagFullInfoDTO>> getListTag(@PathVariable String videoId){
        return ResponseEntity.ok(videoTagService.getTagListByVideoId(videoId));
    }
}
