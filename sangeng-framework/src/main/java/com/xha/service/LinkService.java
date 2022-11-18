package com.xha.service;

import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddLinkDto;
import com.xha.domain.dto.LinkDto;
import com.xha.domain.dto.LinkStatusDto;
import com.xha.domain.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xha.domain.vo.LinkVo;

/**
* @author Tony贾维斯
* @description 针对表【sg_link(友链)】的数据库操作Service
* @createDate 2022-11-05 22:21:27
*/

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto);

    ResponseResult addLink(AddLinkDto addLinkDto);

    ResponseResult deleteLink(Long id);

    ResponseResult getLinkOneById(Long id);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto);
}
