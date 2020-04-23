# demo-web

> A Vue.js project
1.采用vue-cli技术搭建前端项目，搭建命令：
首先安装npm阿里镜像
npm i -g cnpm --registry=https://registry.npm.taobao.org

安装vue脚手架工具vue-cli
npm i -g vue-cli

测试是否安装成功（-V 要求大写）：
vue -V 

安装webpack工具
cnpm install -g webpack

工具安装完成后，开始创建项目（此时要根据提示进行操作，项目名称，描述，作者等等，还有yes/no
根据提示填写，然后直接enter）
vue init webpack 项目名

项目已创建完成，但是确实nodo_modules文件夹（文件夹是整个项目依赖包的文件夹）
接下来cd进入到项目目录下，cnpm install (如果安装了阿里的镜像，用cnpm代替npm，，否则还是npm就好)

完成后就可以使用IDE导入项目进行开发了，，
我用的是IDEA(要安装相关插件)
在setting中的plugins中搜索Vue.js插件并安装

运行项目命令(同理，，如果安装了阿里的镜像，用cnpm代替npm)
cnpm run dev 

完成后在浏览器输入地址访问，看到Vue图标表示成功
http://localhost:8080



## Build Setup

``` bash
# install dependencies
npm install

# serve with hot reload at localhost:8080
npm run dev

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report

# run unit tests
npm run unit

# run e2e tests
npm run e2e

# run all tests
npm test
```

For a detailed explanation on how things work, check out the [guide](http://vuejs-templates.github.io/webpack/) and [docs for vue-loader](http://vuejs.github.io/vue-loader).
