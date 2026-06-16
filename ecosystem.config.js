module.exports = {
    apps: [
        {
            name: 'rxhx-server',
            cwd: './server',
            script: 'npm',
            args: 'run start:prod',
            interpreter: 'none',   // 告诉 PM2 直接执行命令，不用 node 解释
        },
        {
            name: 'rxhx-client',
            cwd: './client',
            script: 'npm',
            args: 'run start:prod',
            interpreter: 'none',
        },
        {
            name: 'rxhx-admin',
            cwd: './admin',
            script: 'npm',
            args: 'run start:prod',
            interpreter: 'none',
        },
    ],
};