package com.cetc52.visitors.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cetc52.visitors.domain.ExcelForm;
import com.cetc52.visitors.utils.CommonUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CommonService {
    @Autowired CommonUtil commonUtil;

    public String createExcel(ExcelForm form) throws IOException {
        //时间戳+随机数=filename
        String fileName = String.valueOf(System.currentTimeMillis()) + commonUtil.getRandom(1, 1000000);
        String filePath = null;
        //组织excel头
        //新建excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        //新建sheet
        HSSFSheet sheet = workbook.createSheet("历史来访记录表");
        LinkedHashMap<String, String> headers = form.getTh();
        Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
        //新建行
        HSSFRow rowTh = sheet.createRow(0);
        ArrayList<String> keysArray = new ArrayList<String>();
        int index = -1;
        while (iterator.hasNext()) {
            ++index;
            Map.Entry<String, String> entry = iterator.next();
            keysArray.add(entry.getKey());
            //新建列
            HSSFCell cell = rowTh.createCell(index);
            HSSFRichTextString text = new HSSFRichTextString(entry.getValue());
            cell.setCellValue(text);
        }

        //组织excel主体内容
        JSONArray jsonArray = JSON.parseArray(form.getData());
        for (int i=0; i<jsonArray.size(); i++){
            HSSFRow row = sheet.createRow(i+1);
            for (int j = 0; j < keysArray.size(); j++){
                HSSFCell cell = row.createCell(j);
                HSSFRichTextString text = new HSSFRichTextString((String) ((JSONObject) jsonArray.get(i)).get(keysArray.get(j)));
                cell.setCellValue(text);
            }
        }

        //通过classpath，正则匹配出根目录的下级目录，在此目录下生成临时xls文件
        String classpath = this.getClass().getResource("/").getPath();
        String regex = ".*([A-Z]:/.*?/).*";
        Pattern r = Pattern.compile(regex);
        Matcher m = r.matcher(classpath);
        if (m.find()) {
            filePath = m.group(1) + fileName + ".xls";
        }
        FileOutputStream fileStream = new FileOutputStream(filePath);
        workbook.write(fileStream);
        fileStream.flush();
        fileStream.close();
        return filePath;
    }
}
