module.exports = {
    lintOnSave: false,

    devServer: {
        port: 28080,
        proxy: {
            '/': {
                ws: false,
                target: 'http://localhost:28081',
                changeOrigin: true,
                pathRewrite: {
                    '^/': '/'
                }
            }
        }
    }
}