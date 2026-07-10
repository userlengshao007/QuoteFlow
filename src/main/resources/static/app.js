(function () {
    var customerForm = document.getElementById("customerForm");
    var submitCustomerButton = document.getElementById("submitCustomerButton");
    var resetCustomerButton = document.getElementById("resetCustomerButton");
    var customerResultText = document.getElementById("customerResultText");

    var projectForm = document.getElementById("projectForm");
    var submitProjectButton = document.getElementById("submitProjectButton");
    var resetProjectButton = document.getElementById("resetProjectButton");
    var projectResultText = document.getElementById("projectResultText");
    var addMemberButton = document.getElementById("addMemberButton");
    var addQuoteButton = document.getElementById("addQuoteButton");
    var reloadCustomerButton = document.getElementById("reloadCustomerButton");
    var projectCustomerId = document.getElementById("projectCustomerId");
    var memberRows = document.getElementById("memberRows");
    var quoteRows = document.getElementById("quoteRows");
    var quoteTotalText = document.getElementById("quoteTotalText");

    function trimValue(formData, fieldName) {
        return String(formData.get(fieldName) || "").trim();
    }

    function trimInput(row, selector) {
        return String(row.querySelector(selector).value || "").trim();
    }

    function buildUuid(prefix) {
        if (window.crypto && typeof window.crypto.randomUUID === "function") {
            return prefix + "-" + window.crypto.randomUUID();
        }
        return prefix + "-" + Date.now();
    }

    function formatDateTime(value) {
        if (!value) {
            return "";
        }
        return value.replace("T", " ");
    }

    function setResult(resultElement, type, message) {
        resultElement.className = type;
        resultElement.textContent = message;
    }

    function formatErrorMessage(responseBody) {
        if (responseBody && responseBody.message) {
            return responseBody.message;
        }
        return "提交失败，请稍后重试";
    }

    function postJson(url, requestBody) {
        return fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestBody)
        }).then(function (response) {
            return response.json().then(function (responseBody) {
                return {
                    ok: response.ok,
                    body: responseBody
                };
            });
        });
    }

    function getJson(url) {
        return fetch(url).then(function (response) {
            return response.json().then(function (responseBody) {
                return {
                    ok: response.ok,
                    body: responseBody
                };
            });
        });
    }

    function buildCustomerRequestBody(formData) {
        return {
            customerName: trimValue(formData, "customerName"),
            creditCode: trimValue(formData, "creditCode").toUpperCase(),
            customerLevel: trimValue(formData, "customerLevel"),
            mainContactId: Number(trimValue(formData, "mainContactId")),
            mainContactName: trimValue(formData, "mainContactName"),
            industry: trimValue(formData, "industry"),
            uuid: buildUuid("customer")
        };
    }

    function validateCustomerRequestBody(requestBody) {
        if (!requestBody.customerName) {
            return "请填写客户名称";
        }
        if (!requestBody.creditCode) {
            return "请填写统一信用代码";
        }
        if (!requestBody.customerLevel) {
            return "请选择客户级别";
        }
        if (!Number.isFinite(requestBody.mainContactId) || requestBody.mainContactId <= 0) {
            return "请填写有效的主要联系人 UID";
        }
        if (!requestBody.mainContactName) {
            return "请填写主要联系人姓名";
        }
        if (!requestBody.industry) {
            return "请填写所属行业";
        }
        return "";
    }

    function formatCustomerSuccessMessage(responseBody, requestBody) {
        var data = responseBody.data || {};
        var submitId = data.formUserId || data.repeatFormUserId || "未返回";
        return JSON.stringify({
            message: responseBody.message || "提交成功",
            formUserId: submitId,
            request: requestBody
        }, null, 2);
    }

    function resetCustomerOptions(message) {
        projectCustomerId.innerHTML = "";
        var option = document.createElement("option");
        option.value = "";
        option.textContent = message;
        projectCustomerId.appendChild(option);
    }

    function fillCustomerOptions(customerList) {
        projectCustomerId.innerHTML = "";
        if (!customerList.length) {
            resetCustomerOptions("暂无客户，请先新增人员信息");
            return;
        }
        var placeholderOption = document.createElement("option");
        placeholderOption.value = "";
        placeholderOption.textContent = "请选择客户";
        projectCustomerId.appendChild(placeholderOption);
        customerList.forEach(function (customer) {
            var option = document.createElement("option");
            option.value = customer.customerId || "";
            option.textContent = [
                customer.customerName || "未命名客户",
                customer.customerLevel || "未设置级别",
                customer.customerId || "无客户ID"
            ].join(" / ");
            option.disabled = !customer.customerId;
            projectCustomerId.appendChild(option);
        });
    }

    function loadCustomerOptions() {
        resetCustomerOptions("加载客户列表中...");
        getJson("/api/customers")
            .then(function (result) {
                if (!result.ok || !result.body.success) {
                    resetCustomerOptions("客户列表加载失败");
                    setResult(projectResultText, "error", formatErrorMessage(result.body));
                    return;
                }
                fillCustomerOptions(result.body.data || []);
            })
            .catch(function () {
                resetCustomerOptions("客户列表加载失败");
                setResult(projectResultText, "error", "客户列表加载失败，请确认后端服务已启动且表单配置完整");
            });
    }

    function createMemberRow(uid, name) {
        var row = document.createElement("div");
        row.className = "entry-row member-row";
        row.innerHTML = [
            '<label class="field compact-field">',
            '<span>成员 UID</span>',
            '<input class="member-uid" type="number" inputmode="numeric" value="' + (uid || "") + '" required>',
            '</label>',
            '<label class="field compact-field">',
            '<span>成员姓名</span>',
            '<input class="member-name" type="text" value="' + (name || "") + '" required>',
            '</label>',
            '<button class="icon-button remove-row-button" type="button" aria-label="删除成员">×</button>'
        ].join("");
        row.querySelector(".remove-row-button").addEventListener("click", function () {
            row.remove();
        });
        memberRows.appendChild(row);
    }

    function createQuoteRow(productName, specification, quantity, unitPrice) {
        var row = document.createElement("div");
        row.className = "entry-row quote-row";
        row.innerHTML = [
            '<label class="field compact-field">',
            '<span>产品名称</span>',
            '<input class="quote-product-name" type="text" value="' + (productName || "") + '" required>',
            '</label>',
            '<label class="field compact-field">',
            '<span>规格型号</span>',
            '<input class="quote-specification" type="text" value="' + (specification || "") + '">',
            '</label>',
            '<label class="field compact-field">',
            '<span>数量</span>',
            '<input class="quote-quantity" type="number" min="0.01" step="0.01" value="' + (quantity || "1") + '" required>',
            '</label>',
            '<label class="field compact-field">',
            '<span>单价</span>',
            '<input class="quote-unit-price" type="number" min="0.01" step="0.01" value="' + (unitPrice || "") + '" required>',
            '</label>',
            '<button class="icon-button remove-row-button" type="button" aria-label="删除明细">×</button>'
        ].join("");
        row.querySelector(".remove-row-button").addEventListener("click", function () {
            row.remove();
            updateQuoteTotal();
        });
        row.querySelector(".quote-quantity").addEventListener("input", updateQuoteTotal);
        row.querySelector(".quote-unit-price").addEventListener("input", updateQuoteTotal);
        quoteRows.appendChild(row);
        updateQuoteTotal();
    }

    function collectProjectMembers() {
        return Array.prototype.map.call(memberRows.querySelectorAll(".member-row"), function (row) {
            return {
                uid: Number(trimInput(row, ".member-uid")),
                name: trimInput(row, ".member-name")
            };
        });
    }

    function collectQuoteDetails() {
        return Array.prototype.map.call(quoteRows.querySelectorAll(".quote-row"), function (row) {
            return {
                productName: trimInput(row, ".quote-product-name"),
                specification: trimInput(row, ".quote-specification"),
                quantity: Number(trimInput(row, ".quote-quantity")),
                unitPrice: Number(trimInput(row, ".quote-unit-price"))
            };
        });
    }

    function calculateQuoteTotal() {
        return collectQuoteDetails().reduce(function (total, detail) {
            if (!Number.isFinite(detail.quantity) || !Number.isFinite(detail.unitPrice)) {
                return total;
            }
            return total + detail.quantity * detail.unitPrice;
        }, 0);
    }

    function updateQuoteTotal() {
        quoteTotalText.textContent = calculateQuoteTotal().toFixed(2);
    }

    function buildProjectRequestBody(formData) {
        return {
            customerId: trimValue(formData, "customerId"),
            projectDate: formatDateTime(trimValue(formData, "projectDate")),
            expectedFinishDate: formatDateTime(trimValue(formData, "expectedFinishDate")),
            projectMembers: collectProjectMembers(),
            quoteDetails: collectQuoteDetails(),
            uuid: buildUuid("project")
        };
    }

    function validateProjectRequestBody(requestBody) {
        if (!requestBody.customerId) {
            return "请填写客户 ID";
        }
        if (!requestBody.projectDate) {
            return "请选择立项日期";
        }
        if (!requestBody.expectedFinishDate) {
            return "请选择预计完成日期";
        }
        if (!requestBody.projectMembers.length) {
            return "请至少添加一位项目成员";
        }
        for (var i = 0; i < requestBody.projectMembers.length; i++) {
            if (!Number.isFinite(requestBody.projectMembers[i].uid) || requestBody.projectMembers[i].uid <= 0
                    || !requestBody.projectMembers[i].name) {
                return "请补全项目成员信息";
            }
        }
        if (!requestBody.quoteDetails.length) {
            return "请至少添加一条报价明细";
        }
        for (var j = 0; j < requestBody.quoteDetails.length; j++) {
            var detail = requestBody.quoteDetails[j];
            if (!detail.productName || !Number.isFinite(detail.quantity) || detail.quantity <= 0
                    || !Number.isFinite(detail.unitPrice) || detail.unitPrice <= 0) {
                return "请补全报价明细";
            }
        }
        return "";
    }

    function formatProjectSuccessMessage(responseBody, requestBody) {
        var data = responseBody.data || {};
        return JSON.stringify({
            message: responseBody.message || "提交成功",
            formUserId: data.formUserId || "未返回",
            detailUrl: data.detailUrl || "",
            request: requestBody
        }, null, 2);
    }

    function resetProjectForm() {
        projectForm.reset();
        memberRows.innerHTML = "";
        quoteRows.innerHTML = "";
        createMemberRow("199752933", "章宇杰");
        createQuoteRow("", "", "1", "");
        updateQuoteTotal();
        setResult(projectResultText, "", "等待提交");
    }

    customerForm.addEventListener("submit", function (event) {
        event.preventDefault();
        var requestBody = buildCustomerRequestBody(new FormData(customerForm));
        var validationMessage = validateCustomerRequestBody(requestBody);
        if (validationMessage) {
            setResult(customerResultText, "error", validationMessage);
            return;
        }

        submitCustomerButton.disabled = true;
        setResult(customerResultText, "", "提交中...");

        postJson("/api/customers", requestBody)
            .then(function (result) {
                if (!result.ok || !result.body.success) {
                    setResult(customerResultText, "error", formatErrorMessage(result.body));
                    return;
                }
                setResult(customerResultText, "success", formatCustomerSuccessMessage(result.body, requestBody));
                loadCustomerOptions();
            })
            .catch(function () {
                setResult(customerResultText, "error", "网络请求失败，请确认后端服务已启动");
            })
            .finally(function () {
                submitCustomerButton.disabled = false;
            });
    });

    resetCustomerButton.addEventListener("click", function () {
        customerForm.reset();
        setResult(customerResultText, "", "等待提交");
    });

    addMemberButton.addEventListener("click", function () {
        createMemberRow("", "");
    });

    addQuoteButton.addEventListener("click", function () {
        createQuoteRow("", "", "1", "");
    });

    reloadCustomerButton.addEventListener("click", loadCustomerOptions);

    projectForm.addEventListener("submit", function (event) {
        event.preventDefault();
        var requestBody = buildProjectRequestBody(new FormData(projectForm));
        var validationMessage = validateProjectRequestBody(requestBody);
        if (validationMessage) {
            setResult(projectResultText, "error", validationMessage);
            return;
        }

        submitProjectButton.disabled = true;
        setResult(projectResultText, "", "提交中...");

        postJson("/api/projects", requestBody)
            .then(function (result) {
                if (!result.ok || !result.body.success) {
                    setResult(projectResultText, "error", formatErrorMessage(result.body));
                    return;
                }
                setResult(projectResultText, "success", formatProjectSuccessMessage(result.body, requestBody));
            })
            .catch(function () {
                setResult(projectResultText, "error", "网络请求失败，请确认后端服务已启动");
            })
            .finally(function () {
                submitProjectButton.disabled = false;
            });
    });

    resetProjectButton.addEventListener("click", resetProjectForm);

    resetProjectForm();
    loadCustomerOptions();
}());
