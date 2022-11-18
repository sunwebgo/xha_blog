package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.TagByIdVo;
import com.xha.domain.dto.TagDto;
import com.xha.domain.vo.PageVo;
import com.xha.service.TagService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Resource
    private TagService tagService;


    /**
     * 查询标签列表
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param tagDto 标记列表dto
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize, TagDto tagDto){
        return tagService.getTagList(pageNum,pageSize,tagDto);
    }

    /**
     * 添加标签
     * @return {@link ResponseResult}
     */
    @PostMapping()
    public ResponseResult addTag(@RequestBody TagDto tagDto){
        return tagService.addTag(tagDto);
    }


    /**
     * 根据标签id删除标签
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable Long id){
        return tagService.deleteTag(id);
    }

    /**
     * 根据标签id查询标签信息
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    /**
     * 更新标签
     * @return {@link ResponseResult}
     */
    @PutMapping()
    public ResponseResult updateTag(@RequestBody TagByIdVo tagByIdVo){
        return tagService.updateTag(tagByIdVo);
    }


    /**
     * 得到名字标记列表
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/listAllTag")
    public ResponseResult getNameTagList(){
        return tagService.getNameTagList();
    }

}
