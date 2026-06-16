import prisma from '../models';
import bcrypt from 'bcryptjs';
import { generateToken } from '../utils/jwt';

export const register = async (username: string, email: string, password: string) => {
    // 检查用户是否存在
    const existingUser = await prisma.user.findFirst({
        where: { OR: [{ username }, { email }] },
    });
    if (existingUser) {
        throw new Error('用户名或邮箱已存在');
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const user = await prisma.user.create({
        data: {
            username,
            email,
            password: hashedPassword,
        },
    });

    const token = generateToken({ userId: user.id, role: user.role });
    return { user: { id: user.id, username: user.username, email: user.email, role: user.role, avatar: user.avatar }, token };
};

export const login = async (username: string, password: string) => {
    const user = await prisma.user.findUnique({ where: { username } });
    if (!user) {
        throw new Error('用户名或密码错误');
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);
    if (!isPasswordValid) {
        throw new Error('用户名或密码错误');
    }

    const token = generateToken({ userId: user.id, role: user.role });
    return { user: { id: user.id, username: user.username, email: user.email, role: user.role, avatar: user.avatar }, token };
};