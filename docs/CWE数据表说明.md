# CWE 数据表说明

## 概述

CWE（Common Weakness Enumeration）数据采用三层架构设计，从底层到上层依次为：**原始数据层** → **分类层** → **聚类层**。

## 三层架构

### 底层：原始数据层

#### `cwe_reference` - CWE 标准参考表

**作用**：存储从 CWE 官方 CSV 文件导入的完整 CWE 数据，作为所有分类和聚类的基础数据源。

**关键字段**：
- `cwe_id`: CWE 编号（如 `CWE-89`），唯一标识
- `name_en` / `name_zh`: 英文/中文名称
- `description_en` / `description_zh`: 英文/中文描述
- `weakness_abstraction`: 弱点抽象层级（Base/Variant/Class/Compound）
- `status`: 状态（Draft/Incomplete/Stable/Deprecated）

**数据来源**：`STANDARDS/CWE.csv`，通过 `import_cwe_reference.py` 脚本导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Bo, Vo

---

### 中层：分类层

分类层存储基于规则的分类结果，包括标准分类、影响分类和层级关系。

#### `cwe_standard_type` - 标准分类类型字典表

**作用**：定义标准分类的类型（如 OWASP Top 10、CWE Top 25 等）。

**关键字段**：
- `type_code`: 标准类型代码（如 `owasp_top10`）
- `type_name`: 标准类型名称（如 `OWASP Top 10`）
- `version`: 标准版本（如 `2021`, `2023`）

**数据来源**：从 `cwe_classification_output.json` 自动提取，通过 `import_cwe_classification.py` 脚本导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Bo, Vo

#### `cwe_standard_mapping` - CWE 标准分类映射表

**作用**：存储 CWE 与标准分类的映射关系（从 Taxonomy Mappings 解析）。

**关键字段**：
- `cwe_id`: CWE 编号（关联 `cwe_reference.cwe_id`）
- `type_code`: 标准类型代码（关联 `cwe_standard_type.type_code`）
- `entry_code`: 标准条目代码（如 `A01`, `A02`）
- `entry_name`: 标准条目名称

**数据来源**：`classify_cwe_data.py` 脚本生成，通过 `import_cwe_classification.py` 导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Vo

#### `cwe_impact_mapping` - CWE 影响类型映射表

**作用**：存储 CWE 与影响类型的映射关系（从 Common Consequences 解析，CIA 三元组）。

**关键字段**：
- `cwe_id`: CWE 编号
- `impact_type`: 影响类型（`confidentiality` / `integrity` / `availability`）

**数据来源**：`classify_cwe_data.py` 脚本生成，通过 `import_cwe_classification.py` 导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Vo

#### `cwe_hierarchy` - CWE 层级关系表

**作用**：存储 CWE 之间的层级关系（从 Related Weaknesses 解析）。

**关键字段**：
- `cwe_id`: 子 CWE 编号
- `parent_cwe_id`: 父 CWE 编号
- `relationship_type`: 关系类型（`ChildOf` / `CanPrecede` / `CanFollow` 等）

**数据来源**：`classify_cwe_data.py` 脚本生成，通过 `import_cwe_classification.py` 导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Vo

#### `cwe_business_category` / `cwe_business_category_mapping` - 业务分类表（已废弃）

**说明**：这两个表在 SQL 文件中存在，但**实际业务分类由聚类层提供**（见 `cwe_cluster.category_code`）。

**原因**：根据架构设计，业务分类通过语义聚类直接定义，不再需要独立的业务分类表。

**后端代码**：❌ 未实现（无需实现）

---

### 上层：聚类层

聚类层存储基于语义相似度的聚类结果，通过 LLM 解释生成业务分类。

#### `cwe_cluster` - CWE 聚类主表

**作用**：存储聚类的基本信息和 LLM 解释结果，**直接提供业务分类**。

**关键字段**：
- `cluster_id`: 聚类ID（算法生成的编号）
- `cluster_method`: 聚类方法（`kmeans` / `hierarchical` / `dbscan`）
- `cluster_name_zh` / `cluster_name_en`: 聚类中文/英文名称（LLM 生成）
- `category_code`: **分类代码**（LLM 生成，如 `injection`），**这就是业务分类**
- `description`: 聚类描述（LLM 生成）
- `keywords`: 关键词列表（JSON 数组格式，LLM 生成）
- `cwe_count`: 包含的 CWE 数量
- `silhouette_score`: 轮廓系数（聚类质量指标）
- `llm_interpretation`: LLM 完整解释结果（JSON 格式）

**数据来源**：`cluster_cwe_data.py` 脚本生成，通过 `import_cwe_clustering.py` 导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Bo, Vo

#### `cwe_cluster_mapping` - CWE 聚类映射表

**作用**：存储 CWE 与聚类的映射关系（一个 CWE 只能属于一个聚类）。

**关键字段**：
- `cwe_id`: CWE 编号（关联 `cwe_reference.cwe_id`）
- `cluster_id`: 聚类ID（关联 `cwe_cluster.cluster_id`）
- `cluster_method`: 聚类方法（关联 `cwe_cluster.cluster_method`）
- `distance_to_center`: 到聚类中心的距离（可选）

**数据来源**：`cluster_cwe_data.py` 脚本生成，通过 `import_cwe_clustering.py` 导入

**后端代码**：✅ Entity, Mapper, Service, Controller, Vo

---

## 数据流转关系

```
CWE.csv (原始数据)
    ↓
cwe_reference (底层：原始数据)
    ↓
    ├─→ classify_cwe_data.py (规则匹配)
    │       ↓
    │   cwe_standard_mapping (标准分类)
    │   cwe_impact_mapping (影响分类)
    │   cwe_hierarchy (层级关系)
    │
    └─→ cluster_cwe_data.py (语义聚类)
            ↓
        cwe_cluster (聚类主表，包含业务分类)
        cwe_cluster_mapping (聚类映射)
```

## 关键设计决策

1. **业务分类由聚类提供**：不再使用独立的 `cwe_business_category` 表，业务分类直接存储在 `cwe_cluster.category_code` 中。

2. **分类与聚类分离**：
   - **分类层**：基于规则的精确匹配（标准分类、影响分类、层级关系）
   - **聚类层**：基于语义相似度的智能分组（业务分类）

3. **三层架构的优势**：
   - **底层**：提供标准化的原始数据
   - **中层**：提供精确的规则匹配结果
   - **上层**：提供语义化的业务分类

## 后端代码位置

- **Entity**: `ruoyi-modules-api/ruoyi-knowledge-api/src/main/java/org/ruoyi/domain/`
- **Mapper**: `ruoyi-modules-api/ruoyi-knowledge-api/src/main/java/org/ruoyi/mapper/`
- **Service**: `ruoyi-modules-api/ruoyi-knowledge-api/src/main/java/org/ruoyi/service/`
- **Controller**: `ruoyi-modules/ruoyi-chat/src/main/java/org/ruoyi/chat/controller/knowledge/`

## 相关脚本

- **导入原始数据**: `script/sql/import_cwe_reference.py`
- **分类脚本**: `script/sql/classify_cwe_data.py`
- **聚类脚本**: `script/sql/cluster_cwe_data.py`
- **导入分类结果**: `script/sql/import_cwe_classification.py`
- **导入聚类结果**: `script/sql/import_cwe_clustering.py`

## 注意事项

1. **业务分类查询**：应通过 `cwe_cluster.category_code` 查询，而非 `cwe_business_category`。

2. **数据更新**：分类和聚类结果通过脚本生成，更新时需要重新运行相应脚本并导入 SQL。

3. **聚类方法**：支持多种聚类方法（kmeans/hierarchical/dbscan），查询时需要指定 `cluster_method`。
