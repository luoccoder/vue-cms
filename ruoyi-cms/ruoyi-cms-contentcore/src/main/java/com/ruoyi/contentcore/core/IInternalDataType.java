package com.ruoyi.contentcore.core;

import java.io.IOException;
import java.util.Map;

import cn.dev33.satoken.config.SaTokenConfig;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

import com.ruoyi.system.security.StpAdminUtil;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 内部数据类型
 *
 * @author 兮玥
 * @email 190785909@qq.com
 */
public interface IInternalDataType {
	
	/**
	 * Bean名称前缀
	 */
	String BEAN_NAME_PREFIX = "InternalDataType_";
	
	/**
	 * 获取内部数据预览地址
	 * 
	 * 预览路径规则：cms/preview/{内部数据类型}/{数据id}?pp={发布通道编码}&pi={页码}
	 * 
	 * @param type
	 * @param id
	 * @param publishPipeCode
	 * @param pageIndex
	 * @return
	 */
	static String getPreviewPath(String type, Long id, String publishPipeCode, int pageIndex) {
		String path = "cms/preview/" + type + "/" + id + "?pp=" + publishPipeCode;
		if (pageIndex > 1) {
			path += "&pi=" + pageIndex;
		}
		if (StpAdminUtil.isLogin()) {
			SaTokenConfig config = StpAdminUtil.getStpLogic().getConfig();
			path = path + "&" + config.getTokenName() + "="
					+ (StringUtils.isNotEmpty(config.getTokenPrefix()) ? config.getTokenPrefix() + " " : "")
					+ StpAdminUtil.getTokenValue();
		}
		return path;
	}

	static String getPreviewPath(String type, Long id, String publishPipeCode) {
		return getPreviewPath(type, id, publishPipeCode, 1);
	}

	/**
	 * 获取内部数据动态浏览地址
	 * 
	 * 动态路径规则：cms/view/{内部数据类型}/{数据id}?pp={发布通道编码}&pi={页码}
	 * 
	 * @param type
	 * @param id
	 * @param publishPipeCode
	 * @param pageIndex
	 * @return
	 */
	static String getViewPath(String type, Long id, String publishPipeCode, int pageIndex) {
		String path = "cms/preview/" + type + "/" + id + "?pp=" + publishPipeCode;
		if (pageIndex > 1) {
			path += "&pi=" + pageIndex;
		}
		return path;
	}

	static String getViewPath(String type, Long id, String publishPipeCode) {
		return getViewPath(type, id, publishPipeCode, 1);
	}
	
	/**
	 * 类型ID
	 */
	public String getId();

	/**
	 * 获取模板解析页面内容
	 * 
	 * @param requestData
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	default String getPageData(RequestData requestData) throws IOException, TemplateException {
		return StringUtils.EMPTY;
	}
	
	/**
	 * 访问链接
	 * 
	 * @param internalUrl
	 * @param pageIndex
	 * @param publishPipeCode
	 * @param preview
	 * @return
	 */
	default String getLink(InternalURL internalUrl, int pageIndex, String publishPipeCode, boolean preview) {
		return StringUtils.EMPTY;
	}

	@Getter
	@Setter
	class RequestData {

		/**
		 * 数据ID
		 */
		Long dataId;

		/**
		 * 分页标识
		 */
		int pageIndex;

		/**
		 * 发布通道编码
		 */
		String publishPipeCode;

		/**
		 * 是否预览模式
		 */
		boolean preview;

		/**
		 * 其他扩展参数
		 */
		Map<String, String> params;

		public RequestData(Long dataId, Integer pageIndex, String publishPipeCode, boolean preview, Map<String, String> params) {
			this.dataId = dataId;
			this.pageIndex = pageIndex;
			this.publishPipeCode = publishPipeCode;
			this.preview = preview;
			this.params = params;
		}
	}
}
