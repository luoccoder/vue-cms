package com.ruoyi.contentcore.domain.dto;

import com.ruoyi.common.security.domain.BaseDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateRenameDTO extends BaseDTO {

	@NotNull
	@Min(1)
	private Long templateId;

    @NotEmpty
    private String path;
    
    private String remark;
}
