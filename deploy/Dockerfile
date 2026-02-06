FROM eclipse-temurin:17-jdk

# 安装 Node.js 和 npm（用于 MCP 功能）
# 注意：如果不需要MCP功能，可以注释掉以下内容以加快构建速度
# RUN apt-get update && \
#     apt-get install -y curl && \
#     curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
#     apt-get install -y nodejs && \
#     npm install -g npm@latest && \
#     apt-get clean && \
#     rm -rf /var/lib/apt/lists/*

RUN mkdir -p /ruoyi/server/logs \
    /ruoyi/server/temp

WORKDIR /ruoyi/server
COPY ruoyi-admin.jar ruoyi-admin.jar

ENTRYPOINT ["java","-jar","ruoyi-admin.jar"]