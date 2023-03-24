package com.ruoyi.contentcore.template.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ruoyi.common.staticize.core.TemplateContext;
import com.ruoyi.common.utils.ConvertUtils;
import com.ruoyi.common.utils.ReflectASMUtils;
import com.ruoyi.contentcore.domain.CmsCatalog;
import com.ruoyi.contentcore.domain.CmsContent;
import com.ruoyi.contentcore.domain.CmsSite;
import com.ruoyi.contentcore.mapper.CmsContentMapper;
import com.ruoyi.contentcore.service.ICatalogService;
import com.ruoyi.contentcore.service.ISiteService;
import com.ruoyi.contentcore.template.ITemplateType;
import com.ruoyi.contentcore.util.TemplateUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component(ITemplateType.BEAN_NAME_PREFIX + ContentTemplateType.TypeId)
public class ContentTemplateType implements ITemplateType {
	
	public final static String TypeId = "Content";
	
	private final CmsContentMapper contentMapper;
	
	private final ISiteService siteService;

	private final ICatalogService catalogService;

	@Override
	public String getId() {
		return TypeId;
	}

	@Override
	public void initTemplateData(Object dataId, TemplateContext context) {
		CmsContent content = this.contentMapper.selectById(ConvertUtils.toLong(dataId));
		Map<String, Object> contentMap = ReflectASMUtils.beanToMap(content);
		context.getVariables().put(TemplateUtils.TemplateVariable_Content, contentMap);

		CmsSite site = this.siteService.getSite(content.getSiteId());
		CmsCatalog catalog = this.catalogService.getCatalog(content.getCatalogId());
		TemplateUtils.addCatalogVariables(site, catalog, context);
	}
}