package com.ruoyi.article.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ruoyi.article.domain.vo.ArticleVO;
import com.ruoyi.article.mapper.CmsArticleDetailMapper;
import com.ruoyi.article.service.IArticleService;
import com.ruoyi.common.async.AsyncTaskManager;
import com.ruoyi.contentcore.domain.CmsSite;
import com.ruoyi.contentcore.listener.event.AfterContentEditorInitEvent;
import com.ruoyi.contentcore.listener.event.BeforeSiteDeleteEvent;
import com.ruoyi.contentcore.util.InternalUrlUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArticleListener {

	private final IArticleService articleService;

	private final CmsArticleDetailMapper articleMapper;

	@EventListener
	public void beforeSiteDelete(BeforeSiteDeleteEvent event) {
		CmsSite site = event.getSite();
		int pageSize = 500;
		// 删除文章数据
		try {
			long total = this.articleMapper.selectCountBySiteIdIgnoreLogicDel(site.getSiteId());
			for (int i = 0; i * pageSize < total; i++) {
				AsyncTaskManager.setTaskProgressInfo((int)  (i * pageSize * 100 / total), "正在删除文章详情备份数据：" + (i * pageSize) + "/" + total);
				this.articleMapper.deleteBySiteIdIgnoreLogicDel(site.getSiteId(), pageSize);
			}
		} catch (Exception e) {
			e.printStackTrace();
			AsyncTaskManager.addErrMessage("删除文章详情错误：" + e.getMessage());
		}
	}

	@EventListener
	public void afterContentEditirInit(AfterContentEditorInitEvent event) {
		if (event.getContentVO() instanceof ArticleVO vo) {
			vo.setContentHtml(InternalUrlUtils.dealResourceInternalUrl(vo.getContentHtml()));
		}
	}
}
