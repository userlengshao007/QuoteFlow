package com.quoteflow.controller;

import com.quoteflow.dto.CustomerQuoteStatsItemVO;
import com.quoteflow.exception.BusinessException;
import com.quoteflow.exception.ErrorCodeEnum;
import com.quoteflow.service.CustomerQuoteStatsService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户报价统计页面控制器。
 *
 * @author QuoteFlow
 */
@RestController
@RequestMapping("/customer-quote-stats")
public class CustomerQuoteStatsPageController {

    /**
     * 客户报价统计服务。
     */
    private final CustomerQuoteStatsService customerQuoteStatsService;

    public CustomerQuoteStatsPageController(CustomerQuoteStatsService customerQuoteStatsService) {
        this.customerQuoteStatsService = customerQuoteStatsService;
    }

    /**
     * 顶部按钮打开的客户报价统计页面。
     *
     * @param requestParamMap 超星顶部按钮自动追加的上下文参数
     * @return 统计结果 HTML
     */
    @GetMapping(value = "/top", produces = MediaType.TEXT_HTML_VALUE)
    public String summarizeTopButton(@RequestParam Map<String, String> requestParamMap) {
        Long uid = parseRequiredUid(requestParamMap.get("uid"));
        String queryId = requestParamMap.get("queryId");
        if (!StringUtils.hasText(queryId)) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "queryId 不能为空");
        }

        List<CustomerQuoteStatsItemVO> itemList = customerQuoteStatsService.summarizeTopSelected(uid, queryId);
        return renderStatsPage(itemList, requestParamMap);
    }

    private Long parseRequiredUid(String uidText) {
        if (!StringUtils.hasText(uidText)) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "uid 不能为空");
        }
        try {
            return Long.valueOf(uidText);
        } catch (NumberFormatException exception) {
            throw new BusinessException(ErrorCodeEnum.INVALID_PARAMETER, "uid 格式不正确", exception);
        }
    }

    private String renderStatsPage(List<CustomerQuoteStatsItemVO> itemList, Map<String, String> requestParamMap) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        int projectCount = 0;
        for (CustomerQuoteStatsItemVO item : itemList) {
            projectCount += item.getProjectCount() == null ? 0 : item.getProjectCount();
            totalAmount = totalAmount.add(item.getTotalQuoteAmount() == null
                    ? BigDecimal.ZERO : item.getTotalQuoteAmount());
        }

        StringBuilder html = new StringBuilder(4096);
        html.append("<!doctype html><html lang=\"zh-CN\"><head><meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">")
                .append("<title>客户报价统计</title>")
                .append("<style>")
                .append("body{margin:0;background:#f6f7f9;color:#1f2937;font-family:")
                .append("-apple-system,BlinkMacSystemFont,'Segoe UI','Microsoft YaHei',sans-serif;}")
                .append(".page{max-width:1040px;margin:0 auto;padding:28px 20px 40px;}")
                .append("h1{margin:0 0 18px;font-size:24px;font-weight:650;}")
                .append(".summary{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:12px;margin-bottom:18px;}")
                .append(".metric{background:#fff;border:1px solid #e5e7eb;border-radius:8px;padding:14px 16px;}")
                .append(".metric span{display:block;color:#6b7280;font-size:13px;margin-bottom:8px;}")
                .append(".metric strong{font-size:22px;font-weight:650;}")
                .append("table{width:100%;border-collapse:collapse;background:#fff;border:1px solid #e5e7eb;")
                .append("border-radius:8px;overflow:hidden;}")
                .append("th,td{padding:12px 14px;border-bottom:1px solid #eef0f3;text-align:left;font-size:14px;}")
                .append("th{background:#f9fafb;color:#4b5563;font-weight:650;}")
                .append("tr:last-child td{border-bottom:0;}")
                .append(".amount{text-align:right;font-variant-numeric:tabular-nums;}")
                .append(".empty{background:#fff;border:1px solid #e5e7eb;border-radius:8px;padding:32px;text-align:center;")
                .append("color:#6b7280;}")
                .append(".meta{margin-top:14px;color:#9ca3af;font-size:12px;word-break:break-all;}")
                .append("@media(max-width:720px){.summary{grid-template-columns:1fr;}th,td{padding:10px 8px;}}")
                .append("</style></head><body><main class=\"page\">")
                .append("<h1>客户报价统计</h1>")
                .append("<section class=\"summary\">")
                .append(renderMetric("客户数量", String.valueOf(itemList.size())))
                .append(renderMetric("项目数量", String.valueOf(projectCount)))
                .append(renderMetric("报价总额", formatAmount(totalAmount)))
                .append("</section>");

        if (itemList.isEmpty()) {
            html.append("<section class=\"empty\">未获取到本次选中的项目报价数据</section>");
        } else {
            html.append("<table><thead><tr>")
                    .append("<th>客户 ID</th><th>客户名称</th><th>项目数量</th><th class=\"amount\">报价总额</th>")
                    .append("</tr></thead><tbody>");
            for (CustomerQuoteStatsItemVO item : itemList) {
                html.append("<tr>")
                        .append("<td>").append(escape(defaultText(item.getCustomerId()))).append("</td>")
                        .append("<td>").append(escape(defaultText(item.getCustomerName()))).append("</td>")
                        .append("<td>").append(item.getProjectCount() == null ? 0 : item.getProjectCount())
                        .append("</td>")
                        .append("<td class=\"amount\">")
                        .append(formatAmount(item.getTotalQuoteAmount()))
                        .append("</td>")
                        .append("</tr>");
            }
            html.append("</tbody></table>");
        }

        html.append("<div class=\"meta\">queryId：")
                .append(escape(defaultText(requestParamMap.get("queryId"))))
                .append("；selectTotal：")
                .append(escape(defaultText(requestParamMap.get("selectTotal"))))
                .append("</div>")
                .append("</main></body></html>");
        return html.toString();
    }

    private String renderMetric(String name, String value) {
        return "<div class=\"metric\"><span>" + escape(name) + "</span><strong>" + escape(value) + "</strong></div>";
    }

    private String formatAmount(BigDecimal amount) {
        BigDecimal safeAmount = amount == null ? BigDecimal.ZERO : amount;
        return safeAmount.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String defaultText(String value) {
        return value == null ? "-" : value;
    }

    private String escape(String value) {
        return HtmlUtils.htmlEscape(value, "UTF-8");
    }
}
