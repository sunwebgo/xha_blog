package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.AddLinkDto;
import com.xha.domain.dto.LinkDto;
import com.xha.domain.dto.LinkStatusDto;
import com.xha.domain.entity.Link;
import com.xha.domain.vo.AdminLinkVo;
import com.xha.domain.vo.LinkVo;
import com.xha.mapper.LinkMapper;
import com.xha.service.LinkService;
import com.xha.utils.BeanCopyPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.xha.constants.CommonConstants.*;
import static com.xha.enums.AppHttpCodeEnum.*;

/**
 * @author Tony贾维斯
 * @description 针对表【sg_link(友链)】的数据库操作Service实现
 * @createDate 2022-11-05 22:21:27
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
        implements LinkService {


    @Resource
    private LinkMapper linkMapper;

    /**
     * 查询所有友链
     *
     * @return
     */
    public ResponseResult getAllLink() {
//        1.查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<Link>();
        queryWrapper.eq(Link::getStatus, LINK_STATUS_NORMAL);
        List<Link> lists = list(queryWrapper);
//        2.转换为Vo对象
        List<LinkVo> linkVos = BeanCopyPropertiesUtils.copyBeanList(lists, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    /**
     * 分页查询所有友链-Admin
     *
     * @param pageNum
     * @param pageSize
     * @param linkDto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getAllLinkByAdmin(Integer pageNum, Integer pageSize, LinkDto linkDto) {
//        1.根据友链名(模糊查询)和状态进行查询
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(linkDto.getStatus()), Link::getStatus, linkDto.getStatus());
        queryWrapper.like(StringUtils.hasText(linkDto.getName()), Link::getName, linkDto.getName());

//        2.分页查询
        Page<Link> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);

//        3.将当前页中的Link对象转换为LinkVo对象
        List<Link> links = page.getRecords();
        List<LinkVo> linkVos = BeanCopyPropertiesUtils.copyBeanList(links, LinkVo.class);
//        4.将LinkVo对象转换为LinkAdminVo对象
        AdminLinkVo adminLinkVo = new AdminLinkVo(linkVos, page.getTotal());
        return ResponseResult.okResult(adminLinkVo);
    }

    /**
     * 添加友链
     *
     * @param addLinkDto 添加链接dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
//        1.首先根据友链名称查询要添加的友链是否存在
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Link::getName, addLinkDto.getName());
        Link link = getOne(queryWrapper);
        if (!Objects.isNull(link)) {
            return ResponseResult.errorResult(LINK_IS_EXIST);
        }
//        2.添加友链
//          2.1将AddLinkDto对象转为Link对象
        Link addLink = BeanCopyPropertiesUtils.copyBean(addLinkDto, Link.class);
        save(addLink);
        return ResponseResult.okResult();
    }


    /**
     * 根据id删除友链
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteLink(Long id) {
        boolean result = removeById(id);
        if (result == false) {
            return ResponseResult.errorResult(DELETE_LINK_FAIL);
        }
        return ResponseResult.okResult();
    }


    /**
     * 根据id查询友链
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getLinkOneById(Long id) {
//        1.根据id查询友链
        Link link = getById(id);
//        2.将Link对象封装为LinkVo对象
        LinkVo linkVo = BeanCopyPropertiesUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }


    /**
     * 修改友链
     *
     * @param linkDto 联系签证官
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
//        1.判断LinkDto对象值是否为空
        if (!StringUtils.hasText(linkDto.getName()) ||
                !StringUtils.hasText(linkDto.getAddress()) ||
                !StringUtils.hasText(String.valueOf(linkDto.getStatus())) ||
                !StringUtils.hasText(linkDto.getLogo()) ||
                !StringUtils.hasText(linkDto.getDescription())) {
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
//        2.将LinkVo对象转换为Link对象
        Link link = BeanCopyPropertiesUtils.copyBean(linkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }


    /**
     * 更新友链状态
     *
     * @param linkStatusDto 链接状态dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateLinkStatus(LinkStatusDto linkStatusDto) {
//        1.根据友链id设置友链status
        UpdateWrapper<Link> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq(LINK_ID, linkStatusDto.getId());
        updateWrapper.set(LINK_STATUS, linkStatusDto.getStatus());
        linkMapper.update(null, updateWrapper);
        return ResponseResult.okResult();
    }
}




