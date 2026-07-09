# QuoteFlow

QuoteFlow is a Spring Boot backend that wraps the Chaoxing office form and approval SDK.

## Current Scope

- Create customer form data.
- Submit project quote approval data.
- Search selected project data and summarize quote amount by customer.
- Create sales performance statistics data.

## Required Configuration

Set these values before calling real Chaoxing APIs:

```bash
export CHAOXING_FORMS_SIGN=
export CHAOXING_FORMS_KEY=
export CHAOXING_APPROVE_SIGN=
export CHAOXING_APPROVE_KEY=
export CHAOXING_FID=
export CHAOXING_SUBMIT_UID=
export CHAOXING_CUSTOMER_FORM_ID=3412772
export CHAOXING_PROJECT_APPROVE_FORM_ID=
export CHAOXING_SALES_STATISTICS_FORM_ID=
```

Current table scope:

| Business table | Config key | Current value |
| --- | --- | --- |
| 人员信息表 | `CHAOXING_CUSTOMER_FORM_ID` | `3412772` |
| 项目立项与报价表 | `CHAOXING_PROJECT_APPROVE_FORM_ID` | waiting for creation |
| 销售业绩统计表 | `CHAOXING_SALES_STATISTICS_FORM_ID` | waiting for creation |

Field aliases are configured in `src/main/resources/application.yml`. They currently follow the requirement
document, such as `customer_name`, `project_members`, and `total_quote_amount`. Replace them if the platform uses
different aliases or numeric field ids.

## First Integration Step

Start with customer creation, because it only depends on the normal form SDK:

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

After customer creation succeeds, test project approval:

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

## Notes

- `lib/office-api-sdk-1.22.2.jar` is the local Chaoxing SDK jar.
- Formula and auto-number fields are sent with `new XxxField(alias, true)` so the platform can calculate defaults.
- Attachment upload is not implemented yet because the upload API and returned `objectId` or `resid` are not available.
- Approval callback or data-push handling is not implemented yet because callback parameters and signature rules are not available.
