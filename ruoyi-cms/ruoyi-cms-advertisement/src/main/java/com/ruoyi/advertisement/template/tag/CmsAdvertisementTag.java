package com.ruoyi.advertisement.template.tag;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.advertisement.domain.CmsAdvertisement;
import com.ruoyi.advertisement.pojo.vo.AdvertisementVO;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.common.staticize.FreeMarkerUtils;
import com.ruoyi.common.staticize.core.TemplateContext;
import com.ruoyi.common.staticize.enums.TagAttrDataType;
import com.ruoyi.common.staticize.tag.AbstractListTag;
import com.ruoyi.common.staticize.tag.TagAttr;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.contentcore.domain.CmsPageWidget;
import com.ruoyi.contentcore.service.IPageWidgetService;
import com.ruoyi.system.fixed.dict.EnableOrDisable;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CmsAdvertisementTag extends AbstractListTag {

	public final static String TAG_NAME = "cms_advertisement";
	public final static String NAME = "{FREEMARKER.TAG.NAME." + TAG_NAME + "}";
	public final static String DESC = "{FREEMARKER.TAG.DESC." + TAG_NAME + "}";
	
	final static String TagAttr_Code = "code";

	private final IAdvertisementService advertisementService;
	
	private final IPageWidgetService pageWidgetService;
	
	@Override
	public List<TagAttr> getTagAttrs() {
		List<TagAttr> tagAttrs = super.getTagAttrs();
		tagAttrs.add(new TagAttr(TagAttr_Code, true, TagAttrDataType.STRING, "广告位编码"));
		return tagAttrs;
	}

	@Override
	public TagPageData prepareData(Environment env, Map<String, String> attrs, boolean page, int size, int pageIndex) throws TemplateException {
		String code = MapUtils.getString(attrs, TagAttr_Code);
		CmsPageWidget adSpace = this.pageWidgetService.getOne(new LambdaQueryWrapper<CmsPageWidget>().eq(CmsPageWidget::getCode, code));
		if (adSpace == null) {
			throw new TemplateException(StringUtils.messageFormat("<@{0}>AD place `{1}` not exists.", this.getTagName(), code), env)  ;
		}
		String condition = MapUtils.getString(attrs, TagAttr.AttrName_Condition);

		LambdaQueryWrapper<CmsAdvertisement> q = new LambdaQueryWrapper<CmsAdvertisement>()
				.eq(CmsAdvertisement::getAdSpaceId, adSpace.getPageWidgetId())
				.eq(CmsAdvertisement::getState, EnableOrDisable.ENABLE);
		q.apply(StringUtils.isNotEmpty(condition), condition);
		Page<CmsAdvertisement> pageResult = this.advertisementService.page(new Page<>(pageIndex, size, page), q);
		if (pageIndex > 1 & pageResult.getRecords().size() == 0) {
			throw new TemplateException(StringUtils.messageFormat("Page data empty: pageIndex = {0}", pageIndex), env) ;
		}
		List<AdvertisementVO> list = pageResult.getRecords().stream().map(AdvertisementVO::new).toList();
		return TagPageData.of(list, pageResult.getTotal());
	}

	@Override
	public String getTagName() {
		return TAG_NAME;
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getDescription() {
		return DESC;
	}
}
