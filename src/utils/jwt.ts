import jwt from 'jsonwebtoken';
import { config } from '../config';

export const generateToken = (payload: { userId: number; role: string }) => {
    return jwt.sign(payload, config.jwtSecret, { expiresIn: '7d' });
};

export const verifyToken = (token: string) => {
    return jwt.verify(token, config.jwtSecret);
};