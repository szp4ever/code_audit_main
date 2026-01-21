# Flask端文件夹上传处理说明

## 概述

当Java后端上传文件夹时，会将文件的相对路径作为文件名传递给Flask。Flask端需要根据相对路径重建目录结构，以便正确处理代码文件。

## Java端传递的数据格式

Java端会通过 `multipart/form-data` 方式发送以下数据：

- `task_type`: 任务类型（字符串）
- `description`: 任务描述（字符串，可选）
- `files`: 文件数组，每个文件的 `filename` 字段包含相对路径

**示例：**
- 单文件上传：`filename = "App.java"`
- 文件夹上传：`filename = "src/main/java/App.java"`

## Flask端修改方案

### 方案一：从文件名中提取相对路径（推荐）

这是最简单的方式，直接使用文件名作为相对路径。

```python
from flask import Flask, request, jsonify
import os
import tempfile
import shutil
from werkzeug.utils import secure_filename

app = Flask(__name__)

@app.route('/api/process', methods=['POST'])
def process_task():
    """
    处理任务接口
    接收文件并保持目录结构
    """
    try:
        # 获取任务参数
        task_type = request.form.get('task_type')
        description = request.form.get('description', '')
        
        # 获取所有文件
        files = request.files.getlist('files')
        
        if not files:
            return jsonify({
                'code': 400,
                'msg': '没有上传文件',
                'data': None
            }), 400
        
        # 创建临时目录用于存储文件
        temp_dir = tempfile.mkdtemp(prefix='task_')
        
        try:
            # 处理每个文件，保持目录结构
            file_paths = []
            for file in files:
                if file.filename == '':
                    continue
                
                # filename 就是相对路径，如 "src/main/java/App.java"
                relative_path = file.filename
                
                # 构建完整路径
                full_path = os.path.join(temp_dir, relative_path)
                
                # 确保目录存在
                os.makedirs(os.path.dirname(full_path), exist_ok=True)
                
                # 保存文件
                file.save(full_path)
                file_paths.append({
                    'relative_path': relative_path,
                    'full_path': full_path
                })
                
                print(f"保存文件: {relative_path} -> {full_path}")
            
            # 现在 temp_dir 目录下已经包含了完整的目录结构
            # 例如：
            # temp_dir/
            #   └── src/
            #       └── main/
            #           └── java/
            #               └── App.java
            
            # 在这里进行你的代码分析处理
            # 例如：调用代码分析工具，传入 temp_dir 作为项目根目录
            issues = analyze_code(temp_dir, task_type, description)
            
            # 返回结果
            return jsonify({
                'code': 200,
                'msg': '处理成功',
                'data': {
                    'fileName': 'project',  # 项目名称
                    'totalIssues': len(issues),
                    'issues': issues
                }
            })
            
        finally:
            # 清理临时目录
            shutil.rmtree(temp_dir, ignore_errors=True)
            
    except Exception as e:
        return jsonify({
            'code': 500,
            'msg': f'处理失败: {str(e)}',
            'data': None
        }), 500


def analyze_code(project_dir, task_type, description):
    """
    分析代码的函数
    这里需要根据你的实际需求实现
    
    Args:
        project_dir: 项目根目录路径
        task_type: 任务类型
        description: 任务描述
    
    Returns:
        issues列表，格式如下：
        [
            {
                'issueName': '问题名称',
                'severity': 'High',  # Low, Medium, High, Critical
                'lineNumber': '45',  # 可以是单个行号或范围，如 "45-48"
                'description': '问题描述',
                'fixSuggestion': '修复建议'
            },
            ...
        ]
    """
    issues = []
    
    # 示例：遍历所有代码文件
    for root, dirs, files in os.walk(project_dir):
        for file in files:
            file_path = os.path.join(root, file)
            
            # 计算相对路径（相对于项目根目录）
            relative_path = os.path.relpath(file_path, project_dir)
            
            # 根据任务类型进行分析
            if task_type == 'code_standard_check':
                # 代码规范检查
                file_issues = check_code_standard(file_path, relative_path)
                issues.extend(file_issues)
            elif task_type == 'data_security_audit':
                # 数据安全审计
                file_issues = audit_data_security(file_path, relative_path)
                issues.extend(file_issues)
            # ... 其他任务类型
    
    return issues


def check_code_standard(file_path, relative_path):
    """
    代码规范检查示例
    """
    issues = []
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            
            for line_num, line in enumerate(lines, start=1):
                # 示例检查：行长度
                if len(line.rstrip()) > 120:
                    issues.append({
                        'issueName': '行长度过长',
                        'severity': 'Low',
                        'lineNumber': str(line_num),
                        'description': f'文件 {relative_path} 第 {line_num} 行超过120个字符',
                        'fixSuggestion': '将长行拆分为多行'
                    })
                
                # 示例检查：TODO注释
                if 'TODO' in line or 'FIXME' in line:
                    issues.append({
                        'issueName': '存在TODO/FIXME注释',
                        'severity': 'Medium',
                        'lineNumber': str(line_num),
                        'description': f'文件 {relative_path} 第 {line_num} 行包含待办事项',
                        'fixSuggestion': '完成待办事项或移除注释'
                    })
    
    except Exception as e:
        print(f"读取文件失败 {file_path}: {e}")
    
    return issues


def audit_data_security(file_path, relative_path):
    """
    数据安全审计示例
    """
    issues = []
    
    # 示例：检查硬编码的密码、密钥等
    security_patterns = [
        ('password', '密码'),
        ('secret', '密钥'),
        ('api_key', 'API密钥'),
        ('token', '令牌')
    ]
    
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            
            for line_num, line in enumerate(lines, start=1):
                line_lower = line.lower()
                for pattern, desc in security_patterns:
                    if pattern in line_lower and '=' in line:
                        # 检查是否是硬编码
                        if any(char.isdigit() or char.isalpha() for char in line.split('=')[1].strip()[:20]):
                            issues.append({
                                'issueName': f'可能的硬编码{desc}',
                                'severity': 'High',
                                'lineNumber': str(line_num),
                                'description': f'文件 {relative_path} 第 {line_num} 行可能包含硬编码的{desc}',
                                'fixSuggestion': '将敏感信息移到配置文件或环境变量中'
                            })
    
    except Exception as e:
        print(f"读取文件失败 {file_path}: {e}")
    
    return issues


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5004, debug=True)
```

### 方案二：单独传递相对路径数组（可选）

如果Flask端需要单独接收相对路径数组，可以修改Java端代码，取消注释以下部分：

```java
// 在 TaskProcessingServiceImpl.java 中
for (String relativePath : relativePaths) {
    bodyBuilder.part("relative_paths", relativePath);
}
```

然后在Flask端接收：

```python
@app.route('/api/process', methods=['POST'])
def process_task():
    # 获取相对路径数组（如果单独传递）
    relative_paths = request.form.getlist('relative_paths')
    
    files = request.files.getlist('files')
    
    # 将文件与相对路径对应
    for i, file in enumerate(files):
        if i < len(relative_paths):
            relative_path = relative_paths[i]
        else:
            relative_path = file.filename
        
        # 使用 relative_path 保存文件
        full_path = os.path.join(temp_dir, relative_path)
        os.makedirs(os.path.dirname(full_path), exist_ok=True)
        file.save(full_path)
```

## 重要注意事项

### 1. 路径分隔符

- **Java端**：使用 `/` 作为路径分隔符（如 `src/main/java/App.java`）
- **Flask端（Windows）**：`os.path.join()` 会自动处理路径分隔符
- **Flask端（Linux/Mac）**：使用 `/` 作为路径分隔符

### 2. 文件名安全处理

如果担心文件名安全问题，可以使用 `secure_filename`：

```python
from werkzeug.utils import secure_filename

# 但要注意，secure_filename 可能会改变路径结构
# 所以建议直接使用相对路径，因为Java端已经验证了文件类型
relative_path = file.filename  # 直接使用，因为都是代码文件
```

### 3. 临时目录清理

确保在处理完成后清理临时目录，避免磁盘空间浪费：

```python
try:
    # 处理文件
    ...
finally:
    # 确保清理临时目录
    shutil.rmtree(temp_dir, ignore_errors=True)
```

### 4. 文件编码

代码文件通常使用 UTF-8 编码，但也要处理其他编码：

```python
encodings = ['utf-8', 'gbk', 'gb2312', 'latin-1']
for encoding in encodings:
    try:
        with open(file_path, 'r', encoding=encoding) as f:
            content = f.read()
        break
    except UnicodeDecodeError:
        continue
```

## 完整示例：处理文件夹上传

```python
from flask import Flask, request, jsonify
import os
import tempfile
import shutil

app = Flask(__name__)

@app.route('/api/process', methods=['POST'])
def process_task():
    """
    处理任务接口 - 支持文件夹上传
    """
    try:
        task_type = request.form.get('task_type')
        description = request.form.get('description', '')
        files = request.files.getlist('files')
        
        if not files:
            return jsonify({
                'code': 400,
                'msg': '没有上传文件',
                'data': None
            }), 400
        
        # 创建临时目录
        temp_dir = tempfile.mkdtemp(prefix='task_')
        
        try:
            # 保存所有文件，保持目录结构
            saved_files = []
            for file in files:
                if file.filename == '':
                    continue
                
                # filename 就是相对路径
                relative_path = file.filename
                full_path = os.path.join(temp_dir, relative_path)
                
                # 创建目录
                os.makedirs(os.path.dirname(full_path), exist_ok=True)
                
                # 保存文件
                file.save(full_path)
                saved_files.append(relative_path)
            
            print(f"已保存 {len(saved_files)} 个文件到 {temp_dir}")
            
            # 执行代码分析
            issues = analyze_project(temp_dir, task_type, description)
            
            return jsonify({
                'code': 200,
                'msg': '处理成功',
                'data': {
                    'fileName': 'project',
                    'totalIssues': len(issues),
                    'issues': issues
                }
            })
            
        finally:
            # 清理临时目录
            shutil.rmtree(temp_dir, ignore_errors=True)
            
    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({
            'code': 500,
            'msg': f'处理失败: {str(e)}',
            'data': None
        }), 500


def analyze_project(project_dir, task_type, description):
    """
    分析整个项目
    """
    issues = []
    
    # 遍历项目目录
    for root, dirs, files in os.walk(project_dir):
        for file in files:
            file_path = os.path.join(root, file)
            relative_path = os.path.relpath(file_path, project_dir)
            
            # 只处理代码文件
            if is_code_file(file_path):
                file_issues = analyze_file(file_path, relative_path, task_type)
                issues.extend(file_issues)
    
    return issues


def is_code_file(file_path):
    """
    判断是否是代码文件
    """
    code_extensions = ['.java', '.js', '.ts', '.py', '.cpp', '.c', '.cs', '.go', 
                       '.rs', '.php', '.rb', '.swift', '.kt', '.scala', '.html', 
                       '.css', '.vue', '.jsx', '.tsx']
    return any(file_path.endswith(ext) for ext in code_extensions)


def analyze_file(file_path, relative_path, task_type):
    """
    分析单个文件
    """
    issues = []
    
    try:
        with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
            lines = f.readlines()
            
            for line_num, line in enumerate(lines, start=1):
                # 根据任务类型进行检查
                if task_type == 'code_standard_check':
                    # 代码规范检查
                    if len(line.rstrip()) > 120:
                        issues.append({
                            'issueName': '行长度过长',
                            'severity': 'Low',
                            'lineNumber': str(line_num),
                            'description': f'{relative_path}:{line_num} 行超过120个字符',
                            'fixSuggestion': '将长行拆分为多行'
                        })
                
                elif task_type == 'data_security_audit':
                    # 数据安全审计
                    if 'password' in line.lower() and '=' in line:
                        issues.append({
                            'issueName': '可能的硬编码密码',
                            'severity': 'High',
                            'lineNumber': str(line_num),
                            'description': f'{relative_path}:{line_num} 可能包含硬编码密码',
                            'fixSuggestion': '使用环境变量或配置文件'
                        })
    
    except Exception as e:
        print(f"分析文件失败 {file_path}: {e}")
    
    return issues


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5004, debug=True)
```

## 测试建议

1. **测试单文件上传**：确保单文件上传仍然正常工作
2. **测试文件夹上传**：上传包含多个文件和子文件夹的文件夹
3. **测试路径处理**：确保相对路径正确重建目录结构
4. **测试中文路径**：如果文件名包含中文，确保编码正确

## 总结

- Java端会将相对路径作为 `filename` 传递给Flask
- Flask端需要根据 `filename` 重建目录结构
- 使用临时目录存储文件，处理完成后清理
- 确保正确处理文件编码和路径分隔符

