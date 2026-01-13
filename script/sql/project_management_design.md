# 项目管理模块数据库设计说明

## 一、表结构设计

### 1. project_management（项目管理主表）

**表说明**：存储项目的基本信息

**字段说明**：
- `id`: 主键ID，自增
- `name`: 项目名称，必填
- `description`: 项目描述，可选
- `status`: 项目状态，默认'active'
  - `active`: 进行中（有未完成的任务）
  - `completed`: 已完成（所有任务都已完成）
  - `archived`: 已归档（手动归档）
  - `cancelled`: 已取消（手动取消）
- `create_dept`: 创建部门ID
- `create_by`: 创建者用户名
- `create_by_id`: 创建人ID（用户ID）
- `create_time`: 创建时间
- `update_by`: 更新者
- `update_time`: 更新时间
- `remark`: 备注
- `del_flag`: 删除标志（0-存在，1-删除）
- `tenant_id`: 租户ID

**索引**：
- 主键：`id`
- 普通索引：`status`, `create_time`, `create_by`, `create_by_id`, `tenant_id`, `name`

### 2. project_management_tag（项目标签关联表）

**表说明**：存储项目与标签的多对多关联关系

**字段说明**：
- `id`: 主键ID，自增
- `project_id`: 项目ID，外键关联`project_management.id`
- `tag_name`: 标签名称
- `create_time`: 创建时间

**索引**：
- 主键：`id`
- 普通索引：`project_id`, `tag_name`
- 唯一索引：`(project_id, tag_name)` - 确保同一项目不会重复添加相同标签

**外键约束**：
- `project_id` → `project_management.id` (ON DELETE CASCADE, ON UPDATE CASCADE)

### 3. task_management（任务管理表 - 修改）

**新增字段**：
- `project_id`: 项目ID，外键关联`project_management.id`，可为NULL（允许任务不归属于任何项目）

**索引**：
- 新增索引：`idx_project_id` - 用于按项目查询任务

**外键约束**：
- `project_id` → `project_management.id` (ON DELETE SET NULL, ON UPDATE CASCADE)
  - **重要**：删除项目时，关联的任务不会被删除，只是`project_id`会被设置为NULL

## 二、表关联关系

```
project_management (1) ──< (N) task_management
     │
     │ (1)
     │
     └──< (N) project_management_tag
```

### 关联说明：

1. **项目 ↔ 任务**：一对多关系
   - 一个项目可以包含多个任务
   - 一个任务可以属于一个项目（也可以不属于任何项目，`project_id`为NULL）
   - 删除项目时，任务的`project_id`会被设置为NULL，任务本身不会被删除

2. **项目 ↔ 标签**：多对多关系
   - 一个项目可以有多个标签
   - 一个标签可以被多个项目使用
   - 通过`project_management_tag`中间表实现

## 三、业务逻辑说明

### 1. 项目状态计算逻辑

项目状态可以通过以下方式确定：

**自动计算**（推荐）：
- `active`: 项目下有未完成的任务（status != 'completed'）
- `completed`: 项目下所有任务都已完成（status = 'completed'）且没有其他状态的任务
- `archived`: 手动设置
- `cancelled`: 手动设置

**手动设置**：
- 用户可以手动将项目状态设置为`archived`或`cancelled`
- 手动设置的状态优先级高于自动计算

### 2. 统计字段计算

前端接口中需要的统计字段：
- `taskCount`: 项目下的任务总数（包括已删除的任务，根据`del_flag`过滤）
- `completedTaskCount`: 项目下已完成的任务数量

这些字段可以通过SQL查询实时计算，不需要在表中存储。

### 3. 查询优化建议

1. **获取项目列表时**：
   - 可以使用LEFT JOIN关联任务表，统计任务数量和已完成任务数量
   - 或者使用子查询，但要注意性能

2. **获取项目详情时**：
   - 需要关联查询项目的标签列表
   - 需要统计任务数量和已完成任务数量

3. **获取项目的任务列表时**：
   - 直接通过`project_id`查询`task_management`表
   - 注意过滤`del_flag = '0'`

## 四、数据迁移说明

如果已有`task_management`表，执行以下SQL添加`project_id`字段：

```sql
ALTER TABLE `task_management`
    ADD COLUMN `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目ID' AFTER `id`,
    ADD INDEX `idx_project_id`(`project_id`) USING BTREE COMMENT '项目ID索引',
    ADD CONSTRAINT `fk_task_management_project` FOREIGN KEY (`project_id`) REFERENCES `project_management` (`id`) ON DELETE SET NULL ON UPDATE CASCADE COMMENT '任务关联项目外键';
```

## 五、API接口对应关系

| 前端接口 | 后端需要实现的功能 | 涉及的表 |
|---------|------------------|---------|
| `/project/create` | 创建项目 | `project_management`, `project_management_tag` |
| `/project/list` | 查询项目列表（支持分页、状态筛选、关键词搜索） | `project_management`（需要关联统计任务数量） |
| `/project/update/{id}` | 更新项目 | `project_management`, `project_management_tag` |
| `/project/delete/{id}` | 删除项目（逻辑删除） | `project_management`（关联任务的`project_id`会被设置为NULL） |
| `/project/detail/{id}` | 获取项目详情 | `project_management`, `project_management_tag`（需要统计任务数量） |
| `/project/{projectId}/tasks` | 获取项目的任务列表 | `task_management`（通过`project_id`查询） |
| `/project/{projectId}/statistics` | 获取项目统计信息 | `task_management`（统计任务数量、已完成任务数量等） |

