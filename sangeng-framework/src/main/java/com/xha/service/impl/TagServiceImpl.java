package com.xha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xha.domain.ResponseResult;
import com.xha.domain.dto.TagByIdVo;
import com.xha.domain.dto.TagDto;
import com.xha.domain.entity.Category;
import com.xha.domain.entity.Tag;
import com.xha.domain.vo.CategoryVo;
import com.xha.domain.vo.PageVo;
import com.xha.domain.vo.TagVo;
import com.xha.enums.AppHttpCodeEnum;
import com.xha.mapper.TagMapper;
import com.xha.service.TagService;
import com.xha.utils.BeanCopyPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.xha.constants.CommonConstants.CATEGORY_STATUS_NORMAL;
import static com.xha.enums.AppHttpCodeEnum.CONTENT_IS_BLANK;

/**
 * @author Tony贾维斯
 * @description 针对表【sg_tag(标签)】的数据库操作Service实现
 * @createDate 2022-11-10 21:43:53
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private TagMapper tagMapper;



    /**
     * 查询标签列表
     *
     * @param pageNum    页面num
     * @param pageSize   页面大小
     * @param tagDto 标记列表dto
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult<PageVo> getTagList(Integer pageNum, Integer pageSize, TagDto tagDto) {
//        1.封装查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagDto.getName()),Tag::getName, tagDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagDto.getRemark()),Tag::getRemark, tagDto.getRemark());

//        2.分页查询
        Page<Tag> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

//        3.将Tag对象转换为TagVo对象
        List<TagVo> tagVos = BeanCopyPropertiesUtils.copyBeanList(page.getRecords(), TagVo.class);

//        3.将TagVo对象封装为pageVo返回
        PageVo pageVo = new PageVo(tagVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    /**
     * 添加标签
     * @return {@link ResponseResult}
     * @param tagDto
     */
    @Override
    public ResponseResult addTag(TagDto tagDto) {
//        1.首先根据标签名判断标签是否存在
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Tag::getName,tagDto.getName());

        Tag getTag = getOne(queryWrapper);
        if (getTag != null){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_EXIST);
        }
//        1.将TagDto对象转换为Tag对象
        Tag tag = BeanCopyPropertiesUtils.copyBean(tagDto, Tag.class);
//        2.添加标签
        save(tag);
        return ResponseResult.okResult();
    }

    /**
     * 根据标签id删除标签
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteTag(Long id) {
//        1.根据标签id删除标签
        removeById(id);
        return ResponseResult.okResult();
    }

    /**
     * 根据标签id查询标签信息
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getTagById(Long id) {
//        1.根据id获取到Tag对象
        Tag tag = getById(id);
        if (Objects.isNull(tag)){
            return ResponseResult.errorResult(AppHttpCodeEnum.TAG_IS_NOEXIST);
        }

//        2.将Tag对象转化为TagVo对象
        TagVo tagVo = BeanCopyPropertiesUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    /**
     * 更新标签
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateTag(TagByIdVo tagByIdVo) {
//        1.判断数据是否为空
        if (!StringUtils.hasText(tagByIdVo.getName()) && !StringUtils.hasText(tagByIdVo.getRemark())){
            return ResponseResult.errorResult(CONTENT_IS_BLANK);
        }
//        2.将TagByIdVo对象转换为Tag对象
        Tag tag = BeanCopyPropertiesUtils.copyBean(tagByIdVo, Tag.class);
//        3.根据标签id查询对应的标签信息
        updateById(tag);
        return ResponseResult.okResult();
    }

    /**
     * 得到名字标记列表
     *
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult getNameTagList() {
        //        1.查询所有文章类别列表
        List<Tag> tags = tagMapper.selectList(null);
//        2.将Category对象转换为TagVo对象
        List<TagVo> tagVos = BeanCopyPropertiesUtils.copyBeanList(tags, TagVo.class);
        return ResponseResult.okResult(tagVos);
    }
}




