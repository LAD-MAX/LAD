import { Request, Response, NextFunction } from 'express';
import { verifyToken } from '../utils/jwt';

export interface AuthRequest extends Request {
    user?: { userId: number; role: string };
}

export const authMiddleware = (req: AuthRequest, res: Response, next: NextFunction) => {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        return res.status(401).json({ success: false, message: '未登录，请先登录' });
    }

    const token = authHeader.split(' ')[1];
    try {
        const decoded = verifyToken(token) as any;
        req.user = { userId: decoded.userId, role: decoded.role };
        next();
    } catch (err) {
        return res.status(401).json({ success: false, message: 'Token 无效或已过期' });
    }
};

// 可选：管理员权限中间件
export const adminMiddleware = (req: AuthRequest, res: Response, next: NextFunction) => {
    if (req.user?.role !== 'ADMIN' && req.user?.role !== 'EDITOR') {
        return res.status(403).json({ success: false, message: '无权限' });
    }
    next();
};