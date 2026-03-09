package org.ruoyi.chat.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.aspose.words.License;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.ruoyi.chat.domain.TaskManagementFile;
import lombok.RequiredArgsConstructor;
import org.ruoyi.chat.domain.vo.ReportVo;
import org.ruoyi.chat.mapper.TaskManagementFileMapper;
import org.ruoyi.chat.service.ITaskManagementFileService;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.common.oss.core.OssClient;
import org.ruoyi.common.oss.factory.OssFactory;
import org.ruoyi.common.web.core.BaseController;
import org.ruoyi.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;


@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController extends BaseController {

    private final TaskManagementFileMapper fileMapper;

    /**
     * 获取报告列表
     */
    @SaIgnore
    @PostMapping("/list")
    public R<List<ReportVo>> list(@RequestBody ReportQueryParams params) {
        // 调用 Mapper 查出 file_category = 'output' 的数据
        List<ReportVo> list = fileMapper.selectReportList(params.getKeyword(), params.getType());
        return R.ok(list);
    }

    /**
     * 删除报告
     */
    @SaIgnore
// ✨ 2. 使用 @RequestMapping 兼容前端可能发出的 POST 或 DELETE 请求
    @RequestMapping(value = "/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.POST})
    public R<Void> delete(@PathVariable Long id) {
        try {
            // 先查出文件信息（如果需要同步删除 MinIO 里的物理文件）
            TaskManagementFile file = fileMapper.selectById(id);

            // 执行数据库删除
            int rows = fileMapper.deleteById(id);

            return rows > 0 ? R.ok() : R.fail("删除失败，记录不存在");
        } catch (Exception e) {
            return R.fail("服务器错误：" + e.getMessage());
        }
    }

    /**
     * 导出报告（此处复用下载逻辑）
     */
    @SaIgnore
    @PostMapping("/export")
    public void export(@RequestBody java.util.Map<String, Object> params, HttpServletResponse response) {
        try {
            Object idObj = params.get("id");
            String format = (String) params.getOrDefault("format", "word"); // 默认按 word 导出

            if (idObj == null) {
                response.setStatus(400);
                return;
            }
            Long id = Long.valueOf(idObj.toString());

            TaskManagementFile file = fileMapper.selectById(id);
            if (file == null || file.getUrl() == null) {
                response.setStatus(404);
                return;
            }

            // 1. 解析 OSS 里的 ObjectKey
            String fullUrl = file.getUrl();
            String objectKey = "";
            try {
                URL url = new URL(fullUrl);
                String path = url.getPath();
                String[] parts = path.split("/", 3);
                objectKey = parts.length >= 3 ? parts[2] : (path.startsWith("/") ? path.substring(1) : path);
            } catch (Exception e) {
                objectKey = fullUrl.substring(fullUrl.lastIndexOf("/") + 1);
            }

            // 2. 准备基础的文件名（去掉原有的后缀）
            String baseName = file.getName();
            if (baseName != null && baseName.contains(".")) {
                baseName = baseName.substring(0, baseName.lastIndexOf("."));
            } else if (baseName == null) {
                baseName = "报告导出";
            }

            OssClient ossClient = OssFactory.instance();

            // 3. 核心逻辑：获取原始 Word 流并根据前端请求的格式处理
            try {
                InputStream licenseStream = this.getClass().getClassLoader().getResourceAsStream("license.xml");
                if (licenseStream != null) {
                    License license = new License();
                    license.setLicense(licenseStream);
                } else {
                    System.err.println("未找到 license.xml 文件，导出的 PDF 将带有水印。");
                }
            } catch (Exception e) {
                System.err.println("加载 Aspose License 失败: " + e.getMessage());
            }
            try (InputStream in = ossClient.getObjectContent(objectKey);
                 OutputStream out = response.getOutputStream()) {

                if ("pdf".equalsIgnoreCase(format)) {
                    // ====== 走 PDF 转换逻辑 ======
                    response.setContentType("application/pdf");
                    String downloadName = baseName + ".pdf";
                    response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downloadName, "UTF-8"));

                    // 利用 Aspose.Words 加载 Word 流并保存为 PDF 流
                    Document doc = new Document(in);
                    doc.save(out, SaveFormat.PDF);

                } else {
                    // ====== 走原生的 Word 下载逻辑 ======
                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    String downloadName = baseName + ".docx";
                    response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downloadName, "UTF-8"));

                    // 直接将 OSS 流拷贝给前端
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }
                out.flush();
            }

        } catch (Exception e) {
            System.err.println("导出或转换失败: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    @Data
    public static class ReportQueryParams {
        private Integer currentPage;
        private Integer pageSize;
        private String keyword;
        private String type;
    }

    @Autowired // ✨ 这就是“引用”的核心，让 Spring 帮你把 Service 实例注入进来
    private ITaskManagementFileService taskManagementFileService;

    @SaIgnore
    @RequestMapping(value = "/download/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    public void download(@PathVariable("id") Long id, HttpServletResponse response) {

        taskManagementFileService.downloadFile(id, response);
    }
}