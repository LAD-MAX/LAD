"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
const models_1 = __importDefault(require("../models"));
const express_1 = require("express");
const auth_middleware_1 = require("../middlewares/auth.middleware");
const router = (0, express_1.Router)();
router.use(auth_middleware_1.authMiddleware, auth_middleware_1.adminMiddleware);
// 文章 CRUD
router.post('/articles', async (req, res) => {
    const article = await models_1.default.article.create({ data: { ...req.body, authorId: req.user.userId } });
    res.json({ success: true, data: article });
});
router.put('/articles/:id', async (req, res) => {
    const article = await models_1.default.article.update({ where: { id: Number(req.params.id) }, data: req.body });
    res.json({ success: true, data: article });
});
router.delete('/articles/:id', async (req, res) => {
    await models_1.default.article.delete({ where: { id: Number(req.params.id) } });
    res.json({ success: true, message: '已删除' });
});
exports.default = router;
//# sourceMappingURL=admin.routes.js.map