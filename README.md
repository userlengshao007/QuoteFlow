# QuoteFlow

QuoteFlow 是一个 Spring Boot 后端服务，用于封装超星办公表单和审批 SDK。

## 当前范围

- 新增人员信息表数据。
- 发起项目立项与报价审批数据。
- 项目立项与报价顶部按钮按客户汇总报价总额，并校验超星追加的 `enc` 加密串。
- 项目立项审批通过后，通过数据推送自动生成销售业绩明细。
- 生成销售业绩统计数据。

## 必要配置

真实调用超星接口前，需要配置以下环境变量：

```bash
export CHAOXING_FORMS_SIGN=
export CHAOXING_FORMS_KEY=
export CHAOXING_APPROVE_SIGN=
export CHAOXING_APPROVE_KEY=
export CHAOXING_TOP_BUTTON_KEY=
export CHAOXING_FID=
export CHAOXING_SUBMIT_UID=199752933
export CHAOXING_CUSTOMER_FORM_ID=3412772
export CHAOXING_PROJECT_APPROVE_FORM_ID=300533
export CHAOXING_SALES_STATISTICS_FORM_ID=3412951
export CHAOXING_SALES_PERFORMANCE_FORM_ID=3423084
```

当前业务表：

| 业务表 | 配置项 | 当前值 |
| --- | --- | --- |
| 人员信息表 | `CHAOXING_CUSTOMER_FORM_ID` | `3412772` |
| 项目立项与报价审批表 | `CHAOXING_PROJECT_APPROVE_FORM_ID` | `300533` |
| 销售业绩明细表 | `CHAOXING_SALES_PERFORMANCE_FORM_ID` | `3423084` |
| 销售业绩统计表 | `CHAOXING_SALES_STATISTICS_FORM_ID` | `3412951` |

当前测试提交人：

| 用户 | 配置项 | 当前值 |
| --- | --- | --- |
| 章宇杰 | `CHAOXING_SUBMIT_UID` | `199752933` |

字段别名配置在 `src/main/resources/application.yml`。目前先按需求文档中的字段别名配置，例如
`customer_name`、`project_members`、`total_quote_amount`。如果平台实际字段别名或字段 ID 不一致，需要同步修改配置。

## 本地启动

```bash
mvn spring-boot:run
```

本地默认地址：

```text
http://localhost:8080
```

如果需要让超星服务器回调本机数据推送接口，可以使用 Cloudflare Quick Tunnel：

```bash
cloudflared tunnel --url http://localhost:8080
```

终端会生成一个 `https://*.trycloudflare.com` 公网地址。超星数据推送 URL 需要使用这个公网地址，
不要填写 `localhost`。

## 人员信息新增

```http
POST /api/customers
Content-Type: application/json

{
  "customerName": "测试客户",
  "creditCode": "91310000123456789X",
  "customerLevel": "战略",
  "mainContactId": 123456,
  "mainContactName": "张三",
  "industry": "信息技术",
  "uuid": "customer-test-001"
}
```

也可以使用手动集成测试新增一条人员信息表数据。运行前需要配置真实的表单 `sign`、`key` 和单位 ID。

```bash
mvn -Dtest=CustomerServiceIntegrationTest test
```

## 项目立项与报价审批

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

项目创建时：

- `project_code` 由后端生成，格式为 `PROJ-年份-UUID`。
- `customer_name`、`customer_level` 由超星表单数据联动默认值自动赋值。
- 子表单 `subtotal` 和主表 `total_quote_amount` 由超星表单默认值或公式自动计算。

## 客户报价统计顶部按钮

超星“项目立项与报价”数据管理页顶部按钮 URL：

```text
http://localhost:8080/customer-quote-stats/top
```

如果使用公网穿透，则改成：

```text
https://你的公网地址/customer-quote-stats/top
```

顶部按钮需要勾选“追加加密串”，并保证超星按钮里填写的 key 与后端配置
`CHAOXING_TOP_BUTTON_KEY` 一致。

后端验签规则与超星一致：

```text
enc = MD5([参数名=参数值]...[key])
```

其中参数按字母排序，`enc` 本身不参与计算。

## 审批通过生成销售业绩

超星“项目立项与报价审批表”的数据推送 URL：

```text
https://你的公网地址/api/chaoxing/project-approval/push
```

处理规则：

- 只有审批最终通过才生成销售业绩。
- 审批拒绝、撤销、删除、打回修改中不生成销售业绩。
- 业绩金额取项目立项与报价审批表中的 `total_quote_amount`。
- 销售人员取人员信息表中的 `main_contact`，写入销售业绩明细表的 `sales_contact`。
- 销售业绩明细表的 `performance_no` 是自动编号字段，后端不传。
- 后端使用 `PROJECT_APPROVAL_PERFORMANCE_审批数据ID` 作为业务唯一 ID，避免重复推送生成重复业绩。

当前推送入口会记录推送 payload 方便联调。正式环境建议增加推送签名校验，并避免在 INFO 日志打印完整业务数据。

## 销售业绩统计

```http
POST /api/sales-performance/statistics
Content-Type: application/json

{
  "customerLevel": "战略",
  "projectDateStart": "2026-07-01",
  "projectDateEnd": "2026-07-31",
  "salesName": "张"
}
```

所有筛选条件都可以为空。当前“销售姓名”按人员信息表中的主要联系人姓名进行模糊匹配。

## 注意事项

- `lib/office-api-sdk-1.22.2.jar` 是本地超星 SDK。
- 公式字段和自动编号字段通过 `new XxxField(alias, true)` 触发平台默认值或公式计算。
- 附件上传暂未实现，因为目前还没有附件上传接口以及 `objectId`、`resid` 等返回字段说明。
- 当前 `application.yml` 是本地联调配置，正式提交代码时不要提交真实 `sign`、`key` 或公网穿透地址。
