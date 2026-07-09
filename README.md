# QuoteFlow

QuoteFlow 是一个 Spring Boot 后端服务，用于封装超星办公表单和审批 SDK。

## 当前范围

- 新增人员信息表数据。
- 发起项目立项与报价审批数据。
- 查询选中的项目数据，并按客户汇总报价总额。
- 生成销售业绩统计数据。

## 必要配置

真实调用超星接口前，需要配置以下环境变量：

```bash
export CHAOXING_FORMS_SIGN=
export CHAOXING_FORMS_KEY=
export CHAOXING_APPROVE_SIGN=
export CHAOXING_APPROVE_KEY=
export CHAOXING_FID=
export CHAOXING_SUBMIT_UID=199752933
export CHAOXING_CUSTOMER_FORM_ID=3412772
export CHAOXING_PROJECT_APPROVE_FORM_ID=3412949
export CHAOXING_SALES_STATISTICS_FORM_ID=3412951
```

当前三张业务表：

| 业务表 | 配置项 | 当前值 |
| --- | --- | --- |
| 人员信息表 | `CHAOXING_CUSTOMER_FORM_ID` | `3412772` |
| 项目立项与报价表 | `CHAOXING_PROJECT_APPROVE_FORM_ID` | `3412949` |
| 销售业绩统计表 | `CHAOXING_SALES_STATISTICS_FORM_ID` | `3412951` |

当前测试提交人：

| 用户 | 配置项 | 当前值 |
| --- | --- | --- |
| 章宇杰 | `CHAOXING_SUBMIT_UID` | `199752933` |

字段别名配置在 `src/main/resources/application.yml`。目前先按需求文档中的字段别名配置，例如
`customer_name`、`project_members`、`total_quote_amount`。如果平台实际字段别名或字段 ID 不一致，需要同步修改配置。

## 首个联调接口

建议先从人员信息新增开始，因为它只依赖普通表单 SDK：

```http
POST /api/customers
Content-Type: application/json

{
  "customerName": "测试客户",
  "customerLevel": "战略",
  "mainContactId": 123456,
  "mainContactName": "张三",
  "industry": "信息技术",
  "uuid": "customer-test-001"
}
```

也可以使用手动集成测试新增一条人员信息表数据。该测试默认不会执行，只有显式打开
`chaoxing.integration-test` 开关才会调用真实超星接口：

```bash
mvn -Dtest=CustomerServiceIntegrationTest \
  -Dchaoxing.integration-test=true \
  -Dchaoxing.test.forms-sign=你的表单sign \
  -Dchaoxing.test.forms-key=你的表单key \
  -Dchaoxing.test.fid=你的单位ID \
  -Dchaoxing.test.customer-level=战略 \
  -Dchaoxing.test.industry=信息技术 \
  -Dchaoxing.test.main-contact-id=199752933 \
  -Dchaoxing.test.main-contact-name=章宇杰 \
  test
```

人员信息新增成功后，再测试项目立项与报价审批：

```http
POST /api/projects
Content-Type: application/json

{
  "customerId": "客户ID",
  "projectDate": "2026-07-09 00:00",
  "expectedFinishDate": "2026-08-09 00:00",
  "projectMembers": [
    {
      "uid": 123456,
      "name": "张三"
    }
  ],
  "quoteDetails": [
    {
      "productName": "产品A",
      "specification": "标准版",
      "quantity": 2,
      "unitPrice": 10000
    }
  ]
}
```

## 注意事项

- `lib/office-api-sdk-1.22.2.jar` 是本地超星 SDK。
- 公式字段和自动编号字段通过 `new XxxField(alias, true)` 触发平台默认值或公式计算。
- 附件上传暂未实现，因为目前还没有附件上传接口以及 `objectId`、`resid` 等返回字段说明。
- 审批回调或数据推送暂未实现，因为目前还没有回调参数和签名校验规则说明。
