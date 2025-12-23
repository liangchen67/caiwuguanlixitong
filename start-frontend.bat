@echo off
chcp 65001 >nul
echo ====================================
echo 正在启动前端服务...
echo ====================================
cd frontend

if not exist node_modules (
    echo 首次运行，正在安装依赖...
    npm install
)

npm run dev










