# Weather 天气应用

一个基于 **Open-Meteo API** 的 Android 天气查询应用，支持按城市名搜索地点并查看实时与多日天气预报。

## 功能特性

- 🔍 **地点搜索**：输入城市名实时搜索，带 500ms 防抖，避免频繁请求
- 🌍 **当前位置天气**：以定位坐标展示当前天气（温度、天气图标、天气描述、风力风向等）
- 🕐 **今日逐小时预报**：横向滚动展示当日逐小时温度与降水概率
- 📅 **未来 7 天预报**：展示每日最高/最低温度与降水概率
- ⚡ **响应式状态管理**：统一处理加载 / 成功 / 错误三种 UI 状态

## 技术栈

- **语言**：Kotlin 1.9.24
- **架构**：MVVM（ViewModel + Repository + Flow）
- **网络**：Retrofit 2.9 + Gson
- **异步**：Kotlin Coroutines + Flow

## 项目结构

```
app/src/main/java/com/example/weather/
├── MainActivity.kt             # 主界面，绑定 UI 并观察 ViewModel 状态
├── data/                      # 数据模型
│   ├── LocationResult.kt      # 地点搜索结果模型
│   └── WeatherResponse.kt     # 天气数据模型
├── network/                   # 网络层
│   └── NetworkModule.kt       # Retrofit 双 Base URL 客户端配置
├── repository/                # 数据仓库
│   └── WeatherRepository.kt   # 业务数据获取与统一状态处理
├── viewmodel/                 # 界面 ViewModel
│   └── WeatherViewModel.kt    # 承载 UI 状态，持有防抖 Job
├── adapter/                   # RecyclerView 适配器
└── util/                      # 工具类
    └── WeatherCode.kt         # WMO 天气代码 → 图标/描述映射
```
