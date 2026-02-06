#!/bin/bash

# 后端一键重建脚本
# 功能：停止容器 -> 构建 JAR -> 复制到 deploy -> 构建镜像 -> 重启容器

set -e  # 遇到错误立即退出

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 配置
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
DEPLOY_DIR="$(cd "$(dirname "$0")" && pwd)"
IMAGE_TAG="ruoyi-ai-backend:v20251013"
CONTAINER_NAME="ruoyi-ai-backend"

echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  后端一键重建脚本${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""

# 0. 停止后端容器
echo -e "${GREEN}[0/5] 停止后端容器...${NC}"
cd "$DEPLOY_DIR"
docker compose stop ruoyi-backend 2>/dev/null || echo -e "${YELLOW}容器未运行或不存在，跳过停止步骤${NC}"
echo -e "${GREEN}✓ 容器已停止${NC}"
echo ""

# 1. 构建 JAR
echo -e "${GREEN}[1/5] 构建 JAR 文件...${NC}"
cd "$PROJECT_ROOT"
mvn -pl ruoyi-admin -am -Pprod clean package -DskipTests

if [ ! -f "ruoyi-admin/target/ruoyi-admin.jar" ]; then
    echo -e "${RED}错误: JAR 文件构建失败！${NC}"
    exit 1
fi
echo -e "${GREEN}✓ JAR 构建成功${NC}"
echo ""

# 2. 复制 JAR 到 deploy 目录
echo -e "${GREEN}[2/5] 复制 JAR 到 deploy 目录...${NC}"
cp ruoyi-admin/target/ruoyi-admin.jar "$DEPLOY_DIR/"
echo -e "${GREEN}✓ JAR 复制成功${NC}"
echo ""

# 3. 构建 Docker 镜像
echo -e "${GREEN}[3/5] 构建 Docker 镜像...${NC}"
cd "$DEPLOY_DIR"
docker build -t "$IMAGE_TAG" .

if [ $? -ne 0 ]; then
    echo -e "${RED}错误: Docker 镜像构建失败！${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Docker 镜像构建成功${NC}"
echo ""

# 4. 重启后端容器
echo -e "${GREEN}[4/5] 重启后端容器...${NC}"
docker compose up -d --force-recreate ruoyi-backend

if [ $? -ne 0 ]; then
    echo -e "${RED}错误: 容器启动失败！${NC}"
    exit 1
fi
echo -e "${GREEN}✓ 容器重启成功${NC}"
echo ""

# 显示日志
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}  重建完成！查看日志：${NC}"
echo -e "${YELLOW}  docker compose logs -f ruoyi-backend${NC}"
echo -e "${YELLOW}========================================${NC}"

# 询问是否查看日志
read -p "是否立即查看日志？(y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    docker compose logs -f ruoyi-backend
fi
