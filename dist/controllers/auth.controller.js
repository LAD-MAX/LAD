"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.getCurrentUser = exports.login = exports.register = void 0;
const models_1 = __importDefault(require("../models"));
const authService = __importStar(require("../services/auth.service"));
const register = async (req, res, next) => {
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
    }
    catch (err) {
        if (err.message === '用户名或邮箱已存在') {
            return res.status(409).json({ success: false, message: err.message });
        }
        next(err);
    }
};
exports.register = register;
const login = async (req, res, next) => {
    try {
        const { username, password } = req.body;
        if (!username || !password) {
            return res.status(400).json({ success: false, message: '请填写用户名和密码' });
        }
        const data = await authService.login(username, password);
        res.json({ success: true, data });
    }
    catch (err) {
        if (err.message === '用户名或密码错误') {
            return res.status(401).json({ success: false, message: err.message });
        }
        next(err);
    }
};
exports.login = login;
const getCurrentUser = async (req, res, next) => {
    try {
        const user = await models_1.default.user.findUnique({ where: { id: req.user.userId }, select: { id: true, username: true, email: true, role: true, avatar: true } });
        if (!user) {
            return res.status(404).json({ success: false, message: '用户不存在' });
        }
        res.json({ success: true, data: user });
    }
    catch (err) {
        next(err);
    }
};
exports.getCurrentUser = getCurrentUser;
//# sourceMappingURL=auth.controller.js.map