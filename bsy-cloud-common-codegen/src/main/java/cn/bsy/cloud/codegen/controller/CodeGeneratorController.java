package cn.bsy.cloud.codegen.controller;

import cn.bsy.cloud.codegen.bo.GenCodeBO;
import cn.bsy.cloud.codegen.service.ICodeGenreatorService;
import cn.hutool.core.io.IoUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 代码生成器控制层
 *
 * @author gaoheng
 * 2023年5月15日下午10:13:53
 */
@RestController
@RequestMapping("/code/gen")
@AllArgsConstructor
public class CodeGeneratorController {
    private final ICodeGenreatorService codeGenreatorService;

    /**
     * 生成并下载代码
     *
     * @param genCodeBO
     * @param response
     * @param bindingResult
     */
    @SneakyThrows
    @GetMapping
    public void generatorCode(@Valid GenCodeBO genCodeBO, HttpServletResponse response, BindingResult bindingResult) {
        byte[] data = codeGenreatorService.generatorCode(genCodeBO);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.zip", genCodeBO.getTableName()));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");
        IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);
    }
}
