package com.cetc52.visitors.controller.common;
import com.cetc52.visitors.domain.ExcelForm;
import com.cetc52.visitors.service.CommonService;
import com.cetc52.visitors.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.asm.Advice;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-02 14:59
 */
@RestController
@Api(description = "公用对外接口")
public class CommonController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/api/common/createExcel", produces = "application/json")
    @ApiOperation("生成excel文件")
    public String createExcel(@RequestBody ExcelForm form) throws IOException {
        return commonService.createExcel(form);

    }

    @RequestMapping(value = "/api/common/downloadExcel", produces = "application/json")
    @ApiOperation("下载本地excel文件")
    public void downloadExcel(@RequestParam(value = "filePath") String filePath, @RequestParam(value = "fileName") String fileName, HttpServletResponse response) throws IOException {
        File file = new File(filePath);
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        out.write(FileUtils.readFileToByteArray(file));
        out.flush();
        out.close();
        //删除临时excel文件
        if (file.exists()){
            file.delete();
        }
    }
}
