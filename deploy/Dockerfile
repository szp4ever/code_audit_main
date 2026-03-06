FROM eclipse-temurin:17-jdk

# 安装中文字体（用于PDF导出）
# 将字体安装放在前面，这样即使JAR文件变化，字体层也可以被缓存复用
RUN apt-get update && \
    apt-get install -y fontconfig fonts-noto-cjk fonts-wqy-zenhei && \
    fc-cache -fv && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 安装 Node.js 和 npm（用于 MCP 功能）
# 注意：如果不需要MCP功能，可以注释掉以下内容以加快构建速度
# RUN apt-get update && \
#     apt-get install -y curl && \
#     curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
#     apt-get install -y nodejs && \
#     npm install -g npm@latest && \
#     apt-get clean && \
#     rm -rf /var/lib/apt/lists/*

# 创建工作目录（这一层也可以被缓存）
RUN mkdir -p /ruoyi/server/logs \
    /ruoyi/server/temp

WORKDIR /ruoyi/server

# JAR文件复制放在最后，这样只有JAR变化时才会重新构建这一层
COPY ruoyi-admin.jar ruoyi-admin.jar

#设置JVM内存参数，避免PDF生成时内存溢出
#-Xms: 初始堆内存，-Xmx: 最大堆内存，-XX:+UseG1GC: 使用G1垃圾回收器（适合大内存）
ENTRYPOINT ["java","-Xms512m","-Xmx2048m","-XX:+UseG1GC","-XX:MaxGCPauseMillis=200","-jar","ruoyi-admin.jar"]