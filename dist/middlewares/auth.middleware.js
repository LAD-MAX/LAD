"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.adminMiddleware = exports.authMiddleware = void 0;
const jwt_1 = require("../utils/jwt");
const authMiddleware = (req, res, next) => {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ success: false, message: '未登录，请先登录' });
    }
    const token = authHeader.split(' ')[1];
    try {
        const decoded = (0, jwt_1.verifyToken)(token);
        req.user = { userId: decoded.userId, role: decoded.role };
        next();
    }
    catch (err) {
        return res.status(401).json({ success: false, message: 'Token 无效或已过期' });
    }
};
exports.authMiddleware = authMiddleware;
// 可选：管理员权限中间件
const adminMiddleware = (req, res, next) => {
    if (req.user?.role !== 'ADMIN' && req.user?.role !== 'EDITOR') {
        return res.status(403).json({ success: false, message: '无权限' });
    }
    next();
};
exports.adminMiddleware = adminMiddleware;
//# sourceMappingURL=auth.middleware.js.map