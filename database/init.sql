-- 创建数据库
CREATE DATABASE IF NOT EXISTS finance_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE finance_db;

-- 会计科目初始化数据
-- 由于Spring Boot JPA会自动创建表结构，这里只插入初始数据

-- 插入一级会计科目（资产类）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('1001', '库存现金', '资产', '流动资产', 0, 1, '借方', true, '企业的库存现金', NOW(), NOW()),
('1002', '银行存款', '资产', '流动资产', 0, 1, '借方', true, '企业存入银行的各种存款', NOW(), NOW()),
('1122', '应收账款', '资产', '流动资产', 0, 1, '借方', true, '企业因销售商品、提供劳务等应向购货单位收取的款项', NOW(), NOW()),
('1123', '预付账款', '资产', '流动资产', 0, 1, '借方', true, '企业按照合同规定预付的款项', NOW(), NOW()),
('1401', '材料采购', '资产', '流动资产', 0, 1, '借方', true, '企业采用计划成本进行材料日常核算而购入材料的采购成本', NOW(), NOW()),
('1403', '原材料', '资产', '流动资产', 0, 1, '借方', true, '企业库存的各种材料', NOW(), NOW()),
('1405', '库存商品', '资产', '流动资产', 0, 1, '借方', true, '企业库存的各种商品的实际成本', NOW(), NOW()),
('1601', '固定资产', '资产', '非流动资产', 0, 1, '借方', true, '企业持有的固定资产原价', NOW(), NOW()),
('1602', '累计折旧', '资产', '非流动资产', 0, 1, '贷方', true, '企业固定资产的累计折旧', NOW(), NOW()),
('1701', '无形资产', '资产', '非流动资产', 0, 1, '借方', true, '企业持有的无形资产成本', NOW(), NOW());

-- 插入一级会计科目（负债类）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('2001', '短期借款', '负债', '流动负债', 0, 1, '贷方', true, '企业向银行或其他金融机构等借入的期限在1年以下的各种借款', NOW(), NOW()),
('2201', '应付账款', '负债', '流动负债', 0, 1, '贷方', true, '企业因购买材料、商品和接受劳务等应支付的款项', NOW(), NOW()),
('2202', '预收账款', '负债', '流动负债', 0, 1, '贷方', true, '企业按照合同规定预收的款项', NOW(), NOW()),
('2211', '应付职工薪酬', '负债', '流动负债', 0, 1, '贷方', true, '企业根据有关规定应付给职工的各种薪酬', NOW(), NOW()),
('2221', '应交税费', '负债', '流动负债', 0, 1, '贷方', true, '企业按照税法规定计算应交纳的各种税费', NOW(), NOW()),
('2501', '长期借款', '负债', '非流动负债', 0, 1, '贷方', true, '企业向银行或其他金融机构借入的期限在1年以上的各项借款', NOW(), NOW());

-- 插入一级会计科目（所有者权益类）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('4001', '实收资本', '所有者权益', '实收资本', 0, 1, '贷方', true, '企业接受投资者投入的实收资本', NOW(), NOW()),
('4002', '资本公积', '所有者权益', '资本公积', 0, 1, '贷方', true, '企业收到投资者出资额超出其在注册资本中所占份额的部分', NOW(), NOW()),
('4103', '盈余公积', '所有者权益', '留存收益', 0, 1, '贷方', true, '企业从净利润中提取的盈余公积', NOW(), NOW()),
('4104', '本年利润', '所有者权益', '留存收益', 0, 1, '贷方', true, '企业当期实现的净利润或发生的净亏损', NOW(), NOW()),
('4103', '利润分配', '所有者权益', '留存收益', 0, 1, '贷方', true, '企业利润的分配和历年分配后的结存余额', NOW(), NOW());

-- 插入一级会计科目（成本类）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('5001', '生产成本', '成本', '生产成本', 0, 1, '借方', true, '企业进行工业性生产所发生的各项生产成本', NOW(), NOW()),
('5101', '制造费用', '成本', '制造费用', 0, 1, '借方', true, '企业为生产产品和提供劳务而发生的各项间接费用', NOW(), NOW());

-- 插入一级会计科目（损益类 - 收入）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('6001', '主营业务收入', '损益', '收入', 0, 1, '贷方', true, '企业确认的销售商品、提供劳务等主营业务的收入', NOW(), NOW()),
('6051', '其他业务收入', '损益', '收入', 0, 1, '贷方', true, '企业确认的除主营业务活动以外的其他经营活动实现的收入', NOW(), NOW()),
('6111', '投资收益', '损益', '收入', 0, 1, '贷方', true, '企业对外投资所取得的收益', NOW(), NOW()),
('6301', '营业外收入', '损益', '收入', 0, 1, '贷方', true, '企业发生的与其日常活动无直接关系的各项利得', NOW(), NOW());

-- 插入一级会计科目（损益类 - 费用）
INSERT INTO account_subject (code, name, type, category, parent_id, level, direction, enabled, description, created_at, updated_at) 
VALUES 
('6401', '主营业务成本', '损益', '费用', 0, 1, '借方', true, '企业确认销售商品、提供劳务等主营业务收入时应结转的成本', NOW(), NOW()),
('6402', '其他业务成本', '损益', '费用', 0, 1, '借方', true, '企业确认的除主营业务活动以外的其他经营活动所发生的支出', NOW(), NOW()),
('6403', '税金及附加', '损益', '费用', 0, 1, '借方', true, '企业经营活动发生的消费税、城市维护建设税、教育费附加等相关税费', NOW(), NOW()),
('6601', '销售费用', '损益', '费用', 0, 1, '借方', true, '企业销售商品和材料、提供劳务的过程中发生的各种费用', NOW(), NOW()),
('6602', '管理费用', '损益', '费用', 0, 1, '借方', true, '企业为组织和管理企业生产经营所发生的管理费用', NOW(), NOW()),
('6603', '财务费用', '损益', '费用', 0, 1, '借方', true, '企业为筹集生产经营所需资金等而发生的筹资费用', NOW(), NOW()),
('6701', '营业外支出', '损益', '费用', 0, 1, '借方', true, '企业发生的与其日常活动无直接关系的各项损失', NOW(), NOW()),
('6801', '所得税费用', '损益', '费用', 0, 1, '借方', true, '企业确认的应从当期利润总额中扣除的所得税费用', NOW(), NOW());

-- 插入示例企业数据
INSERT INTO company (name, scale, registered_capital, industry, address, legal_person, contact_phone, email, tax_number, created_at, updated_at)
VALUES 
('示例科技有限公司', '中型', 5000000.00, '软件和信息技术服务业', '北京市海淀区中关村大街1号', '张三', '010-12345678', 'contact@example.com', '91110108MA01234567', NOW(), NOW());

-- 插入示例供应商数据
INSERT INTO supplier (code, name, contact_person, contact_phone, email, address, tax_number, bank_name, bank_account, category, status, remark, created_at, updated_at)
VALUES 
('SUP001', '北京办公用品有限公司', '李经理', '010-87654321', 'liming@office.com', '北京市朝阳区建国路88号', '91110105MA01234568', '中国工商银行北京分行', '6222021001234567890', '办公用品', '启用', '主要供应商之一', NOW(), NOW()),
('SUP002', '上海电子设备公司', '王经理', '021-12345678', 'wangli@electronic.com', '上海市浦东新区世纪大道200号', '91310115MA01234569', '中国建设银行上海分行', '6217001234567890123', '电子设备', '启用', '电子设备供应商', NOW(), NOW());

-- 插入示例客户数据
INSERT INTO customer (code, name, contact_person, contact_phone, email, address, tax_number, bank_name, bank_account, category, status, remark, created_at, updated_at)
VALUES 
('CUS001', '深圳创新科技公司', '赵总', '0755-12345678', 'zhao@innovation.com', '深圳市南山区科技园南区', '91440300MA01234570', '中国银行深圳分行', '6216611234567890123', '大客户', '启用', '重要客户', NOW(), NOW()),
('CUS002', '广州商贸有限公司', '钱经理', '020-87654321', 'qian@trade.com', '广州市天河区珠江新城', '91440101MA01234571', '招商银行广州分行', '6225881234567890123', '普通客户', '启用', '长期合作客户', NOW(), NOW());

-- 插入示例员工数据
INSERT INTO employee (employee_no, name, department, position, phone, email, hire_date, status, bank_account, remark, created_at, updated_at)
VALUES 
('EMP001', '张三', '财务部', '会计', '13800138001', 'zhangsan@company.com', '2023-01-15', '在职', '6222021001111111111', '财务人员', NOW(), NOW()),
('EMP002', '李四', '销售部', '销售经理', '13800138002', 'lisi@company.com', '2023-03-20', '在职', '6222021002222222222', '销售骨干', NOW(), NOW()),
('EMP003', '王五', '技术部', '软件工程师', '13800138003', 'wangwu@company.com', '2023-06-01', '在职', '6222021003333333333', '技术人员', NOW(), NOW());










