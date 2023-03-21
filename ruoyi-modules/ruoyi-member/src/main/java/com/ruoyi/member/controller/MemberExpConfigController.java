package com.ruoyi.member.controller;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.domain.R;
import com.ruoyi.common.exception.CommonErrorCode;
import com.ruoyi.common.i18n.I18nUtils;
import com.ruoyi.common.security.web.BaseRestController;
import com.ruoyi.common.utils.Assert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.member.domain.MemberExpConfig;
import com.ruoyi.member.domain.vo.ExpOperationVO;
import com.ruoyi.member.level.IExpOperation;
import com.ruoyi.member.level.ILevelType;
import com.ruoyi.member.service.IMemberExpConfigService;
import com.ruoyi.member.service.IMemberLevelConfigService;
import com.ruoyi.system.security.SaAdminCheckLogin;
import com.ruoyi.system.security.StpAdminUtil;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

@SaAdminCheckLogin
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/expConfig")
public class MemberExpConfigController extends BaseRestController {

	private final IMemberLevelConfigService memberLevelConfigService;

	private final IMemberExpConfigService memberExpOperationService;

	@GetMapping
	public R<?> getPageList(@RequestParam(value = "opType", required = false) String opType,
			@RequestParam(value = "levelType", required = false) String levelType) {
		PageRequest pr = this.getPageRequest();
		Page<MemberExpConfig> page = this.memberExpOperationService.lambdaQuery()
				.eq(StringUtils.isNotEmpty(opType), MemberExpConfig::getOpType, opType)
				.eq(StringUtils.isNotEmpty(levelType), MemberExpConfig::getLevelType, levelType)
				.page(new Page<>(pr.getPageNumber(), pr.getPageSize(), true));

		page.getRecords().forEach(conf -> {
			IExpOperation expOperation = this.memberExpOperationService.getExpOperation(conf.getOpType());
			conf.setOpTypeName(I18nUtils.get(expOperation.getName()));
			ILevelType lt = this.memberLevelConfigService.getLevelType(conf.getLevelType());
			conf.setLevelTypeName(I18nUtils.get(lt.getName()));
		});
		return this.bindDataTable(page);
	}

	@GetMapping("/{expOperationId}")
	public R<?> getExpOperationDetail(@PathVariable("expOperationId") @Min(1) Long expOperationId) {
		MemberExpConfig conf = this.memberExpOperationService.getById(expOperationId);
		Assert.notNull(conf, () -> CommonErrorCode.DATA_NOT_FOUND_BY_ID.exception("id", expOperationId));
		IExpOperation expOperation = this.memberExpOperationService.getExpOperation(conf.getOpType());
		conf.setOpTypeName(I18nUtils.get(expOperation.getName()));
		return R.ok(conf);
	}

	@GetMapping("/types")
	public R<?> getOperationTypes() {
		List<ExpOperationVO> list = this.memberExpOperationService.getExpOperations().values().stream()
				.map(op -> new ExpOperationVO(op.getId(), I18nUtils.get(op.getName()))).toList();
		return R.ok(list);
	}

	@PostMapping
	public R<?> addMemberExpOperation(@RequestBody MemberExpConfig expOp) {
		expOp.setCreateBy(StpAdminUtil.getLoginUser().getUsername());
		this.memberExpOperationService.addExpOperation(expOp);
		return R.ok();
	}

	@PutMapping
	public R<?> updateMemberExpOperation(@RequestBody MemberExpConfig expOp) {
		expOp.setUpdateBy(StpAdminUtil.getLoginUser().getUsername());
		this.memberExpOperationService.updateExpOperation(expOp);
		return R.ok();
	}

	@DeleteMapping
	public R<?> deleteExpOperations(@RequestBody @NotEmpty List<Long> expOperationIds) {
		this.memberExpOperationService.deleteExpOperations(expOperationIds);
		return R.ok();
	}
}