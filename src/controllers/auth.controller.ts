import prisma from '../models';
import { Request, Response, NextFunction } from 'express';
import * as authService from '../services/auth.service';

export const register = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const { username, email, password } = req.body;
        if (!username || !email || !password) {
            return res.status(400).json({ success: false, message: '请填写所有字段' });
        }
        if (password.length < 6) {
            return res.status(400).json({ success: false, message: '密码长度不能少于6位' });
        }
        const data = await authService.register(username, email, password);
        res.json({ success: true, data });
    } catch (err: any) {
        if (err.message === '用户名或邮箱已存在') {
            return res.status(409).json({ success: false, message: err.message });
        }
        next(err);
    }
};

export const login = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const { username, password } = req.body;
        if (!username || !password) {
            return res.status(400).json({ success: false, message: '请填写用户名和密码' });
        }
        const data = await authService.login(username, password);
        res.json({ success: true, data });
    } catch (err: any) {
        if (err.message === '用户名或密码错误') {
            return res.status(401).json({ success: false, message: err.message });
        }
        next(err);
    }
};

export const getCurrentUser = async (req: any, res: Response, next: NextFunction) => {
    try {
        const user = await prisma.user.findUnique({ where: { id: req.user.userId }, select: { id: true, username: true, email: true, role: true, avatar: true } });
        if (!user) {
            return res.status(404).json({ success: false, message: '用户不存在' });
        }
        res.json({ success: true, data: user });
    } catch (err) {
        next(err);
    }
};