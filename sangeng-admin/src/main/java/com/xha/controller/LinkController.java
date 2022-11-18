package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddLinkDto;
import com.xha.domain.dto.LinkDto;
import com.xha.domain.dto.LinkStatusDto;
import com.xha.domain.vo.LinkVo;
import com.xha.service.LinkService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Resource
    private LinkService linkService;


    /**
     * 分页查询所有友链-Admin
     *
     * @return {@link ResponseResult}
     */
    @GetMapping("/list")
    public ResponseResult getLinkList(Integer pageNum, Integer pageSize, LinkDto linkDto) {
        return linkService.getAllLinkByAdmin(pageNum, pageSize, linkDto);
    }


    /**
     * 添加友链
     * @param addLinkDto 添加链接dto
     * @return {@link ResponseResult}
     */
    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }

    /**
     * 删除链接
     * @param id id
     * @return {@link ResponseResult}
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }

    /**
     * 根据id查询友链
     * @param id id
     * @return {@link ResponseResult}
     */
    @GetMapping("/{id}")
    public ResponseResult getLinkOneById(@PathVariable Long id){
        return linkService.getLinkOneById(id);
    }


    /**
     * 修改友链
     * @param linkDto
     * @return {@link ResponseResult}
     */
    @PutMapping()
    public ResponseResult updateLink(@RequestBody LinkDto linkDto){
        return linkService.updateLink(linkDto);
    }


    /**
     * 更新友链状态
     * @param linkStatusDto 链接状态dto
     * @return {@link ResponseResult}
     */
    @PutMapping("/changeLinkStatus")
    public ResponseResult updateLinkStatus(@RequestBody LinkStatusDto linkStatusDto){
        return linkService.updateLinkStatus(linkStatusDto);
    }


}
